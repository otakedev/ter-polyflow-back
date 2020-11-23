PARAMS=""
SARG=0
while (("$#")); do
    case "$1" in
    -s | --skip)
        SARG=1
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

# Create a .war file and store it in back/ directory
if [[ $SARG == 0 ]]; then
    cd ../system
    mvn clean package -DskipTests -Pdocker
    cd $path
fi

cp ../system/webservices/target/polyflow-backend.war ./system

# build docker images 
docker-compose build

# delete generated files
rm -rf system/polyflow-backend.war

# run docker 
docker-compose up