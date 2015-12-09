import java.io.IOException;

public class ClientLoader
{

public static void main(String[] args) 
    {
        if(args.length == 0)
        {
            try
            {
                Runtime.getRuntime().exec(new String[] {"java", "-Djava.library.path=\"/lib/native/\"", "-jar", "run.jar"}); 
            }
            catch(IOException ioe) 
            {  
                ioe.printStackTrace();  
                
            }
            System.exit(0);
        }
        System.out.println("Running LWJGL native libraries.");
        Client c = new Client();
       
    }
}
