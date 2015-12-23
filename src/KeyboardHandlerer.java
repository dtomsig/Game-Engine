import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFW;

public class KeyboardHandlerer extends GLFWKeyCallback
{
    public static boolean keys[] = new boolean[317];    
    
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods)
    {
        keys[key - 32] = action != GLFW.GLFW_RELEASE;
    }
    
    public static boolean isKeyDown(int keycode)
    {
        return keys[keycode - 32];
    }
}
