#!/usr/bin/env bash

./gradlew fatJar
java -jar build/libs/Shobu.jar "cat src/test/resources/black0.txt" "cat src/test/resources/white1.txt" | tee "shobu.out"
winner=$(tail -n 1 "shobu.out")
if [[ "Winner is: WHITE" == "$winner" ]]; then
  echo "Integration test succeeded!"
  exit 0
else
  echo "Integration test failed."
  exit 1
fi
