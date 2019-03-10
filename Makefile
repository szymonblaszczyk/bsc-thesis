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

repo: NAME=repository-service
repo: buildone

all: repo howdy