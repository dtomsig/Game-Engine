import java.io.IOException;
import java.nio.IntBuffer;
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
  /*  public enum state
    {   
        MAIN_MENU(0), IN_GAME(1), SCORE_SCREEN(2);
        private int value;
        
        private state(int value)
        {
            this.value = value;
        }
        
        int getValue()
        {

        }
    }*/
    
    public Client()
    {
    }
    
    public void initClient()
    {   
        glfwSetErrorCallback(errorCallback);
        windowHandle = glfwCreateWindow(640, 480, "Simple example", NULL, NULL);
        glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
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
