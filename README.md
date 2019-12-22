# Shobu AI Playground

This application implements the board game Shobu. The players of the game are subprocesses 
which implement the AI for each player. Communication between the AIs and the game engine is done 
by sending the game state to the AI subprocesses on their STDIN and reading their turns from STDOUT.

## Building

To compile and run tests, use:
```bash
./gradlew build
``` 

## Running

The names AI programs you wish to play against each other should be passed as arguments to the 
Shobu game engine. For example:

```bash
java -jar shobu.jar "bot-one.jar" "bot-two.py"
``` 

I intend to support several different runtime environments so that AI's can be written in whatever
language you like, but in the near term I will be only supporting Java and Python AIs.