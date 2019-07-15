import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import java.awt.*;


public class SampleExecution {

    public static void main(String[] args) {

        Topology tp = new Topology();

        tp.disableWireless();

        Height height_1 = new Height(0, 0, 0, 0, -1, 'H', 'H');
        MyNode n1 = new MyNode(height_1, 'H', '?', 1,2);

        Height height_2 = new Height(0, 0, 0, 1, -1, 'H', 'G');
        MyNode n2 = new MyNode(height_2,'H', 'H', 0,2);

        Height height_3 = new Height(0, 0, 0, 2, -1, 'H', 'F');
        MyNode n3 = new MyNode(height_3,'H', 'G',0,2);

        Height height_4 = new Height(0, 0, 0, 2, -1, 'H', 'E');
        Node n4 = new MyNode(height_4, 'H', 'G', 0,2);

        Height height_5 = new Height(0, 0, 0, 2, -1, 'H', 'D');
        MyNode n5 = new MyNode(height_5, 'H', 'G', 0,2);

        Height height_6 = new Height(0, 0, 0, 3, -1, 'H', 'C');
        MyNode n6 = new MyNode(height_6, 'F', 'F', 0,2);

        Height height_7 = new Height(0, 0, 0, 3, -1, 'H', 'B');
        MyNode n7 = new MyNode(height_7, 'D', 'D', 0,2);

        Height height_8 = new Height(0, 0, 0, 4, -1, 'H', 'A');
        MyNode n8 = new MyNode(height_8, 'D', 'B', 0,2);

        //n8.setLabel("hello");

        tp.addNode(300, 100, n8);
        tp.addNode(200, 200, n7);
        tp.addNode(400, 200, n6);
        tp.addNode(200, 300, n4);
        tp.addNode(400, 300, n3);
        tp.addNode(150, 400, n1);
        tp.addNode(300, 400, n2);
        tp.addNode(300, 250, n5);

        Label l = new Label("hello");

        n1.setLabel(l);
        n2.setLabel(l);
        n3.setLabel(l);
        n4.setLabel(l);
        n5.setLabel(l);
        n6.setLabel(l);
        n7.setLabel(l);
        n8.setLabel(l);

        n1.setColor(Color.red);

        //n8.setLabel("hello");

        Link link = new Link(n2, n1, Link.Type.DIRECTED);

        Link link_1 = new Link(n3, n2, Link.Type.DIRECTED);

        Link link_2 = new Link(n4, n2, Link.Type.DIRECTED);

        Link link_3 = new Link(n5, n2, Link.Type.DIRECTED);

        Link link_4 = new Link(n7, n4, Link.Type.DIRECTED);

        Link link_5 = new Link(n7, n5, Link.Type.DIRECTED);

        Link link_6 = new Link(n6, n3, Link.Type.DIRECTED);

        Link link_7 = new Link(n8, n6, Link.Type.DIRECTED);

        Link link_8 = new Link(n8, n7, Link.Type.DIRECTED);


        tp.addLink(link);
        tp.addLink(link_1);
        tp.addLink(link_2);
        tp.addLink(link_3);
        tp.addLink(link_4);
        tp.addLink(link_5);
        tp.addLink(link_6);
        tp.addLink(link_7);
        tp.addLink(link_8);

        tp.setDimensions(600, 500);
        //tp.setClockModel();

        new JViewer (tp);
        tp.start();

    }
}
