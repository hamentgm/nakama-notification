#!/bin/bash

rm -rf modules

#packr

docker-compose down

docker run --rm -v $GOPATH/mod_for_nakama:/go -v /$PWD:/usr/src/multiplay -w="/usr/src/multiplay" heroiclabs/nakama-pluginbuilder:2.12.0 build --buildmode=plugin -trimpath -o ./modules/multiplay.so

docker-compose up
