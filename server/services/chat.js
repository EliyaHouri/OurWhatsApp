var ObjectId = require('mongodb').ObjectId;
const UserService = require('./user')
const Chat = require('../models/chat')
const Message = require('../models/message');
const user = require('../models/user');

const createChat = async (firstUsername, secondUsername) => {
    try {
        // compare users, if not equal create new chat - return id and user
        if (firstUsername == secondUsername) {
            return false;
        }
        const user1 = await UserService.getIDByUsername(firstUsername);
        const user2 = await UserService.getIDByUsername(secondUsername);
        if (!user1 || !user2) {
            return false;
        }
        const user = new Chat({
            users: [
                user1._id,
                user2._id
            ],
            messages: []
        });
        const chat = await user.save();
        return {
            id: chat._id,
            user: {
                username: user2.username,
                displayName: user2.displayName,
                profilePic: user2.profilePic
            }
        };
    } catch {
        return false;
    }
};

const getChats = async (username) => {
    try {
        const user = await UserService.getIDByUsername(username);
        if (!user) {
            return false;
        }
        const chats = await Chat.find({ users: user._id });

        // Map each chat to a Promise that will fetch its details
        const chatPromises = chats.map(async (chat) => {
            let returnChat = {
                id: chat._id
            };
            const secondUser = chat.users[0].equals(user._id) ? chat.users[1] : chat.users[0];
            returnChat.user = await UserService.getUserByID(secondUser);
            returnChat.lastMessage = null;
            if (chat.messages.length > 0) {
                const messageID = chat.messages.slice(-1)[0];
                const last = await getMessageByID(messageID);
                returnChat.lastMessage = {
                    id: messageID,
                    created: last.created,
                    content: last.content
                };
            }
            return returnChat;
        });

        // Execute all the promises in parallel and wait for them to finish
        const returnChats = await Promise.all(chatPromises);

        return returnChats;
    } catch {
        return false;
    }
};


const getMessageByID = async (ID) => {
    try {
        return await Message.findById(ID);
    } catch {
        return false;
    }
}

const getChat = async (username, chatID) => {
    try {
        // check if chatID exists and sender part of it
        const user = await UserService.getIDByUsername(username);
        if (!user) {
            return false;
        }
        // get chat the username is part of
        let chat = await Chat.findById(chatID).findOne({ users: user._id }).populate([{
            path: 'messages',
            select: 'created sender content',
            populate: {
                path: 'sender',
                select: 'username displayName profilePic -_id'
            }
        }, {
            path: 'users',
            select: 'username displayName profilePic -_id'
        }]);
        if (!chat) {
            return false;
        }
        chat = await chat.toJSON();
        for (i = 0; i < chat.messages.length; i++) {
            chat.messages[i].id = chat.messages[i]._id
            delete chat.messages[i]._id
            delete chat.messages[i].__v
        }
        chat.id = chat._id
        delete chat._id
        delete chat.__v
        return chat;
        // get user id and get chat ID if part of - id, users, messages
    } catch {
        return false;
    }
}

const deleteChat = async (username, chatID) => {
    try {
        // if user part of chat - delete
        const chat = await getChatByUserAndID(username, chatID);
        if (!chat) {
            return false;
        }
        return await Chat.deleteOne({ "_id": chat._id });
    } catch {
        return false;
    }
}

const sendMessage = async (sender, chatID, messageContent) => {
    try {
        // check if chatID exists and sender part of it
        const chat = await getChatByUserAndID(sender, chatID);
        if (!chat) {
            return false;
        }
        let user = await UserService.getIDByUsername(sender);
        if (!user) {
            return false;
        }

        // if so, send message on this chat via this sender
        const message = new Message({
            sender: user._id,
            content: messageContent
        });
        const newMessage = await message.save();

        const upda = await Chat.findOneAndUpdate(
            { _id: chat._id },
            { $push: { messages: newMessage._id } }
        );
        if (!upda) {
            return false;
        }

        // send push notification
        var FCM = require('fcm-node');

        var serverKey = process.env.SERVER_KEY;
        const secondUser = chat.users[0].equals(user._id) ? chat.users[1] : chat.users[0];
        const secondUserData = await UserService.getUserByID(secondUser);
        var topic1 = chatID + "_" + secondUserData.username;
        var fcm = new FCM(serverKey);


        const firstUsername = chat.users[0].equals(user._id) ? chat.users[0] : chat.users[1];
        const firstUserData = await UserService.getUserByID(firstUsername);

        var pushNotificationMessage = {
            to: "/topics/" + topic1,
            notification: {
                title: 'Message From: ' + firstUserData.displayName,
                body: newMessage.content
            },
            data: {
                chatID: chatID,
                content: newMessage.content,
            }
        };

        fcm.send(pushNotificationMessage, function (err, response) {
            if (err) {
                console.log(err);
            }
        });

        return {
            id: newMessage._id,
            created: newMessage.created,
            sender: {
                username: user.username,
                displayName: user.displayName,
                profilePic: user.profilePic
            },
            content: newMessage.content
        };
        // return - id, created, sender, content
    } catch {
        return false;
    }
}

const getChatByUserAndID = async (username, chatID) => {
    try {
        const user = await UserService.getIDByUsername(username);
        if (!user) {
            return false;
        }
        // get chat the username is part of
        const chat = await Chat.findById(chatID).findOne({ users: user._id });
        if (!chat) {
            return false;
        }
        return chat;
    } catch {
        return false;
    }
}

const getMessages = async (username, chatID) => {
    try {
        // check if chatID exists and sender part of it
        const user = await UserService.getIDByUsername(username);
        if (!user) {
            return false;
        }
        // get chat the username is part of
        let chat = await Chat.findById(chatID).findOne({ users: user._id }).populate({
            path: 'messages',
            select: 'created sender content',
            populate: {
                path: 'sender',
                select: 'username -_id'
            }
        });
        if (!chat) {
            return false;
        }
        chat = await chat.toJSON();
        chat.messages = chat.messages.reverse();
        for (i = 0; i < chat.messages.length; i++) {
            chat.messages[i].id = chat.messages[i]._id
            delete chat.messages[i]._id;
        }
        return chat.messages;

        // return - id, created, sender (only username), content
    } catch {
        return false;
    }
}

module.exports = { createChat, getChats, getChat, deleteChat, sendMessage, getMessages };