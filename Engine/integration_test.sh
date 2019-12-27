#!/usr/bin/env bash

./gradlew Engine:fatJar
java -jar Engine/build/libs/Engine.jar "cat Engine/src/test/resources/black0.txt" "cat Engine/src/test/resources/white1.txt" | tee "engine.out"
winner=$(tail -n 1 "engine.out")
if [[ "Winner is: WHITE" == "$winner" ]]; then
  echo "Integration test succeeded!"
  exit 0
else
  echo "Integration test failed."
  exit 1
fi
