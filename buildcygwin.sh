#!/bin/sh -ex
CP=".;c:\\javatools\\junit\\junit-4.11.jar;c:\\javatools\\junit\\hamcrest-core-1.3.jar"
javac -cp "$CP" *.java
#java -cp .:/usr/share/java/junit4.jar org.junit.runner.JUnitCore PivotPartitioningTest
java -cp "$CP" SingleJUnitTestRunner PivotPartitioningTest#testD120
