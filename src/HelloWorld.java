import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class HelloWorld{
    public static void main(String[] args){
        Topology tp = new Topology();
        tp.setDefaultNodeModel(BroadcastNode.class);
        tp.setTimeUnit(500);
        new JViewer (tp);
        tp.start();
    }
}