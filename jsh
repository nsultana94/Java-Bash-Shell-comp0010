#!/bin/bash

JSH_ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

JSH_JAR="$JSH_ROOT/target/jsh-1.0-SNAPSHOT-jar-with-dependencies.jar"

if [ ! -f "$JSH_JAR" ]; then
    echo JSH is not built && exit 1
fi

java -jar "$JSH_JAR" "$@"