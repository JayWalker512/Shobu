#!/usr/bin/env bash
mkdir "games"
cnt=1;
total=1;
while [ 1 ]; do
  game=$(java -jar Engine/build/libs/Engine.jar "java -jar AIStarterKit/build/libs/AIStarterKit.jar" "java -jar AIStarterKit/build/libs/AIStarterKit.jar")
  echo "$game" | json_pp | grep "winner"
  # json=$(echo "$game" | ../Shobu_Analysis/target/release/turn_limiter)
  json=$(echo "$game" | json_pp)
  if [[ "$json" != "" ]]; then
    checksum=$(echo "$json" | sha256sum)
    checksum=${checksum:0:64}
    echo "$json" > games/"$checksum.json"
    echo "Game $cnt / $total saved."
    let cnt+=1;
  fi
  let total+=1;
done