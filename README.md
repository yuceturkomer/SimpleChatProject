# SimpleChatProject
So simple chat program. Uses python as "server" and Java/Fx for "client"

The project was an idea in the begninning. Python has a limited option for drag&drop gui creation. We can use pyQt but i thought i can make the gui with JavaFX and i send the necessary info to python so i can do my job in python side. The project has only one purpose: what can i do more?
Everybody can access and improve the code as they want.

pyServer code is under the project. It is a Netbeans project for Java side. You can import to another IDE or something else.
Commands for running(For test purposes, i assumed that you are the only client and you want to open 2 java programs):
python pyServer.py & java -jar SimpleChat.jar & java -jar SimpleChat.jar

Improvements i will do in the future(just the ideas for now):
- Login Screen and Sign Up Screen (id, Name, password is enough. It is "simple" chat program. ) ***
- General Error box (Anything can happen!) ***
- For now, you can message everyone. I want to create a contacts area (maybe with a tab pane) and everyone will has his/her own privacy. *
- Definitely better GUI experience *
- Every information is kept in pyServer. I will use MongoDB for the users and let the server check only current users. *
