#!/usr/bin/env bash

set -e

pushd backend
	mvn clean package
popd

mkdir -p dist

cp backend/target/*-runner.jar dist/ecopoints.jar