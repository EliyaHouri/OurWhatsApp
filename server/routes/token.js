const express = require('express');
var router = express.Router();
const userController = require('../controllers/user');

router.route('/').post(userController.getToken);

module.exports = router;