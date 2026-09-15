@echo off
javac -d out\classes src\main\java\org\example\*.java
javadoc -d out\docs src\main\java\org\example\*.java
jar --create --file=out\app.jar --main-class=org.example.Main -C out\classes .
java -jar out\app.jar