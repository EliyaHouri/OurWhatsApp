const chatService = require('../services/chat')

const createChat = async (req, res) => {
    if (!req.body.username) {
        return res.status(400).end();
    }
    const json = await chatService.createChat(req.username, req.body.username);
    if (!json) {
        return res.status(400).end();
    }
    res.json(json);
};

const getChats = async (req, res) => {
    const json = await chatService.getChats(req.username);
    if (!json) {
        return res.status(400).end();
    }
    res.json(json);
};

const getChat = async (req, res) => {
    const json = await chatService.getChat(req.username, req.params.id);
    if (!json) {
        return res.status(401).end();
    }
    res.json(json);
};

const deleteChat = async (req, res) => {
    const json = await chatService.deleteChat(req.username, req.params.id);
    if (!json) {
        return res.status(404).end();
    }
    res.status(204).end();
};

const getMessages = async (req, res) => {
    const json = await chatService.getMessages(req.username, req.params.id);
    if (!json) {
        return res.status(401).end();
    }
    res.json(json);
};

const sendMessage = async (req, res) => {
    if (!req.body.msg) {
        return res.status(400).end();
    }
    const json = await chatService.sendMessage(req.username, req.params.id, req.body.msg);
    if (!json) {
        return res.status(401).end();
    }
    res.json(json);
};

const checkID = (req, res, next) => {
    if (req.params.id) {
        var ObjectId = require('mongoose').Types.ObjectId;
        if (!ObjectId.isValid(req.params.id)) {
            return res.status(400).end();
        }
        return next();
    }
    else
        return res.status(400).end();
}

module.exports = {createChat, getChats, getChat, deleteChat, sendMessage, getMessages, checkID};