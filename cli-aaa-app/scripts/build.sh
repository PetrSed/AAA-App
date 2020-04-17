#!/usr/bin/env bash
cd ..

kotlinc ./src -include-runtime -d ./app.jar
