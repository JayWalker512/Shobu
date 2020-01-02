#!/usr/bin/env bash

./gradlew Engine:fatJar

# first test
java -jar Engine/build/libs/Engine.jar "cat Engine/src/test/resources/black0.txt" "cat Engine/src/test/resources/white1.txt" | tee "engine.out"
winner=$(tail -n 1 "engine.out")
if [[ "Winner is: WHITE" == "$winner" ]]; then
  echo "Integration test succeeded!"
else
  echo "Integration test failed."
fi

# pass through test
expectedOutput=$(cat "Engine/src/test/resources/game_transitioned.json")
output=$(cat Engine/src/test/resources/game_and_turn.json | java -jar Engine/build/libs/Engine.jar --json-pass-through)
if [[ "$output" == "$expectedOutput" ]]; then
  echo "JSON pass-through test succeeded!"
else
  echo "JSON pass-through test failed."
fi