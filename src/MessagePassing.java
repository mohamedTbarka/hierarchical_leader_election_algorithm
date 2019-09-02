import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mohamed
 * @project Hierarchical Leader Election Algorithm with Remoteness Constraint
 */

public class MessagePassing implements ActionListener {
    Topology tp;
    private int nhc;
    private JLabel label;

    public MessagePassing() {
        tp = new Topology();
        tp.setDefaultNodeModel(MyNode.class);
        tp.setTimeUnit(1000);
        tp.setCommunicationRange(100);
        //JViewer jv = new JViewer (tp);
        //jv.getJTopology().addBackgroundPainter(new MyBackgroundPainter());

        /*for(int i = 0; i<10; i++) {
            MyNode n = new MyNode();
            tp.addNode(-1,-1,n);

        }*/
        JFrame window = new JFrame("Hierarchical Leader Election");
        JPanel panel = new JPanel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        JButton button = new JButton("performance test");
        JButton button_1 = new JButton("reset");

        label = new JLabel("");
        panel.add(button);
        panel.add(button_1);
        button.addActionListener(this);
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Node> list = tp.getNodes();
                for (Node n : list)
                    n.setProperty("hc", false);

            }
        } );
        window.add(panel,BorderLayout.BEFORE_FIRST_LINE
        );

        window.add(label,BorderLayout.AFTER_LAST_LINE);
        window.add(new JTopology(tp));
        window.pack();
        window.setVisible(true);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);

        tp.start();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //tp.addNode(-1,-1);
        nhc = 0;
        List<Node> list = tp.getNodes();
        int i = 0;
        for (Node n : list) {
            if((boolean)n.getProperty("hc"))
                nhc++;
            i++;
        }
        label.setText("tolal number of nodes : "+i+", sensitivity : "+nhc);

    }

    public static void main(String[] args) {
        new MessagePassing();
    }
}