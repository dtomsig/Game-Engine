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
import org.lwjgl.system.MemoryUtil;


public class Client
{   
    private double initialTime, fps;
    private int glfwErrorState, resolutionWidth, resolutionHeight; 
    private IntBuffer physicalWindowWidth = BufferUtils.createIntBuffer(1);
    private IntBuffer physicalWindowHeight = BufferUtils.createIntBuffer(1);
    private long windowHandle;
    private TextRenderer textRenderer;
    private state clientState;
    private static GLFWErrorCallback errorCallback = GLFWErrorCallback.
                                                     createPrint(System.err);
    
    public enum state
    {   
        MAIN_MENU(0), IN_GAME(1), SCORE_SCREEN(2);
        private int value;
        
        private state(int value)
        {
            this.value = value;
        }
        
        int getValue()
        {
            return this.value;
        }
    }
    
    public Client()
    {
        this.resolutionHeight = 640;
        this.resolutionWidth = 480;
    }
    
    public void run()
    {
        try 
        {
            initClient();
            display();
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
        glfwErrorState = GLFW.glfwInit();
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        
        if(glfwErrorState == GL11.GL_FALSE)
            throw new IllegalStateException("Failed to initialize.");
        
        this.windowHandle = GLFW.glfwCreateWindow(this.resolutionHeight, 
                                                  this.resolutionWidth, 
                                                  "Elements Game",
                                                  MemoryUtil.NULL, 
                                                  MemoryUtil.NULL);
        this.clientState = state.MAIN_MENU;
        
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
        
        /* Sets up TextRendering. */
        textRenderer = new TextRenderer();
        textRenderer.load_fonts();        
    }
    
    public void display()
    {   
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        // Main display loop. Calls upon resources to produce the next frame.
        while(GLFW.glfwWindowShouldClose(windowHandle) == GLFW.GLFW_FALSE)
        {
            initialTime = GLFW.glfwGetTime();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GLFW.glfwGetFramebufferSize(windowHandle, physicalWindowWidth,
                                        physicalWindowHeight);
                                        
            physicalWindowWidth.rewind();
            physicalWindowHeight.rewind();
            
            switch(clientState)
            {
                case MAIN_MENU:
                    this.drawSquare();
                    
                case IN_GAME:                    
            }
            
            fps = 1 / (GLFW.glfwGetTime() - initialTime);
            this.renderFPS();
            
            System.out.println("FPS: " + fps);
            GLFW.glfwSwapBuffers(windowHandle);
            GLFW.glfwPollEvents();
        }
    }
    
    public void resize(/*GL11.GLsizei w, GL11.GLsizei height*/)
    {
            
    }
    
    public void drawSquare()
    {        
        GL11.glColor3f(0.1f, 0.1f, 0.6f);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex3f(150, 150, 0.0f);
        GL11.glVertex3f(175, 200, 0.0f);
        GL11.glVertex3f(200, 150, 0.0f);
        GL11.glVertex3f(175, 100, 0.0f); 
        GL11.glEnd();
    }
    
    public void renderFPS()
    {
        textRenderer.print(0, 0, 0, "FPS: " + fps);
        //TextRenderer.renderText("Main Menu:", 50, 50);
       // TextRenderer.renderText("Options:", 50, 60);*/
    }
    
}
