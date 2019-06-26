#!/usr/bin/env bash

finder(){
    for target in "$1"/*
    do
    if [ -d "$target" ]
    then
        finder "$target"

    elif [ ${target: -4} == ".sol" ]
    then

        dirname=$(dirname $target)
        filename=$(basename "${target%.*}")

        solc --bin --abi --optimize --overwrite --allow-paths . $target -o ./abi/ > /dev/null
        echo "[$target] generating"
        java -jar ../../../../console/build/libs/console-0.9.4.jar solidity generate \
            -b "./abi/$filename".bin \
            -a "./abi/$filename".abi \
            -p com.klaytn.caver.generated \
            -o ../java/ > /dev/null
        echo "Complete"
    fi
    done
}

finder "klaytn-intergration-tests/INT-SOL/contracts"
