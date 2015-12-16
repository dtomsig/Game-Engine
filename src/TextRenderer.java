import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.libffi.Closure;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryUtil.*;

public class TextRenderer
{   
    private static int BITMAP_W = 512;
    private static int BITMAP_H = 512;
    private static float[] scale = {24.0f, 14.0f};
    private static float[] sf = {0, 1, 2, 0, 1, 2};
    
    private Closure debugProc;
    private FloatBuffer xb = memAllocFloat(1);
    private FloatBuffer yb = memAllocFloat(1);
    private GLFWErrorCallback errorfun;
    private GLFWFramebufferSizeCallback framebufferSizefun;
    private GLFWKeyCallback keyfun;
    private GLFWWindowSizeCallback windowSizefun;
    private int font_tex;
    private long window;
    private STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
    private STBTTPackedchar.Buffer chardata;
    
    
    private void load_fonts()
    {
        font_tex = GL11.glGenTextures();
        chardata = STBTTPackedchar.mallocBuffer(6 * 128); // Could have an error
        STBTTPackContext pc = STBTTPackContext.malloc();    
    
        try
        {
            ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W *
                                                             BITMAP_H);    
            ByteBuffer ttf = ioResourceToByteBuffer("assets/Fonts/PaddingtonSc.ttf", 160*1024);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    
        
    }
    
    private static void drawBoxTC(float x0, float y0, float x1, float y1, float s0, float t0, float s1, float t1) 
    {
        GL11.glTexCoord2f(s0, t0);
        GL11.glVertex2f(x0, y0);
        GL11.glTexCoord2f(s1, t0);
        GL11.glVertex2f(x1, y0);
        GL11.glTexCoord2f(s1, t1);
        GL11.glVertex2f(x1, y1);
        GL11.glTexCoord2f(s0, t1);
        GL11.glVertex2f(x0, y1);
    } 

}


