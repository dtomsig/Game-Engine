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
        initClient();
    }
    
    public void initClient()
    {   
        int glfwErrorState = glfwInit();
        
        glfwSetErrorCallback(errorCallback);
        
        if(glfwErrorState == GL_FALSE)
            throw new IllegalStateException("Failed to initialize.");
        
        
        this.windowHandle = glfwCreateWindow(640, 480, "Elements Game", NULL, NULL);
        glfwMakeContextCurrent(windowHandle);
        glfwSwapInterval(1);
        glfwShowWindow(windowHandle);
    }
    
    public void changeTitle()
    {
        
    }
    
    public void update()
    {    

    }
    
    public void render()
    {
        
    }    
}
