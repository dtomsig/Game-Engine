import org.lwjgl.opengl.GL11;

public class ObjectRenderer
{
    public static void drawTriangle()
    {
        float vertices[] = 
        {
            -0.5f, -0.5f, 0.0f,
             0.5f, -0.5f, 0.0f,
             0.0f,  0.5f, 0.0f
        };    
        
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
