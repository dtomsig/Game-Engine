public class ClientLoader
{
public static void main(String[] args) 
    {
        System.out.println("Running LWJGL native libraries.");
        System.setProperty("org.lwjgl.librarypath", "lib/native");
        Client c = new Client();
        c.run();
    }
}
