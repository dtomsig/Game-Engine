all:
	javac -d build -cp "lib/eventbus.jar:lib/gluegen-rt.jar:lib/jbullet.jar:lib/jglfont-core.jar:lib/jinput.jar:lib/jME3-blender.jar:lib/jME3-core.jar:lib/jME3-desktop.jar:lib/jME3-effects.jar:lib/jbullet.jar:lib/jME3-jogg.jar:lib/jME3-lwjgl.jar:lib/jME3-lwjgl-natives.jar:lib/jME3-networking.jar:lib/jME3-niftygui.jar:lib/jME3-openal-soft-natives-android.jar:lib/jME3-plugins.jar:lib/jME3-terrain.jar:lib/jME3-testdata.jar:lib/joal.jar:lib/j-ogg-oggd.jar:lib/j-ogg-vorbisd.jar:lib/jogl-all.jar:lib/lwjgl.jar:lib/nifty.jar:lib/nifty-default-controls.jar:lib/nifty-examples.jar:lib/nifty-style-black.jar:lib/stack-alloc.jar:lib/vecmath.jar:lib/xmlpull-xpp3.jar" src/Client.java src/Unit.java	
	java -cp "lib/eventbus.jar:lib/gluegen-rt.jar:lib/jbullet.jar:lib/jglfont-core.jar:lib/jinput.jar:lib/jME3-blender.jar:lib/jME3-core.jar:lib/jME3-desktop.jar:lib/jME3-effects.jar:lib/jbullet.jar:lib/jME3-jogg.jar:lib/jME3-lwjgl.jar:lib/jME3-lwjgl-natives.jar:lib/jME3-networking.jar:lib/jME3-niftygui.jar:lib/jME3-openal-soft-natives-android.jar:lib/jME3-plugins.jar:lib/jME3-terrain.jar:lib/jME3-testdata.jar:lib/joal.jar:lib/j-ogg-oggd.jar:lib/j-ogg-vorbisd.jar:lib/jogl-all.jar:lib/lwjgl.jar:lib/nifty.jar:lib/nifty-default-controls.jar:lib/nifty-examples.jar:lib/nifty-style-black.jar:lib/stack-alloc.jar:lib/vecmath.jar:lib/xmlpull-xpp3.jar:src/.:build/." Client Unit
