@echo off
start "node 1" java -jar NetNode.jar -i 1 -f nodelist
start "node 2" java -jar NetNode.jar -i 2 -f nodelist
start "node 3" java -jar NetNode.jar -i 3 -f nodelist
start "node 4" java -jar NetNode.jar -i 4 -f nodelist
start "node 5" java -jar NetNode.jar -i 5 -f nodelist