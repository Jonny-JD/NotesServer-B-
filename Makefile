APP_NAME=notes-backend
export DOCKERHUB_USERNAME
export DOCKERHUB_TOKEN
JAR_FILE := $(firstword $(filter-out %-plain.jar, $(wildcard build/libs/*.jar)))

.PHONY: build jar docker-run clean login push

ifdef ComSpec
  GRADLEW = gradlew.bat
else
  GRADLEW = ./gradlew
endif

jar:
	${GRADLEW} clean build

build: jar
		docker build -t "$$DOCKERHUB_USERNAME"/$(APP_NAME):latest --build-arg JAR_FILE=$(JAR_FILE) .

docker-run:
	docker run -p 8080:8080 --name $(APP_NAME)-container "$$DOCKERHUB_USERNAME"/$(APP_NAME):latest

clean:
	docker rm -f $(APP_NAME)-container || true
	docker rmi -f "$$DOCKERHUB_USERNAME"/$(APP_NAME):latest || true
	${GRADLEW} clean

login:
	docker login -u "$$DOCKERHUB_USERNAME" --password "$$DOCKERHUB_TOKEN"

push: login
	docker push "$$DOCKERHUB_USERNAME"/$(APP_NAME):latest