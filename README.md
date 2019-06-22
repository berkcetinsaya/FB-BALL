# FB-BALL

This is a Java-based brick breaking game like DX-BALL. This was our term project for Advance Programming class.

## The purpose of our project

First of all, our project is about a ball-brick game. There is a ball and a paddle on the screen. The ball is moving around and hit the bricks above the screen. Then, when ball turns back, player should catch it with the paddle. After that, ball will move again the opposite direction. If player do not catch the ball, a life will be removed. Player has 3 lives. After 3 lives, GAME OVER, but if you are lucky enough, you can win.

## Explanation of our project's source code (Minimums)

As it can be seen in the source files, there are a great amount of code lines and there is not unnecessary lines in there actually. Therefore, I want to explain the most important parts.

The first minimum criteria was the proper object oriented (OO) hierarchy (E.g. Bricks, paddle types can be implemented that way. Use concepts of interfaces, abstract classes while designing OO hierarchy). We used OO hierarchy to create all of the objects and components. There are 7 classes which have some properties for the specific objects. If an example should be given, we have a `Ball.java` file and it has ball's x and y coordinates, speed, velocity, etc. and the others are `Enemy.java`, `Player.java`, etc.

The second minimum criteria was the mouse or key listeners (at least one of them to control your paddle). We used both of them to move the paddle. The paddle was moved by right and left arrows or mouse, it is player's choice. The player object's x coordinate is setting up with mouse cursor's x coordinate. Additionally, there are other key listeners for some action. For instance, when you press “G”, the godmode is turned on and there are some different features on this mode. We will explain later in the bonus part explanation.

The third minimum criteria was the minimal GUI elements (a button, a check box and a text field). We created a main menu which extends JFrame. It has actually 5 buttons, and 1 label for the image and after that there are text fields for saving scores.

The fourth minimum criteria was graphics (Java 2D API usage while designing the graphical elements). We have chosen to use Graphics2D libraries and objects. We created rectangles by `fillRect()` function for the enemies and paddle. We created ovals such as ball and bullets by `fillOval()` function and to show a string on the graphic panel, we used `drawString()` function.

The fifth minimum criteria was the animation. We tried to create some animation effects when the ball touches to a brick. Generally, when a ball hit a brick there is no effects. But, if the brick has two lives, it means brick will be destroyed after two hits, brick changes its color for a moment to white. Then, we think it can be counted as an animation.

The seventh minimum criteria is high score feature. We have a high scores table. During a game, player can see player's score at the top right of the screen. We did more. I will explain the bonus part.

Consequently, we have achieved all minimum criteria. 

## Explanation of our project's source code (Bonuses)

In this part, we will talk about our bonus parts. We developed all bonus parts successfully. We tried and enjoyed all of bonus part of this code. 

The first bonus criteria is that when you hit a brick, there is a chance that a powerup will float downwards towards the paddle, and can be picked up by touching it with the paddle. 

Certain powerups have positive effects, while others have negative, making it important to try to collect the beneficial powerups while avoiding the detrimental powerups. (minimum criteria for 3 people groups). We have done 8 possible powerups. 5 of them have possitive effects and the others have negative effects.

- +1 life
- -1 life
- Fire
- Slow down time
- Speed up
- Width+
- Width-
- Through brick

The second bonus criteria was the good game experience/pleasure (minimum criteria for 3 people groups). We tried to give you an amazing game experience and did some testing with our friends. They have played our game and they expressed their very positive opinions.

The third bonus criteria was that save and load high score information to/from a disk (minimum criteria for 3 people groups). We save all scores to `'Scores.txt'` file. There is a `writeFile()` function to do this in the Player.java. After that, we have a `ReadFile.java` class to read from the `Scores.txt` file and we show all scores to the player on the game over screen. It means we did this bonus in an appropriate way.

The fourth bonus criteria was using multiple threads. We used multiple threads to execute the chat section in our game. Normally, we used a thread to run our game but after adding the chat part to our code, there should be a different thread. Therefore, we used multiple threads. There is a class called as `ChatThread.java`. We created a `ChatThread` object in our `GamePanel.java` class. So, in `ChatThread`, we extended `Thread` and `run()` function. In the `run()` function, `Server` and `Client` objects were created. In conclusion, two thread run in the meantime without errors.

The fifth bonus criteria was the network sockets. As we mentioned above, we achieved to establish a chat server. We used socket connection to connect to the other pair to chat with each other. 

The sixth bonus criteria and the most important bonus is the network multiplayer game. We achieved it, sort of :). We used sockets again. In global or local network, we can play seperate games and there is a paddle above of the screen. When player1 moves the paddle, on the other screen player2 can see the player1's paddle on his/her own screen without an interaction. It is a multiplayer game in theory. 

To sum up, we did all minimum and bonus criterias for this project. In addition, maybe you can want to press the “G” key during the gameplay.

We would like to thank to our advisor Assist. Prof. Ph.D Tarkan Aydin and Teaching Assist. Erkut Arican for their precise time to help us. We also would like to thank to our valuable friends Nazim Gurkan Demir, Eren Atas, Reha Ok and Tahir Esirgen to give us their valuable feedback and to support us.