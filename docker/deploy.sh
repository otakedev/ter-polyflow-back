#!/bin/bash
path=$(cd "$(dirname "${BASH_SOURCE[0]}")"; pwd -P)
cd $path

echo "building system..."
cd ../system
chmod +x mvnw
./mvnw clean package -DskipTests -Pdocker -q
cd $path
echo "system built"

cp ../system/webservices/target/polyflow-backend.war ./system

# build docker images
docker-compose build

# delete generated files
rm -rf system/polyflow-backend.war

echo "docker env down"
docker-compose -f docker-compose.yml down
echo "docker env up"
docker-compose -f docker-compose.yml up -d
