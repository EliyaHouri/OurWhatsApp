import { getUserByID } from './user';
const express = require('express');
const app = express();
const http = require('http');
const server = http.createServer(app);
const { Server } = require("socket.io");
const io = new Server(server);

app.use(express.static('public'));

const socketsMap = new Map();

io.on("connection", (socket) => {
    // Handle the 'login' event
  socket.on("login", (username) => {
    // Store the socket with the username in the map
    socketsMap.set(username, socket);
  }),

    socket.on("newMsg", (data) => {
      // Handle the 'newMsg' event

      let reciverUsername = getUserByID(data.reciverId);
      if (socketsMap.has(reciverUsername)) {
        socketsMap.get(reciverUsername).emit("newMsg", {
          sender: data.sender,
        })
      }
    });

  socket.on("disconnect", () => {
    let disconnectedUser;
    for (const [key, value] of socketsMap) {
      if (value === socket) {
        disconnectedUser = key;
        break;
      }
    }
    if (disconnectedUser) {
      const [username] = disconnectedUser;
      socketsMap.delete(username); // Remove the client from the map

      // Delete when finish
    }
  });
});
