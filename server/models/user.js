const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const User = new Schema({ 
    username: { type: String, required: true }, 
    password: { type: String, required: true }, 
    displayName: { type: String, required: true }, 
    profilePic: { type: String, required: true }, 
}); 

module.exports = mongoose.model('User', User);