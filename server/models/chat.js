const mongoose = require('mongoose');
var ObjectId = require('mongodb').ObjectId;
const Schema = mongoose.Schema;
const Chat = new Schema({ 
    users: [{ type : ObjectId, ref: 'User' }],
    messages: [{ type : ObjectId, ref: 'Message' }]
}); 

module.exports = mongoose.model('Chat', Chat);