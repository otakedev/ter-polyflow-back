#!/bin/bash
PARAMS=""
NARG=0
MARG=dev
PARG=""
while (("$#")); do
  case "$1" in
  -n | --no-compile)
    NARG=1
    shift
    ;;
  -m | --mode)
    MARG=$2
    shift 2
    ;;
  -p | --project)
    PARG=$2
    shift 2
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
    if [[ -z $PARG ]]; then
      if [ -f settings.xml ]; then
          mvn clean install -s settings.xml -DskipTests
      else
          mvn clean install -DskipTests
      fi
    else
      if [[ $PARG == "wf" ]]; then
        if [ -f settings.xml ]; then
            mvn clean install -s ../settings.xml -DskipTests
        else
            mvn clean install -DskipTests
        fi
      elif [[ $PARG == "wfm" ]]; then
        if [ -f settings.xml ]; then
            mvn clean install -s ../settings.xml -DskipTests
        else
            mvn clean install -DskipTests
        fi
      elif [[ $PARG == "web" ]]; then
        if [ -f settings.xml ]; then
            mvn clean install -s ../settings.xml -DskipTests
        else
            mvn clean install -DskipTests
        fi
      else
        echo $PARG "doesn't exist"
      fi
    fi
fi

cd webservices
if [[ $MARG == "dev" ]]; then
	mvn spring-boot:run -Ptest
else
	mvn spring-boot:run -Pprod
fi