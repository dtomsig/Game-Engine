import com.jme3.app.Application;

public class Client extends Application
{
    public enum clientState
    {
        MAIN_MENU, IN_GAME, SCORE_SCREEN;
    }
    
    public static void main(String[] args) 
    {
        Client app = new Client();
        app.start();
    }
    
    public void simpleInitApp()
    {
        
    }
}
