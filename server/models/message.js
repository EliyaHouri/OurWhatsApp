const mongoose = require('mongoose');
const Schema = mongoose.Schema;
var ObjectId = require('mongodb').ObjectId;

const Message = new Schema({
    created: { type: Date, default: Date },
    sender: { type: ObjectId, ref: 'User' },
    content: { type: String, required: true }
});

module.exports = mongoose.model('Message', Message);