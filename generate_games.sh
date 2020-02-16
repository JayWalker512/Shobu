#!/usr/bin/env bash
mkdir "games"
cnt=1;
while [ 1 ]; do
  game=$(java -jar Engine/build/libs/Engine.jar "java -jar AIStarterKit/build/libs/AIStarterKit.jar" "java -jar AIStarterKit/build/libs/AIStarterKit.jar")
  json=$(echo "$game" | ../Shobu_Analysis/target/release/turn_limiter)
  if [[ "$json" != "" ]]; then
    checksum=$(echo "$json" | sha256sum)
    checksum=${checksum:0:64}
    echo "$json" > games/"$checksum.json"
    echo "Game $cnt saved."
    let cnt+=1;
  fi
done