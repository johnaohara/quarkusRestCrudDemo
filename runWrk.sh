#!/usr/bin/env bash

for USERS in 1 5 10 15 20 25
do
  echo "Runnning with $USERS users"
	for run in {1..2}
   do
		wrk --threads=$USERS --connections=$USERS -d60s http://benchserver4G1:8080/fruits;
	done
done
