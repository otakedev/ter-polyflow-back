#!/bin/bash
PARAMS=""
NARG=0
MARG=dev
while (("$#")); do
  case "$1" in
  -n | --no-compile)
    NARG=1
    break
    ;;
  -m | --mode)
    MARG=$2
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
    if [ -f settings.xml ]; then
        if [[ $MARG == "dev" ]]; then
        	mvn clean install -s settings.xml -DskipTests -Pdev
		else
			mvn clean install -s settings.xml -DskipTests -Pprod
		fi
    else
		if [[ $MARG == "dev" ]]; then
        	mvn clean install -DskipTests -Pdev
		else
        	mvn clean install -DskipTests -Pprod
		fi
    fi
fi

cd webservices
if [[ $MARG == "dev" ]]; then
	mvnDebug spring-boot:run -Ptest -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
else
	mvnDebug spring-boot:run -Pprod -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
fi