# Shobu AI Playground

This application implements the board game Shobu. The players of the game are subprocesses 
which implement the AI for each player. Communication between the AIs and the game engine is done 
by sending the game state to the AI subprocesses on their STDIN and reading their turns from STDOUT.

## Building

To compile and run tests, use:
```bash
./gradlew Engine:build
``` 

To generate a runnable jar file use:
```bash
./gradlew Engine:fatJar
```

## Running

The commands to start the AI subprocesses should be supplied as arguments to the game engine, 
for example:
```bash
java -jar shobu.jar "java -jar bot-one.jar" "python3 bot-two.py"
``` 

This allows connecting bots written in any language to the game engine.

You can examine and run the integration testing script for an example of how the game engine
can be used in practice: 
```bash
./Engine/integration_script.sh
```

## AI Communication

Subprocesses must communicate with the Shobu game engine by receiving a JSON representation of the game 
state and responding with a JSON representation of their intended move. Two examples follow:

**Game state sent by engine**

```json
{
  "type": "game_state",
  "payload": {
    "board": "oooooooo................xxxxxxxxoooooooo................xxxxxxxx",
    "turn": "BLACK",
    "turnNumber": 0
  }
}
```

The board is represented as a string with white stones represented by the letter 'o' 
and black stones represented by the letter 'x'. The character '.' denotes an empty space.
Spaces are sent starting with the upper leftmost space (0,0) going right and then down
to the next line. The example above shows the board in the default start-of-game configuration.

**Player move sent by subprocess**

```json
{
  "type": "turn",
  "payload": {
    "passive": {
      "origin": {
        "x": 0,
        "y": 7
      },
      "heading": {
        "x": 0,
        "y": -1
      }
    }, 
    "aggressive": {
      "origin": {
        "x": 4,
        "y": 3
      },
      "heading": {
        "x": 0,
        "y": -1
      }
    }
  }
}
```

This model of communication between processes and segregation of AI from the game engine was inspired
by the [Halite](https://github.com/HaliteChallenge/Halite-III) engine, which works effectively the same way. 

## Game logging

The Shobu game engine outputs a JSON representation of the entire game that was played when it ends. 
The format of this game output is like so (abbreviated repeating structures with ellipses): 

```json
{
   "winner" : "BLACK",
   "turns" : [
      {
         "passive" : {
            "origin" : {
               "x" : 1,
               "y" : 7
            },
            "heading" : {
               "x" : 1,
               "y" : -1
            }
         },
         "aggressive" : {
            "origin" : {
               "x" : 6,
               "y" : 7
            },
            "heading" : {
               "x" : 1,
               "y" : -1
            }
         }
      },
      ...
      {
         "passive" : {
            "origin" : {
               "y" : 6,
               "x" : 2
            },
            "heading" : {
               "y" : -1,
               "x" : -1
            }
         },
         "aggressive" : {
            "origin" : {
               "y" : 5,
               "x" : 5
            },
            "heading" : {
               "x" : -1,
               "y" : -1
            }
         }
      }
   ],
   "game_states" : [
      {
         "turn" : "BLACK",
         "board" : "oooooooo................xxxxxxxxoooooooo................xxxxxxxx",
         "turnNumber" : 0
      },
      ...
      {
        "turn" : "BLACK",
         "board" : "o.....ooo.o.o.....o.....xxxx.ox...o.o.....x..x...xx...x......x..",
         "turnNumber" : 58
      }
   ]
}

```


## JSON Pass-through mode

You can use the game engine in JSON pass-through mode where a game state and a turn are provided to the engine
on standard input. The engine will process the turn based on the game state provided, transition the board and output
the new game state representation as JSON on standard output. An example of usage follows:

```bash
cat gamestate-and-turn.json | java -jar Engine.jar --json-pass-through > new-gamestate.json
``` 

The JSON input used with pass-through mode should be formatted like so:

```json
{
  "turn": {
    "passive": {
      "origin": {
        "x": 6,
        "y": 7
      },
      "heading": {
        "x": 0,
        "y": -1
      }
    },
    "aggressive": {
      "origin": {
        "x": 3,
        "y": 3
      },
      "heading": {
        "x": 0,
        "y": -1
      }
    }
  },
  "gamestate": {
    "board": "oooooooo................xxxxxxxxoooooooo................xxxxxxxx",
    "turn": "BLACK",
    "turnNumber": 0
  }
}
```

## Board layout

The game board is represented as an 8x8 grid with coordinates (0,0) located in the upper
leftmost space and (7,7) in the lower rightmost space.

## TODO

* Make the Engine output easily parsable by an analysis tool (eg, output JSON representing sequence of gamestates & moves)
* (Maybe) Make the communication between Engine and AI a well-formed JSON array instead of just a sequence of objects. TBD if this is necessary.

## License

Copyright 2020 Brandon S. Foltz

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
