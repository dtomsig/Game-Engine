import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Client extends SimpleApplication
{
    public static void main(String[] args) 
    {
        Client app = new Client();
        app.start();
    }
    
    public void simpleInitApp()
    {
        Box b = new Box(1, 1, 1); // create cube shape
        Material mat = new Material(assetManager, "/Common/MatDefs/Misc/Unshaded.j3md");
        Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
        geom.setMaterial(mat);
        rootNode.attachChild(geom);              // make the cube appear in the scene
    }

}
