#!/bin/bash
PARAMS=""
NARG=0
while (("$#")); do
  case "$1" in
  -n | --no-compile)
    NARG=1
    break
    ;;
  --) # end argument parsing
    shift
    break
    ;;
  -* | --*=) # unsupported flags
    echo "Error: Unsupported flag $1" >&2
    exit 1
    ;;
  *) # preserve positional arguments
    PARAMS="$PARAMS $1"
    shift
    ;;
  esac
done
path=$(cd "$(dirname "${BASH_SOURCE[0]}")"; pwd -P)
cd $path
if [[ $NARG == 0 ]]; then
    if [ -f settings.xml ]; then
        mvn clean install -s settings.xml -DskipTests
    else
        mvn clean install -DskipTests
    fi
fi

cd webservices
mvn spring-boot:run