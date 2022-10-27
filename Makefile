#!/bin/bash
.PHONY: all build help deploy destroy diff bootstrap

# Configuration
export DEFAULT_ACCOUNT_ID := $(shell aws sts get-caller-identity --output text --query 'Account')
export DEFAULT_REGION_ID := $(shell aws configure get region)
## AWS_ACCESS_KEY_ID (or AWS_ACCESS_KEY) and 
## AWS_SECRET_KEY (or AWS_SECRET_ACCESS_KEY

ENV_TARGET=local
all: bf

help: ## Display help
	@grep -E '^[a-zA-Z0-9_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

bf: ## Build feature flags (eg. make bf ENVTARGET=dev)
	@cd featureflags && docker build --build-arg=ENV_TARGET=$(ENV_TARGET) -t featureflags . 

rf: ## Run feature flags (eg. make rf ENVTARGET=dev)
	echo $(ASSUME_ROLE_ARN)
	docker run --env AWS_ACCESS_KEY_ID=$(AWS_ACCESS_KEY_ID) --env AWS_SECRET_ACCESS_KEY=$(AWS_SECRET_ACCESS_KEY) featureflags 