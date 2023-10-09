#!/bin/bash

#copy AS0.java to AS1..AS10, change all the ocurrencies of AS0 to AS1..AS10
for i in {1..9}
do
    cp AS0.java AS$i.java
    gsed -i 's/AS0/AS'$i'/g' AS$i.java
done
