import com.jme3.app.Application;
import com.jme3.system.AppSettings;

public class Client extends Application
{
    private AppSettings settings = new AppSettings(true);
    
    
    public enum clientState
    {
        MAIN_MENU, IN_GAME, SCORE_SCREEN;
    }
    
    public Client()
    {
        settings.setTitle("Elements");
        this.setSettings(settings);
        this.start();
    }
    
    public void changeTitle()
    {
        
    }
    
    public static void main(String[] args) 
    {
        Client mainClient = new Client();
    }
}
