import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class Test_1 {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setTimeUnit(500);
        tp.setCommunicationRange(100);
        tp.addNode(0,10);
        tp.addNode(101,  10);// slow on purpose (500 ms per round)
        new JViewer(tp);
        tp.start();
        while(true)
            System.out.println(tp.getCommunicationRange());
    }
}
