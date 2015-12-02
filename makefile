all:
	javac -d build src/Client.java -cp "lib/eventbus.jar:lib/gluegen-rt.jar:lib/jbullet.jar:lib/jglfont-core.jar:lib/jinput.jar:lib/jME3-blender.jar:lib/jME3-core.jar:lib/jME3-desktop.jar:lib/jME3-effects.jar:lib/jbullet.jar:lib/jME3-jogg.jar:lib/jME3-lwjgl.jar:lib/jME3-lwjgl-natives.jar:lib/jME3-networking.jar:lib/jME3-niftygui.jar:lib/jME3-openal-soft-natives-android.jar:lib/jME3-plugins.jar:lib/jME3-terrain.jar:lib/jME3-testdata.jar:lib/joal.jar:lib/j-ogg-oggd.jar:lib/j-ogg-vorbisd.jar:lib/jogl-all.jar:lib/lwjgl.jar:lib/nifty.jar:lib/nifty-default-controls.jar:lib/nifty-examples.jar:lib/nifty-style-black.jar:lib/stack-alloc.jar:lib/vecmath.jar:lib/xmlpull-xpp3.jar" src/Client.java src/Unit.java	
	touch manifest.txt
	printf 'Main-Class: Client\nClass-Path: build/\n  lib/eventbus.jar\n  lib/gluegen-rt.jar\n  lib/jbullet.jar\n  lib/jglfont-core.jar\n  lib/jinput.jar\n  lib/jME3-blender.jar\n  lib/jME3-core.jar\n  lib/jME3-desktop.jar\n  lib/jME3-effects.jar\n  lib/jbullet.jar\n  lib/jME3-jogg.jar\n  lib/jME3-lwjgl.jar\n  lib/jME3-lwjgl-natives.jar\n  lib/jME3-networking.jar\n  lib/jME3-niftygui.jar\n  lib/jME3-openal-soft-natives-android.jar\n  lib/jME3-plugins.jar\n  lib/jME3-terrain.jar\n  lib/jME3-testdata.jar\n  lib/joal.jar\n  lib/j-ogg-oggd.jar\n  lib/j-ogg-vorbisd.jar\n  lib/jogl-all.jar\n  lib/lwjgl.jar\n  lib/nifty.jar\n  lib/nifty-default-controls.jar\n  lib/nifty-examples.jar\n  lib/nifty-style-black.jar\n  lib/stack-alloc.jar\n  lib/vecmath.jar\n  lib/xmlpull-xpp3.jar\n' > manifest.txt
	jar -cfm run.jar manifest.txt
	rm -f manifest.txt
	java -jar run.jar
	
clean:
	rm -f *.so *.jar 
