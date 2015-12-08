all:
	javac -d build src/Client.java -Djava.library.path="lwjgl lib/native/.*" -cp "lwjgl lib/jar/lwjgl.jar" src/Client.java src/Unit.java	
	touch manifest.txt
	printf 'Main-Class: Client\nClass-Path: lwjgl lib/jar/lwjgl.jarbuild/\n  \n' > manifest.txt
	jar -cfm run.jar manifest.txt
	rm -f manifest.txt
	java -jar run.jar
	
clean:
	rm -f *.so *.jar 
