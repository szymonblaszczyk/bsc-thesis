buildone:
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

discovery: NAME=discovery-service
discovery: buildone

repo: NAME=repository-service
repo: buildone

all: discovery repo start howdy