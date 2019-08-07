import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

/**
 * @author mohamed
 * @project Hierarchical Leader Election Algorithm with Remoteness Constraint
 */

public class MessagePassing{
    public static void main(String[] args){

        Topology tp = new Topology();
        tp.setDefaultNodeModel(MyNode.class);

        tp.setTimeUnit(500);
        tp.addNode(10, 10);
        tp.addNode(100, 100);
        new JViewer (tp);
        tp.start();
        //tp.setl
    }
}