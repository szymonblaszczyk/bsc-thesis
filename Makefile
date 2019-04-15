runone:
	cd ${NAME} && ./gradlew clean build -x test
	docker-compose build ${NAME}
	docker-compose up -d ${NAME}

start:
	docker-compose build
	docker-compose up -d

howdy:
	docker-compose ps

stop:
	docker-compose stop

config:
	cd config-service && ./gradlew clean build -x test

discovery:
	cd discovery-service && ./gradlew clean build -x test

repo:
	cd repository-service && ./gradlew clean build -x test

basic:
	cd basic-client-service && ./gradlew clean build -x test

diff:
	cd diff-client-service && ./gradlew clean build -x test

event:
	cd event-client-service && ./gradlew clean build -x test

gate:
	cd gateway-service && ./gradlew clean build -x test

all: config discovery gate repo basic diff event start howdy

pushconfig:
	cd service-configuration/ && git commit -a -m "Update configs" && git push && cd ..
