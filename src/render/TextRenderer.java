import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryUtil; 


public class TextRenderer
{       
    private static int BITMAP_W = 512;
    private static int BITMAP_H = 512;
    private HashMap<String, UserFont> loaded_user_fonts = new HashMap<String, 
                                                                    UserFont>();
    
    public class UserFont
    {
        public int font_texture_id;
        public BufferedImage texture_data;
        public HashMap<Character, CharCoordinate> character_position_data 
                                                  = new HashMap<Character, 
                                                                CharCoordinate>
                                                                             ();
    }
    
    public class CharCoordinate
    {
        public int x_position;
        public int y_position;
        public int width;
        public int height;
    }
    
    public void load_font(String font_name, int font_size)
    {   
        /* Leaves the function if the font is already loaded. */
        if(loaded_user_fonts.containsKey(font_name + font_size))
            return;
        
        UserFont new_user_font = new UserFont();
        
        /* Verifies that the file exists, throws an exception if it does not. */
        Font selected_awt_font;
        try
        {
            selected_awt_font = Font.createFont(Font.TRUETYPE_FONT, 
                                              new FileInputStream("assets/font" 
                                                                  + "s/" + 
                                                                  font_name + 
                                                                  ".ttf"));
            selected_awt_font = selected_awt_font.deriveFont((float)font_size);            
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
            
        /* Loads the font/size character data into the map. */
        BufferedImage texture_data = new BufferedImage(BITMAP_W, BITMAP_H, 
                                                       BufferedImage.
                                                       TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) texture_data.getGraphics();
        int row_height = 0, x_position = 0, y_position = 0;
                    
        for(int i = 0; i < 256; i++)
        {
            BufferedImage temp = get_font_image(font_size, (char)i, 
                                              selected_awt_font);
            CharCoordinate new_char_coordinate = new CharCoordinate();
            
            new_char_coordinate.width = temp.getWidth();
            new_char_coordinate.height = temp.getHeight();
            
            if(x_position + new_char_coordinate.width >= BITMAP_W)
            {
                x_position = 0;
                y_position += row_height;
                row_height = 0;
            }
            
            new_char_coordinate.x_position = x_position;
            new_char_coordinate.y_position = y_position;
                                    
            if(new_char_coordinate.height > row_height)
                row_height = new_char_coordinate.height;
            
            g.drawImage(temp, x_position, y_position, null);
            new_user_font.character_position_data.put((char)i, 
                                                      new_char_coordinate);
            
            x_position += new_char_coordinate.width;
        }
        
        /* Loads the texture into OpenGL and stores the handle for it. */
        new_user_font.font_texture_id = load_texture(texture_data, 4);
        
        /* Adds the texture data to the object. */
        new_user_font.texture_data = texture_data;
                
        /* Adds the  user font to the user font hashmap. */
        loaded_user_fonts.put(font_name + font_size, new_user_font);
    }
    
    private BufferedImage get_font_image(int font_size, char character, Font s)
    {
        /* Create a  buffered image to obtain height and width information. */
        BufferedImage temp_font_image = new BufferedImage(1, 1, 
                                                        BufferedImage.
                                                        TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) temp_font_image.getGraphics();
        
        FontMetrics font_metrics = g.getFontMetrics();
        g.setFont(s);
        
        int char_width = font_metrics.charWidth(character);
        int char_height = font_metrics.getHeight();
                
        if(char_width <= 0)
            char_width = 1;
            
        if(char_height <= 0)
            char_height = font_size;
        
        /* Create the buffered image using the correct height and width. */
        temp_font_image = new BufferedImage(char_width, char_height, 
                                            BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D)temp_font_image.getGraphics();
        
        g.setFont(s);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(character), 0, 0 + font_metrics.getAscent());

        
        return temp_font_image;
    }
    
    public static void draw_box_tc(float x0, float y0, float x1, float y1, 
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
    
    public int load_texture(BufferedImage image, int bytes_per_pixel)
    {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, 
                     image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() *
                                                         image.getHeight() * 
                                                         bytes_per_pixel); 

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

        int texture_id = GL11.glGenTextures(); 
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture_id); 

        /* Sets up wrap mode. */
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 
                             GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T,
                             GL12.GL_CLAMP_TO_EDGE);

        /* Sets up texture scaling filtering. */
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 
                             GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 
                             GL11.GL_NEAREST);

        /* Sends texel data to OpenGL. */
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, 
                          image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, 
                          GL11.GL_UNSIGNED_BYTE, buffer);

        return texture_id;
    }
    
    public void print(float x, float y, String font_name, int font_size, 
                      String text)
    {           
        char current_char;
        CharCoordinate char_coord;
        int total_width = 0;
        UserFont current_user_font = loaded_user_fonts.get(font_name + 
                                                           font_size);   

        /* Checks to see if the font is loaded. If not, loads the font. */
        if(!loaded_user_fonts.containsKey(font_name + font_size))
            load_font(font_name, font_size);
        
              
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, current_user_font.font_texture_id);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glColor3f(0f, 0f, 0f);
        
        /* Draws each character to the screen. */
        for(int i = 0; i < text.length(); i++)
        {
            current_char = text.charAt(i);
            
            if(current_char >= 0 && current_char < 256)
            {
                
                char_coord = current_user_font.character_position_data.
                                            get(current_char);
                draw_box_tc(x, y, x + total_width + char_coord.width, 
                          y + char_coord.height, char_coord.x_position, 
                          char_coord.y_position, char_coord.x_position + 
                          char_coord.width, char_coord.y_position + 
                          char_coord.height);
                total_width += char_coord.width;
            }
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_BLEND);
    }
}
