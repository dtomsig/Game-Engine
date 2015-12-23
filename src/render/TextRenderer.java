import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

public class TextRenderer
{   
    private static int BITMAP_W = 512;
    private static int BITMAP_H = 512;
    private static float[] scale = {20.0f, 14.0f};
    private static float[] sf = {0, 1, 2, 0, 1, 2};
    
    private boolean integer_align;
    private float rotate_t, translate_t;
    private FloatBuffer xb = MemoryUtil.memAllocFloat(1);
    private FloatBuffer yb = MemoryUtil.memAllocFloat(1);
    private int font_tex;
    private long window;
    private STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
    private STBTTPackedchar.Buffer chardata;
    
    
    public void load_fonts()
    {
        font_tex = GL11.glGenTextures();
        chardata = STBTTPackedchar.mallocBuffer(6 * 128);
             
        try
        {
            ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W *
                                                             BITMAP_H);    
            ByteBuffer ttf = IOUtil.ioResourceToByteBuffer("assets/fonts/Monospace.ttf", 160*1024);            
            STBTTPackContext pc = STBTTPackContext.malloc();
            STBTruetype.stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1,
                                        null);

            for(int i = 0; i < 2; i++)
            {
                chardata.position((i * 3 + 0) * 128 + 32);
                STBTruetype.stbtt_PackSetOversampling(pc, 1, 1);
                STBTruetype.stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, 95, 
                                                chardata);
                chardata.position((i * 3 + 1) * 128 + 32);
                STBTruetype.stbtt_PackSetOversampling(pc, 2, 2);
                STBTruetype.stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, 95,
                                                chardata);
                chardata.position((i * 3 + 2) * 128 + 32);
                STBTruetype.stbtt_PackSetOversampling(pc, 3, 1);
                STBTruetype.stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, 95, 
                                                chardata);
            }
            
            STBTruetype.stbtt_PackEnd(pc);  
            pc.free();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, font_tex);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, BITMAP_W, 
                              BITMAP_H, 0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE,
                              bitmap);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 
                                 GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 
                                 GL11.GL_LINEAR);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public static void drawBoxTC(float x0, float y0, float x1, float y1, 
                                 float s0, float t0, float s1, float t1) 
    {
        GL11.glTexCoord2f(s0, t0);
        GL11.glVertex2f(x0, 640 - y0);
        GL11.glTexCoord2f(s1, t0);
        GL11.glVertex2f(x1, 640 - y0);
        GL11.glTexCoord2f(s1, t1);
        GL11.glVertex2f(x1, 640 - y1);
        GL11.glTexCoord2f(s0, t1);
        GL11.glVertex2f(x0, 640 - y1);
    } 
    
    private void loopmode(float dt) 
    {
        if(dt > 0.25f) 
            dt = 0.25f;
            
        if(dt < 0.01f)
            dt = 0.01f;

        rotate_t += dt;
        translate_t += dt;
    }
    
    public void print(float x, float y, int font, String text)
    {   
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        xb.put(0, x);
        yb.put(0, 640 - y);
        chardata.position(font * 128);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font_tex);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor3f(0f, 0f, 0f);
        
        for(int i = 0; i < text.length(); i++) 
        {
            STBTruetype.stbtt_GetPackedQuad(chardata, BITMAP_W, BITMAP_H, 
                                            text.charAt(i), 
                                xb, yb, q, font == 0 && integer_align ? 1 : 0);
            drawBoxTC(q.x0(), q.y0(), q.x1(), q.y1(), q.s0(), q.t0(), q.s1(), 
                    q.t1());
        }
        
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);

    }
}


