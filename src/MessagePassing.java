import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class MessagePassing{
    public static void main(String[] args){

        Topology tp = new Topology();
        tp.setDefaultNodeModel(LonerMessageBased.class);
        tp.setTimeUnit(500);
        tp.addNode(10, 10);
        tp.addNode(100, 100);
        new JViewer (tp);
        tp.start();
    }
}