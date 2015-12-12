import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Client
{   
    private int glfwErrorState; 
    private long windowHandle;
    private static GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
    private state clientState;
    
    
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
    }
    
    public void run()
    {
        try 
        {
            initClient();
            update();
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
        
        this.windowHandle = GLFW.glfwCreateWindow(640, 480, "Elements Game", 
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
        
        GL11.glViewport(0, 0, 640, 480);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0, 10.0, 0.0, 10.0, -1.0, 1.0);
    }
    
    public void update()
    {   
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        // Main display loop.
        while(GLFW.glfwWindowShouldClose(windowHandle) == GLFW.GLFW_FALSE)
        {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            
            switch(clientState)
            {
                case MAIN_MENU:
                    this.drawSquare();
                    
                case IN_GAME:
                    
            }
            
            GLFW.glfwSwapBuffers(windowHandle);
            GLFW.glfwPollEvents();
        }
    }
    
    public void drawSquare()
    {        
        GL11.glColor3f(0.8f, 0.3f, 0.6f);
        GL11.glBegin(GL11.GL_POLYGON);
        GL11.glVertex3f(2.0f, 4.0f, 0.0f);
        GL11.glVertex3f(8.0f, 4.0f, 0.0f);
        GL11.glVertex3f(8.0f, 6.0f, 0.0f);
        GL11.glVertex3f(2.0f, 6.0f, 0.0f); 
        GL11.glEnd();
        //GL11.glFLush();
    }
    
    public void changeTitle()
    {
        
    }
    public void render()
    {
        
    }    
}
