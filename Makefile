APP_NAME=notes-backend
export DOCKERHUB_USERNAME
export DOCKERHUB_TOKEN
IMAGE_NAME=$(DOCKERHUB_USERNAME)/$(APP_NAME):latest
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
		docker build -t $(IMAGE_NAME) --build-arg JAR_FILE=$(JAR_FILE) .

docker-run:
	docker run -p 8080:8080 --name $(APP_NAME)-container $(IMAGE_NAME)

clean:
	docker rm -f $(APP_NAME)-container || true
	docker rmi -f $(IMAGE_NAME) || true
	${GRADLEW} clean

push:
	docker push $(IMAGE_NAME)