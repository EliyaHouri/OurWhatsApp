const express = require('express');
var router = express.Router();
const userController = require('../controllers/user');
const chatController = require('../controllers/chat');

router.route('/')
    .post(userController.isLoggedIn, chatController.createChat)
    .get(userController.isLoggedIn, chatController.getChats);

router.route('/:id')
    .delete(userController.isLoggedIn, chatController.checkID, chatController.deleteChat)
    .get(userController.isLoggedIn, chatController.checkID, chatController.getChat);

router.route('/:id/Messages')
    .post(userController.isLoggedIn, chatController.checkID, chatController.sendMessage)
    .get(userController.isLoggedIn, chatController.checkID, chatController.getMessages);

module.exports = router;