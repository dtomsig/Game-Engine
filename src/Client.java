import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Client
{    
    private long windowHandle;
    private static GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
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
            return value;
        }
    }
    
    public Client()
    {
        this.run();
    }
    
    public void run()
    {
        try 
        {
            initClient();
            update();
            glfwDestroyWindow(windowHandle);
        }
        finally
        {
            glfwTerminate();
            errorCallback.release();
        }
        
    }
    
    public void initClient()
    {   
        int glfwErrorState = glfwInit();
        
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        
        if(glfwErrorState == GL_FALSE)
            throw new IllegalStateException("Failed to initialize.");
        
        
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        
        this.windowHandle = glfwCreateWindow(640, 480, "Elements Game", NULL, NULL);
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(windowHandle);
    }
    
    public void update()
    {    
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        while(glfwWindowShouldClose(windowHandle) == GLFW_FALSE)
        {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }
    }
    
    
    public void changeTitle()
    {
        
    }
    public void render()
    {
        
    }    
}
