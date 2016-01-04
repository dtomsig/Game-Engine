import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;  
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL45;
import org.lwjgl.system.MemoryUtil;

public class Client
{   
    private boolean showFps = false;
    private double initialTime, fps, scalingFactorX, scalingFactorY;
    private int glfwErrorState, resolutionHeight, resolutionWidth;
    private IntBuffer physicalWindowWidth = BufferUtils.createIntBuffer(1);
    private IntBuffer physicalWindowHeight = BufferUtils.createIntBuffer(1);
    private long windowHandle;
    private TextRenderer TextRenderer;
    private state clientState;
    private static GLFWErrorCallback errorCallback = GLFWErrorCallback.
                                                     createPrint(System.err);
    private static KeyboardHandlerer keyCallBack;
    
    public enum state
    {   
        MAIN_MENU(0), IN_GAME(1), SCORE_SCREEN(2);
        private int value;
        
        private state(int value)
        {
            value = value;
        }
        
        int getValue()
        {
            return value;
        }
    }
    
    public Client()
    {
        
    }
    
    public void run()
    {
        try 
        {
            initClient();
            loop();
            GLFW.glfwDestroyWindow(windowHandle);
        }
        finally
        {
            GLFW.glfwTerminate();
            errorCallback.release();
        }
        
    }
    
    public void initClient()
    {   
        /* Sets default resolution values. */
        resolutionHeight = 480;
        resolutionWidth = 640;
        physicalWindowHeight.put(0, resolutionHeight);
        physicalWindowWidth.put(0, resolutionWidth);
        
        /* Sets up the errorSTate and sets the window handle. */
        glfwErrorState = GLFW.glfwInit();
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        if(glfwErrorState == GL11.GL_FALSE)
            throw new IllegalStateException("Failed to initialize.");
        
        windowHandle = GLFW.glfwCreateWindow(resolutionWidth, resolutionHeight, 
                                             "Elements Game", MemoryUtil.NULL, 
                                             MemoryUtil.NULL);
                                             
        /* Sets the client state to be the main menu initially. */                                     
        clientState = state.MAIN_MENU;
        
        /* Prepares the client window */
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowHandle);
        GL.setCapabilities(GL.createCapabilities());
        
        /* Sets the initial GL context. */
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glViewport(0, 0, this.resolutionWidth, this.resolutionHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, this.resolutionWidth, 0, this.resolutionHeight, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        /* Sets up text rendering. */
        TextRenderer = new TextRenderer();
        
        /* Sets up object rendering. */
        ObjectRenderer.loadModel("test");
        
        
        /* Sets up keyboard scanning. */
        GLFW.glfwSetKeyCallback(windowHandle, keyCallBack = new KeyboardHandlerer());
    }
    
    public void loop()
    {   
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        // Main game loop. Calls upon resources to produce the next frame.
        while(GLFW.glfwWindowShouldClose(windowHandle) == GLFW.GLFW_FALSE)
        {
            update();
            render();
        }
    }
    
    public void update()
    {
        int tempWidth = physicalWindowWidth.get(0), 
            tempHeight = physicalWindowHeight.get(0);
        
        /* Determines if the window is manually resized and calls to resizes. */
        GLFW.glfwGetFramebufferSize(windowHandle, physicalWindowHeight,
                                    physicalWindowWidth);
        physicalWindowWidth.rewind();
        physicalWindowHeight.rewind();
        if(physicalWindowWidth.get(0) != tempWidth || physicalWindowHeight.get(0) != tempHeight)
            resize();
        if(keyCallBack.isKeyDown(GLFW.GLFW_KEY_S))
            showFps = true;
        else
            showFps = false;
    }
    
    public void render()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        initialTime = GLFW.glfwGetTime();  
                    
        switch(clientState)
        {
            case MAIN_MENU:
                ObjectRenderer.render();
                renderMap();
                
                                    
            case IN_GAME:
        }
        
        fps = 1 / (GLFW.glfwGetTime() - initialTime);
        
        if(showFps)
            renderFPS();
  
        GLFW.glfwSwapBuffers(windowHandle);
        GLFW.glfwPollEvents();
    }
    
    public void renderMap()
    {
        GL11.glColor3f(0.0f, 1.0f, 0.0f);
        GL11.glBegin(GL11.GL_POLYGON);
            GL11.glVertex3f( 450.0f, 0.0f, 0.5f);
            GL11.glVertex3f( 450.0f, 30f, 0.0f);
            GL11.glVertex3f( 480.0f, 30f, 0.0f);
            GL11.glVertex3f( 480.0f, 0.0f, 0.0f);
        GL11.glEnd();
        TextRenderer.print(0, 0, 22, "Monospace", "Hit S to show the fps to the"
                           + " physical window.");
    }
    
    public void resize()
    {
        scalingFactorX = physicalWindowWidth.get(0) / resolutionWidth;
        scalingFactorY = physicalWindowHeight.get(0) / resolutionHeight;
    }
    
    public void changeResolution(int newResolutionWidth, int newResolutionHeight)
    {
        
    }
    
    public void renderFPS()
    {
        TextRenderer.print(0, 15, 22, "Monospace", "FPS: " + fps);
    }
    
}
