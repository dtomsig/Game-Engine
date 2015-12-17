FILES = src/Client.java src/Unit.java src/ClientLoader.java src/TextRenderer.java
MANIFEST_CLASSPATH = build/\n  lib/lwjgl/jar/lwjgl.jar\n  lib/build/IOUtil.class\n

all: IOUtil jar

IOUtil:
	javac -d lib/ioutil/class -cp "lib/lwjgl/jar/lwjgl.jar" lib/ioutil/src/IOUtil.java

jar:
	javac -d build -cp "lib/ioutil/class/:lib/lwjgl/jar/lwjgl.jar" ${FILES}
	touch manifest.txt
	printf 'Main-Class: ClientLoader\nClass-Path: ${MANIFEST_CLASSPATH}' > manifest.txt
	jar -cfm run.jar manifest.txt
	rm -f manifest.txt
	java -jar run.jar
	
clean:
	rm -f *.so *.jar *.log
	
