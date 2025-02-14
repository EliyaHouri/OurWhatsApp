# OurWhatsApp

OurWhatsApp is an intuitive messaging web & android application that facilitates real-time communication between users. The application showcases a sleek, modern interface equipped with user registration, authentication and messaging features.
The project includes both clients and server (Node.JS application). Each one of them can run seperately to allow using with different application for the second end.

## Getting Started

Follow these instructions to set up the project on your local machine:

1. Ensure a web browser is installed to run HTML and CSS files.
2. Ensure you have Android Studio installed and an emulator.
3. Ensure you have mongodb installed.
4. Enter the following commands in your terminal:
<pre>
git clone https://github.com/yonikal56/our-whatsapp-3
cd our-whatsapp-3
</pre>

### Run Server

Follow these instruction to run the server on your local machine:

1. Go to "server" folder inside the project.
2. Run "npm install".
3. Update config/.env.local file. Template is provided with the server key:
<pre>
CONNECTION_STRING="mongodb://127.0.0.1:27017"
SERVER_KEY="AAAAhJKNxlE:APA91bG-EiB3fKIfvk-aYV0QyzmGXUjSeGWR_hEDUT7yOoXOLWpbXQPKNjdjLGQMac1WtPg8aeaJcH7yXfh40T5DCgbkFzk43WkLm__v8eo_MOKHIrrQL-S0Iugd3zCf2ZMFB65w19Nl"
PORT=12345
DB_NAME="our-whatsapp-db"
TOKEN_KEY="secretkeyy!"
</pre>

- CONNECTION_STRING is the connection string of mongodb (for example, if installed locally it will be something like mongodb://127.0.0.1:27017). Change if needed.
- SERVER_KEY is the connection string to the firebase service. Change if needed, but notice you need to change the google services file in the android application if you do so.
- DB_NAME is the name of the database inside mongodb. Also can be changed with the default value of our-whatsapp-db.
- PORT is the port the server runs on, adapt if needed (for example if the post is already used).
- TOKEN_KEY is a secret key for the JWT tokens the server uses for authentication. Set it to a secret string of your choice. You should change the default value.

If you want you can also change .env.test file and .env file (with the same fields), but you only need one of them to run the server correctly.

To run the server after this file is set, run one of the following commands:

1. "npm start" to use .env.local file.
2. "npm test" to use .env.test file.
3. "node app.js" to run with .env file.

If you run the server, you can access the client from the browser by navigating to the base url of the server or from the Android studio, simple by running it on the emulator or your android physical device.

### Run Client on Android Studio

simply run it on the emulator.

### Run Client on your Android Phone

1. enable developer mode in you android machine.
2. connect it to the computer and install the application on the physical devices via the Device manager.
3. please notice to change the ip of the server in the application via the setting screen to match the wifi the server is running on.
4. enjoy :)

## Features

- User registration and login, complete with validation against a list of registered users.
- Denial of access to the messages activity without a registered user.
- User, friends, and messages data are stored in local DB.
- Responsive design.
- Add friends, send messages, and display conversations in their chat.
- Nontification of a new message via firebase service.
- Navigate between chats.
- Delete chat by long click inside the chats activity.
- Add chat using the designated button inside chats acitivity.
- Display time, last messages and friends photos of each conversation.
- Can't see nontifications from a chat created before you entered the chats activity again - preventng annoying people by allowing you to delete the chat before any messages delivered to you.
