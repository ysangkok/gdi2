#!/bin/sh -ex
javac -cp .:/usr/share/java/junit4.jar *.java
#java -cp .:/usr/share/java/junit4.jar org.junit.runner.JUnitCore PivotPartitioningTest
#java -cp /usr/share/java/junit4.jar:. SingleJUnitTestRunner PivotPartitioningTest#testD120
java -cp /usr/share/java/junit4.jar:. SingleJUnitTestRunner PivotPartitioningTest#testCustomApril
