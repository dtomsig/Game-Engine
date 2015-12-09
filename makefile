all:
	javac -d build -cp "lib/jar/lwjgl.jar" src/Client.java src/Unit.java src/ClientLoader.java
	touch manifest.txt
	printf 'Main-Class: ClientLoader\nClass-Path: build/\n  lib/jar/lwjgl.jar\n' > manifest.txt
	jar -cfm run.jar manifest.txt
	rm -f manifest.txt
	java -jar run.jar
	
clean:
	rm -f *.so *.jar *.log
	
