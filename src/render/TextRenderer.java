import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
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
    private FloatBuffer xb = MemoryUtil.memAllocFloat(1);
    private FloatBuffer yb = MemoryUtil.memAllocFloat(1);
    private STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
    private HashMap<String, Font> loadedFonts = new HashMap<String, Font>();
    
    private class Font
    {
        public String fontName;
        public STBTTPackedchar.Buffer chardata;
        public int fontSize;
        public int font_tex;
    };

    public void loadFont(String fontName, int fontSize)
    {
        /* Leaves the function if the font is already loaded. */
        if(loadedFonts.containsKey(fontName + fontSize))
            return;
    
        /* Verifies that the file exists, throws an exception if it does not. */
        ByteBuffer ttf;
      
        try
        {
            ttf = IOUtil.ioResourceToByteBuffer("assets/fonts/" + fontName + ".ttf", 160*1024);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        /* Stores the Font data and the OpenGL Font texture data. */
        ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H); 
        int font_tex = GL11.glGenTextures();
        STBTTPackedchar.Buffer chardata = STBTTPackedchar.mallocBuffer(3 * 128);
         
        STBTTPackContext pc = STBTTPackContext.malloc();
        STBTruetype.stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, null);
        
        chardata.position(32);
        STBTruetype.stbtt_PackSetOversampling(pc, 2, 2);
        STBTruetype.stbtt_PackFontRange(pc, ttf, 0, fontSize, 32, 95, chardata);

        pc.free();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font_tex);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, BITMAP_W,
                          BITMAP_H, 0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE,
                          bitmap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 
                             GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 
                             GL11.GL_LINEAR);
                             
        /* Stores the data in the Font, and stores the Font in the map.*/
        Font newFont = new Font();
        newFont.fontName = fontName;
        newFont.fontSize = fontSize;
        newFont.chardata = chardata;
        newFont.font_tex = font_tex;
        loadedFonts.put(fontName + fontSize, newFont);
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
    
    public void print(float x, float y, int fontSize, String fontName, String text)
    {   
        /* Checks to see if the font is loaded. If not, loads the font. */
        if(!loadedFonts.containsKey(fontName + fontSize))
            loadFont(fontName, fontSize);
            
        /* Gets the data from the stored Font. */
        STBTTPackedchar.Buffer chardata = loadedFonts.get(fontName + fontSize).chardata;
        int font_tex = loadedFonts.get(fontName + fontSize).font_tex;
        chardata.position(0);
        
        /* Draws the text to the screen. */
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        xb.put(0, x);
        yb.put(0, 640 - y);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font_tex);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor3f(0f, 0f, 0f);
        
        for(int i = 0; i < text.length(); i++) 
        {
            STBTruetype.stbtt_GetPackedQuad(chardata, BITMAP_W, BITMAP_H, 
                                            text.charAt(i), xb, yb, q, 0);
            drawBoxTC(q.x0(), q.y0(), q.x1(), q.y1(), q.s0(), q.t0(), q.s1(), 
                    q.t1());
        }
        
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
    }
}


