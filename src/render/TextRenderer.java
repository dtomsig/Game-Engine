import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import javax.imageio.ImageIO;

public class TextRenderer
{       
    private static int BITMAP_W = 512;
    private static int BITMAP_H = 512;
    private HashMap<String, UserFont> loadedUserFonts = new HashMap<String, 
                                                                    UserFont>();
    
    public class UserFont
    {
        public int fontTextureID;
        public BufferedImage textureData;
        public HashMap<Character, CharCoordinate> characterPositionData 
                                                  = new HashMap<Character, 
                                                                CharCoordinate>
                                                                             ();
    }
    
    public class CharCoordinate
    {
        public int xPosition;
        public int yPosition;
        public int width;
        public int height;
    }
    
    public void loadFont(String fontName, int fontSize)
    {   
        /* Leaves the function if the font is already loaded. */
        if(loadedUserFonts.containsKey(fontName + fontSize))
            return;
        
        UserFont newUserFont = new UserFont();
        
        /* Verifies that the file exists, throws an exception if it does not. */
        Font selectedAwtFont;
        try
        {
            selectedAwtFont = Font.createFont(Font.TRUETYPE_FONT, 
                                              new FileInputStream("assets/font" 
                                                                  + "s/" + 
                                                                  fontName + 
                                                                  ".ttf"));
            selectedAwtFont.deriveFont(fontSize);            
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        
        /* Loads the font/size character data into the map. */
        BufferedImage textureData = new BufferedImage(BITMAP_W, BITMAP_H, 
                                                      BufferedImage.
                                                      TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) textureData.getGraphics();
        int rowHeight = 0, xPosition = 0, yPosition = 0;
                    
        for(int i = 0; i < 256; i++)
        {
            BufferedImage temp = getFontImage(fontSize, (char)i, 
                                              selectedAwtFont);
            CharCoordinate newCharCoordinate = new CharCoordinate();
            
            newCharCoordinate.width = temp.getWidth();
            newCharCoordinate.height = temp.getHeight();
            
            if(xPosition + newCharCoordinate.width >= BITMAP_W)
            {
                xPosition = 0;
                yPosition += rowHeight;
                rowHeight = 0;
            }
            
            newCharCoordinate.xPosition = xPosition;
            newCharCoordinate.yPosition = yPosition;
                                    
            if(newCharCoordinate.height > rowHeight)
                rowHeight = newCharCoordinate.height;
            
            g.drawImage(temp, xPosition, yPosition, null);
            newUserFont.characterPositionData.put((char)i, newCharCoordinate);
            
            xPosition += newCharCoordinate.width;
        }
        
        /* Loads the texture into opengl and stores the handle for it. */
        newUserFont.fontTextureID = loadTexture(textureData, 4);
        
        /* Adds the texture data to the object. */
        newUserFont.textureData = textureData;
        
        
        /* Debugging */
        try
        {
        File outputfile = new File("test.jpg");
                ImageIO.write(textureData, "jpg", outputfile);

        }
        catch(IOException e)
        {
        }


                
        /* Adds the  user font to the user font hashmap. */
        loadedUserFonts.put(fontName + fontSize, newUserFont);
    }
    
    private BufferedImage getFontImage(int fontSize, char character, 
                                       Font selectedAwtFont)
    {
        /* Create a  buffered image to obtain height and width information. */
        BufferedImage tempFontImage = new BufferedImage(1, 1, 
                                                        BufferedImage.
                                                        TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) tempFontImage.getGraphics();
        
        FontMetrics fontMetrics = g.getFontMetrics();
        g.setFont(selectedAwtFont);
        
        int charWidth = fontMetrics.charWidth(character);
        int charHeight = fontMetrics.getHeight();
                
        if(charWidth <= 0)
            charWidth = 1;
            
        if(charHeight <= 0)
            charHeight = fontSize;
        
        /* Create the buffered image using the correct height and width. */
        tempFontImage = new BufferedImage(charWidth, charHeight, 
                                          BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D)tempFontImage.getGraphics();
        
        g.setFont(selectedAwtFont);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(character), 0, 0 + fontMetrics.getAscent());

        
        return tempFontImage;
    }
    
    public static void drawBoxTC(float x0, float y0, float x1, float y1, 
                                 float s0, float t0, float s1, float t1) 
    {
        
        GL11.glTexCoord2f(s0/BITMAP_W, t0/BITMAP_H);
        GL11.glVertex2f(x0, 640 - y0);
        GL11.glTexCoord2f(s1/BITMAP_W, t0/BITMAP_H);
        GL11.glVertex2f(x1, 640 - y0);
        GL11.glTexCoord2f(s1/BITMAP_W, t1/BITMAP_H);
        GL11.glVertex2f(x1, 640 - y1);
        GL11.glTexCoord2f(s0/BITMAP_W, t1/BITMAP_H);
        GL11.glVertex2f(x0, 640 - y1);
    } 
    
    public int loadTexture(BufferedImage image, int bytesPerPixel)
    {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, 
                     image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() *
                                                         image.getHeight() * 
                                                         bytesPerPixel); 

        for(int y = 0; y < image.getHeight(); y++)
        {
            for(int x = 0; x < image.getWidth(); x++)
            {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));   // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));    // Green component
                buffer.put((byte) (pixel & 0xFF));           // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));   // Alpha component
            }
        }

        buffer.flip(); 

        int textureID = GL11.glGenTextures(); 
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); 

        /* Sets up wrap mode. */
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 
                             GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
                             GL12.GL_CLAMP_TO_EDGE);

        /* Setup texture scaling filtering. */
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 
                             GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 
                             GL11.GL_NEAREST);

        /* Send texel data to OpenGL. */
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 
                          image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, 
                          GL11.GL_UNSIGNED_BYTE, buffer);

        return textureID;
    }
    
    public void print(float x, float y, int fontSize, String fontName, 
                      String text)
    {   
        /* Checks to see if the font is loaded. If not, loads the font. */
        if(!loadedUserFonts.containsKey(fontName + fontSize))
            loadFont(fontName, fontSize);
        
        char currentChar;
        CharCoordinate charCoord;
        int totalWidth = 0;
        UserFont currentUserFont = loadedUserFonts.get(fontName + fontSize);   
        

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentUserFont.fontTextureID);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor3f(0f, 0f, 0f);
        
        /* Draws each character to the screen. */
        for(int i = 0; i < text.length(); i++)
        {
            currentChar = text.charAt(i);
            
            if(currentChar >= 0 && currentChar < 256)
            {
                
                charCoord = currentUserFont.characterPositionData.
                                            get(currentChar);
                drawBoxTC(x, y, x + totalWidth + charCoord.width, 
                          y + charCoord.height, charCoord.xPosition, 
                          charCoord.yPosition, charCoord.xPosition + 
                          charCoord.width, charCoord.yPosition + 
                          charCoord.height);
                totalWidth += charCoord.width;
            }
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
    }
}


