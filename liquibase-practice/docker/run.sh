docker-compose down
docker kill $(docker ps | grep 'liquibase-practice' | awk {'print $1'})
docker rmi $(docker images | grep 'liquibase-practice' | awk {'print $3'})

cd .. && ./gradlew clean build docker
cd docker && docker-compose up