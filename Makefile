buildone:
	cd ${NAME} && ./gradlew clean build -x test

startone:
	docker-compose build ${NAME}
	docker-compose up -d ${NAME}

start:
	docker-compose build
	docker-compose up -d

howdy:
	docker-compose ps

stop:
	docker-compose stop

config: NAME=config-service
config: buildone

discovery: NAME=discovery-service
discovery: buildone

repo: NAME=repository-service
repo: buildone

all: discovery repo start howdy