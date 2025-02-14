const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const users = require('./routes/user');
const tokens = require('./routes/token');
const chats = require('./routes/chat');

require('custom-env').env(process.env.NODE_ENV, './config');

mongoose.connect(process.env.CONNECTION_STRING + '/' + process.env.DB_NAME,
    { useNewUrlParser: true, useUnifiedTopology: true });

var app = express();

const http = require('http');
const server = http.createServer(app);
const { Server } = require("socket.io");
const io = new Server(server, {
    cors: {
        origin: "*"
    }
});

const cors = require("cors");
app.use(cors());
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json({limit : '1000mb'}));

app.use(express.static('public'));
app.get('/messages', function(req, res) {
  res.sendFile('index.html', {root: 'public'});
});
app.get('/login', function(req, res) {
  res.sendFile('index.html', {root: 'public'});
});
app.get('/register', function(req, res) {
  res.sendFile('index.html', {root: 'public'});
});

app.use('/api/Tokens', tokens);
app.use('/api/Users', users);
app.use('/api/Chats', chats);

io.on("connection", (socket) => {
    // Delete when finish
      socket.on("newMsg", (chatId) => {
        // Handle the 'newMsg' event
        socket.broadcast.emit("newMsg", { chatId: chatId });
        socket.broadcast.emit("newMsg", { chatId: chatId });
      });
  });

server.listen(process.env.PORT);