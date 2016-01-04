import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;


public class ObjectRenderer
{
    private static int vertexBufferHandle, colorBufferHandle;
    private static FloatBuffer vertexBuffer, colorBuffer;
        
    public static void loadModel(String fileName)
    {    
        /* Generates handles for the color and vertex buffers. */
        vertexBufferHandle = GL15.glGenBuffers();
        colorBufferHandle = GL15.glGenBuffers();
                    
        /* Generates the color buffer. */
        vertexBuffer = BufferUtils.createFloatBuffer(9);
        vertexBuffer.put(new float[]
                                   {  300.0f  ,  300.0f  ,  0.0f    , 
                                      300.0f  ,  350.0f  ,  0.0f    , 
                                      350.0f  ,  350.0f  ,  0.0f  
                                   }
                       );
        vertexBuffer.flip();
        
        /* Generates the vertex buffer */       
        colorBuffer = BufferUtils.createFloatBuffer(9);
        colorBuffer.put(new float[]
                                  {  1.0f    ,  0.0f    ,  0.0f    , 
                                     0.0f    ,  1.0f    ,  0.0f    , 
                                     0.0f    ,  0.0f    ,  1.0f  
                                  }
                        );
        colorBuffer.flip();
        

        
        /* Associates the handles with buffer data. */
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
    }
    
    public static void render()
    {
        /* Initial OpenGL commands in order to draw VBO . */
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferHandle);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, colorBufferHandle);
        GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 4);
        
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 4);
        

    }

}   
