Test Branch
Random Stuff:
1.
2.
3.
4.
5.
6.
7.
8.
9.
10.

# Usage

### To run the server, use 

`gradle -PmainClass=ece651.risc.team11.RiscServer run`

### To run the client, use

`gradle -PmainClass=ece651.risc.team11.RiscGUIClient run`

### You can change the port number and ip address in 

`src/main/resouces/config.property`


# How to play the game

### Room Owner
The first player(the room owner) who joins the game has the authentication 
to decide how many player he/she wants to play with in this game. Before he 
gives an answer, other players cannot join the game.

### Greeting
After you connect to server, you will receieve a greeting message with your 
player ID. Please remember your ID to know whether you win the game after game is over.

### Ready State
When you are ready, enter "yes". Then, game starts.

### Invalid Actions
If at least one of your actions is invalid, you will have to re-enter all of 
your actions.

### Player Loses
After one player loses, he can watch the result after each turn but cannot 
enter actions anymore.

### Player Lost Connection
If one or more player lost connection, the game will restart and wait for 
other new players to join in the game until the number of player reaches the 
number that room owner sets at the first beginning.

### Player Wins
After we have a winner, the game will show who wins (the player ID) and 
restarts the game automatically.
