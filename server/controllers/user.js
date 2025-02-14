const userService = require('../services/user')
const jwt = require("jsonwebtoken")

const createUser = async (req, res) => {
    // check fields
    let errors = {
        "errors": [],
        "title": "One or more validation errors occurred."
    }
    if (!req.body.username) {
        errors.errors.push('Username is required');
    }
    if (!req.body.password) {
        errors.errors.push('Password is required');
    }
    if (!req.body.displayName) {
        errors.errors.push('DisplayName is required');
    }
    if (!req.body.profilePic) {
        errors.errors.push('ProfilePic is required');
    }
    if (errors.errors.length > 0) {
        return res.status(400).end();
    }

    const json = await userService.createUser(req.body.username, req.body.password, req.body.displayName, req.body.profilePic);
    if (!json) {
        return res.status(409).end();
    }
    res.end();
};

const getUserByUsername = async(req, res) => {
    if (req.params.username != req.username) {
        return res.status(401).end();
    }
    const data = await userService.getUserByUsername(req.username);
    res.json(data);
};

const getToken = async(req, res) => {
    let errors = {
        "errors": [],
        "title": "One or more validation errors occurred."
    }
    if (!req.body.username) {
        errors.errors.push('Username is required');
    }
    if (!req.body.password) {
        errors.errors.push('Password is required');
    }
    if (errors.errors.length > 0) {
        return res.status(400).end();
    }

    const user = await userService.tryLogin(req.body.username, req.body.password);
    if (!user) {
        return res.status(404).end();
    }

    // if sucess
    const data = { username: user.username, id: user._id }
    const token = jwt.sign(data, process.env.TOKEN_KEY)

    res.end(token);
};


const isLoggedIn = (req, res, next) => {
    if (req.headers.authorization) {
        const token = req.headers.authorization.split(" ")[1];
        try {
            const data = jwt.verify(token, process.env.TOKEN_KEY);
            req.username = data.username;
            req.id = data.id;
            return next()
        } catch (err) {
            return res.status(401).end();
        }
    }
    else
        return res.status(401).end();
}

module.exports = {createUser, getUserByUsername, getToken, isLoggedIn};