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
    private boolean show_fps = false;
    private double initial_time, fps, scaling_factor_x, scaling_factor_y;
    private int glfw_error_state, resolution_height, resolution_width;
    private IntBuffer physical_window_height = BufferUtils.createIntBuffer(1);
    private IntBuffer physical_window_width = BufferUtils.createIntBuffer(1);
    private long window_handle;
    private ObjectRenderer ObjectRenderer;
    private TextRenderer TextRenderer;
    private state client_state;
    private static GLFWErrorCallback error_call_back = GLFWErrorCallback.
                                                     createPrint(System.err);
    private static KeyboardHandlerer key_call_back;
    
    public enum state
    {   
        MAIN_MENU(0), IN_GAME(1), SCORE_SCREEN(2);
        private int value;
        
        private state(int value)
        {
            value = value;
        }
        
        int get_value()
        {
            return value;
        }
    }
    
    public Client()
    {
        
    }
    
    public void change_resolution(int new_resolution_width, 
                                  int new_resolution_height)
    {
        
    }
    
    public void init_client()
    {   
        /* Sets default resolution values. */
        resolution_height = 480;
        resolution_width = 640;
        physical_window_height.put(0, resolution_height);
        physical_window_width.put(0, resolution_width);
        
        /*
        ** Sets up the error_state and sets the window handle to the current
        ** window.
        */
        glfw_error_state = GLFW.glfwInit();
        GLFW.glfwSetErrorCallback(error_call_back = 
                                  GLFWErrorCallback.createPrint(System.err));
        if(glfw_error_state == GL11.GL_FALSE)
            throw new IllegalStateException("Failed to initialize.");
        
        window_handle = GLFW.glfwCreateWindow(resolution_width, 
                                              resolution_height, 
                                              "Elements Game", MemoryUtil.NULL, 
                                              MemoryUtil.NULL);
                                             
        /* Sets the client state to be the main menu initially. */                                     
        client_state = state.MAIN_MENU;
        
        /* Prepares the client window */
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwMakeContextCurrent(window_handle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window_handle);
        GL.setCapabilities(GL.createCapabilities());
        
        /* Sets the initial GL context. */
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glViewport(0, 0, this.resolution_width, this.resolution_height);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, this.resolution_width, 0, this.resolution_height, -1, 
                     1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        
        /* Sets up text rendering. */
        TextRenderer = new TextRenderer();
        
        /* Sets up object rendering. */
        ObjectRenderer = new ObjectRenderer();
        ObjectRenderer.createObjModel("box.obj", this.resolution_width, 
                                      this.resolution_height);        
        
        /* Sets up keyboard scanning. */
        GLFW.glfwSetKeyCallback(window_handle, key_call_back = 
                                new KeyboardHandlerer());
    }
    
    public void loop()
    {   
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        // Main game loop. Calls upon resources to produce the next frame.
        while(GLFW.glfwWindowShouldClose(window_handle) == GLFW.GLFW_FALSE)
        {
            update();
            render();
        }
    }
    
    public void update()
    {
        int temp_width = physical_window_width.get(0), 
            temp_height = physical_window_width.get(0);
        
        /* Determines if the window is manually resized and calls to resizes. */
        GLFW.glfwGetFramebufferSize(window_handle, physical_window_height,
                                    physical_window_width);
        physical_window_width.rewind();
        physical_window_height.rewind();
        if(physical_window_width.get(0) != temp_width || 
           physical_window_height.get(0) != temp_height)
            resize();
        if(key_call_back.isKeyDown(GLFW.GLFW_KEY_S))
            show_fps = true;
        else
            show_fps = false;
    }
    
    public void render()
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        initial_time = GLFW.glfwGetTime();  
                    
        switch(client_state)
        {
            case MAIN_MENU:
                render_map();
                break;
                
                                    
            case IN_GAME:
                break;
        }
        
        //ObjectRenderer.renderGraphicsObjects();
        fps = 1 / (GLFW.glfwGetTime() - initial_time);
        
        if(show_fps)
            render_fps();
  
        GLFW.glfwSwapBuffers(window_handle);
        GLFW.glfwPollEvents();
    }
    
    public void render_fps()
    {
        TextRenderer.print(0, 0, 18, "DejaVuSansMono", "FPS: " + fps);
    }
    
    public void render_map()
    {
        GL11.glColor3f(0.0f, 1.0f, 0.0f);
        TextRenderer.print(300, 300, 12, "DejaVuSansMono", 
                           "Press 's' to display FPS.");
    }
   
    public void resize() 
    {
        scaling_factor_x = physical_window_width.get(0) / resolution_width;
        scaling_factor_x = physical_window_height.get(0) / resolution_height;
    }    
    
    public void run()
    {
        try 
        {
            init_client();
            loop();
            GLFW.glfwDestroyWindow(window_handle);
        }
        finally
        {
            GLFW.glfwTerminate();
            error_call_back.release();
        }
        
    }
}
