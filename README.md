# Shobu AI Playground

This application implements the board game Shobu. The players of the game are subprocesses 
which implement the AI for each player. Communication between the AIs and the game engine is done 
by sending the game state to the AI subprocesses on their STDIN and reading their turns from STDOUT.

## Builing

To compile and run tests, use:
```bash
./gradlew build
``` 