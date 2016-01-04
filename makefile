FILES = src/Client.java \
        src/game/Unit.java \
        src/ClientLoader.java \
        src/render/TextRenderer.java \
        src/KeyboardHandlerer.java \
        src/game/Game.java \
        src/render/ObjectRenderer.java
MANIFEST_CLASSPATH = build/\n  lib/lwjgl/jar/lwjgl.jar\n  lib/ioutil/class/\n

all: gameFiles serverFiles

gameFiles: IOUtil jar

serverFiles:

ttTest:
	javac -d build -cp "lib/ioutil/class/:lib/lwjgl/jar/lwjgl.jar" src/TruetypeOversample.java
	java -cp "build/:lib/ioutil/class/:lib/lwjgl/jar/lwjgl.jar" TruetypeOversample
	
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
	rm -f *.so *.jar
	rm -rf ./lib/ioutil/class/*
	rm -rf ./build/*
	
