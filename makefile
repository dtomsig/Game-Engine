FILES = src/Client.java src/Unit.java src/ClientLoader.java src/TextDisplay.java
MANIFEST_CLASSPATH = build/\n  lib/jar/lwjgl.jar\n  lib/jar/slick-util.jar\n


all: jar

jar:
	javac -d build -cp "lib/jar/lwjgl.jar:lib/jar/slick-util.jar" ${FILES}
	touch manifest.txt
	printf 'Main-Class: ClientLoader\nClass-Path: ${MANIFEST_CLASSPATH}' > manifest.txt
	jar -cfm run.jar manifest.txt
	rm -f manifest.txt
	java -jar run.jar
	
clean:
	rm -f *.so *.jar *.log
	
