import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class ObjectRenderer
{
    private ArrayList<GraphicsObject> graphicsObjects;
    
    public class GraphicsObject
    {
        public int vertexBufferHandle = GL15.glGenBuffers(),
                   colorBufferHandle  = GL15.glGenBuffers();
        public FloatBuffer vertexBuffer, colorBuffer, textureBuffer;
    }
    
    public ObjectRenderer()
    {
        /* Generates the arraylist that will hold the graphics objects. */
        graphicsObjects = new ArrayList<GraphicsObject>();
    }
        
    public void createObjModel(String fileName, int clientWidth, 
                               int clientHeight)
    {
        BufferedReader inputFile;
        char[] twoCharBuffer = new char[2];
        GraphicsObject newGraphicsObject = new GraphicsObject();
        int vIndex = 0, sizeTextureBuffer = 0, sizeVertexBuffer = 0;
        String lineData;
        String[] parsedData;

        try
        {
            inputFile = new BufferedReader(new FileReader("assets/models/" + 
                                                          fileName));
                        
            /* Measures the sizes of the different buffers in the .obj file. */
            while(inputFile.read(twoCharBuffer, 0, 2) != -1)
            {
                switch(String.valueOf(twoCharBuffer))
                {
                    case "v ":
                        sizeVertexBuffer += 3;
                        break;
                    
                    case "vt":
                        sizeTextureBuffer += 2;
                        break;
                    
                    case "f ":
                        sizeTextureBuffer += 2;
                        break;
                }
                inputFile.readLine();
            }

            /* Allocates space for the buffers based upon number of vertices. */
            newGraphicsObject.vertexBuffer = BufferUtils.createFloatBuffer(sizeVertexBuffer);
            newGraphicsObject.textureBuffer = BufferUtils.createFloatBuffer(sizeTextureBuffer);
            
            /* Reset the input stream in order to restart reading. */
            inputFile = new BufferedReader(new FileReader("assets/models/" + fileName));
            
            /* Stores the data in the  object's buffers from the OBJ file. */            
            while((lineData = inputFile.readLine()) != null)
            {
                if(lineData.charAt(0) == 'v' || lineData.charAt(0) == 'f')
                {
                    parsedData = lineData.split("\\s+");
                    
                    switch(parsedData[0])
                    {
                        case "v":
                            newGraphicsObject.vertexBuffer.put(100 * Float.parseFloat(parsedData[1]))
                                                          .put(100 * Float.parseFloat(parsedData[2]))
                                                          .put(100 * Float.parseFloat(parsedData[3]));
                                                            
                                                            
                            break;
                            
                        case "vt":
                            newGraphicsObject.textureBuffer.put(Float.parseFloat(parsedData[1]))
                                                           .put(Float.parseFloat(parsedData[2]));
                            break;
                            
                        case "f":
                            break;
                        
                    }
                }
            }
           inputFile.close();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        
        /* Flips the buffers for openGL. */
        newGraphicsObject.vertexBuffer.flip();
        
        /* Associates the handles with buffer data. */
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, newGraphicsObject.vertexBufferHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, newGraphicsObject.vertexBuffer, GL15.GL_STATIC_DRAW);
                
        /* Adds the new graphics object to the object list. */
        graphicsObjects.add(newGraphicsObject);
    }
        
        
        
    public void loadTriangle(float x1, float y1, float z1, float x2, float y2,
                             float z2, float x3, float y3, float z3, float c1,
                             float c2, float c3, float c4, float c5, float c6,
                             float c7, float c8, float c9)
    {                        
        GraphicsObject newGraphicsObject = new GraphicsObject();

        
        /* Generates the color buffer. */
        newGraphicsObject.vertexBuffer = BufferUtils.createFloatBuffer(9);
        newGraphicsObject.vertexBuffer.put(new float[]
                                                     {      x1  ,      y1  ,      z1  , 
                                                            x2  ,      y2  ,      z2  , 
                                                            x3  ,      y3  ,      z3  
                                                     }
                       );
        newGraphicsObject.vertexBuffer.flip();
        
        /* Generates the vertex buffer */       
        newGraphicsObject.colorBuffer = BufferUtils.createFloatBuffer(9);
        newGraphicsObject.colorBuffer.put(new float[]
                                                    {      c1  ,      c2  ,      c3  , 
                                                           c4  ,      c5  ,      c6  , 
                                                           c7  ,      c8  ,      c9  
                                                    }
                                         );
        newGraphicsObject.colorBuffer.flip();
    
        /* Associates the handles with buffer data. */
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 
                          newGraphicsObject.vertexBufferHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, 
                          newGraphicsObject.vertexBuffer, GL15.GL_STATIC_DRAW);        
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 
                          newGraphicsObject.colorBufferHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, 
                          newGraphicsObject.colorBuffer, GL15.GL_STATIC_DRAW);
        
        /* Adds the new graphics object to the array list. */
        graphicsObjects.add(newGraphicsObject);
    }
    
    public void renderGraphicsObjects()
    {
        /* Initial OpenGL commands in order to draw VBO objects. */
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        //GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        
        /* Renders the graphics objects to the screen. */
        for(int i = 0; i < graphicsObjects.size(); i++)
        {   
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 
                              graphicsObjects.get(i).vertexBufferHandle);
            GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 
                              graphicsObjects.get(i).vertexBuffer.capacity());
        }
    
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
    }

}   
