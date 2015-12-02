import com.jme3.app.Application;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

public class Client extends Application
{
    private AppSettings settings = new AppSettings(true);
    protected Node rootNode = new Node("Root Node");
    protected Node guiNode = new Node("Gui Node");  
    state clientState;
    
    public enum state
    {   
        MAIN_MENU(0), IN_GAME(1), SCORE_SCREEN(2);
        private int value;
        
        private state(int value)
        {
            this.value = value;
        }
        
        int getValue()
        {
            return this.value;
        }
    }
    
    public Client()
    {
        this.initClient();
    }
    
    public void initClient()
    {   
        this.clientState = state.MAIN_MENU;
        settings.setTitle("Elements");
        this.setSettings(settings);
        this.start();
        Spatial test = assetManager.loadModel("Models/test.blend");
        viewPort.attachScene(rootNode);
        rootNode.attachChild(test);
    }
    
    public void changeTitle(String newTitle)
    {
        settings.setTitle(newTitle);
        this.setSettings(settings);
        this.restart();
    }
    
    public void update()
    {
        if(speed == 0 || paused)
            return;
        super.update();
    }
    
    public static void main(String[] args) 
    {
        Client mainClient = new Client(); 
    }
}
