path=$(cd "$(dirname "${BASH_SOURCE[0]}")"; pwd -P)
cd $path

if [ -f settings.xml ]; then
    mvn clean install -s settings.xml -DskipTests
else
    mvn clean install -DskipTests
fi

cd webservices
mvn spring-boot:run