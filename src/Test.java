import io.jbotsim.core.Topology;
import io.jbotsim.ui.JTopology;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test implements ActionListener{
    Topology tp;

    public Test() {
        tp = new Topology();
        JFrame window = new JFrame("MyCustomApp");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        JButton button = new JButton("Add random node");
        button.addActionListener(this);
        window.add(button,BorderLayout.PAGE_START);
        window.add(new JTopology(tp));
        window.pack();
        window.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tp.addNode(-1,-1);
    }

    public static void main(String[] args) {
        new Test();
    }
}