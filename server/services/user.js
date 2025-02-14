const User = require('../models/user')
const jwt = require("jsonwebtoken")

const createUser = async (username, password, displayName, profilePic) => {
    try {
        const user = await getUserByUsername(username);
        if (!user) {
            const user = new User({
                username,
                password,
                displayName,
                profilePic
            });
            return await user.save();
        } return false
    } catch {
        return false;
    }
};

const getUserByUsername = async (username) => {
    try {
        return await User.findOne({ username: username }).select('username displayName profilePic -_id');
    } catch {
        return false;
    }
};

const getUserByID = async (id) => {
    try {
        return await User.findById(id).select('username displayName profilePic -_id');
    } catch {
        return false;
    }
};

const getIDByUsername = async (username) => {
    try {
        return await User.findOne({ username: username }).select('username displayName profilePic _id');
    } catch {
        return false;
    }
};

const tryLogin = async (username, password) => {
    try {
        const user = await User.findOne({ username: username, password: password });
        if (!user) {
            return false;
        }
        return user;
    } catch {
        return false;
    }
};


module.exports = { createUser, getUserByUsername, tryLogin, getIDByUsername, getUserByID };