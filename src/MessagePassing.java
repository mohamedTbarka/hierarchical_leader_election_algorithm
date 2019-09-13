
import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JTopology;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

/**
 * @author mohamed
 * @project Hierarchical Leader Election Algorithm with Remoteness Constraint
 */

public class MessagePassing implements ActionListener {
    Topology tp;
    private int nhc;
    private int nr = -1;
    private JLabel label;
    private final double R = 100;

    public MessagePassing() throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
        tp = new Topology();
        tp.setDefaultNodeModel(MyNode.class);
        //tp.setTimeUnit(10);
        tp.setCommunicationRange(2*R);
        //tp.setSensingRange(600);
        //JViewer jv = new JViewer (tp);
        //jv.getJTopology().addBackgroundPainter(new MyBackgroundPainter());


        JFrame window = new JFrame("Hierarchical Leader Election");
        JPanel panel = new JPanel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        JButton button = new JButton("performance test");
        JButton button_1 = new JButton("reset");
        window.add(new JTopology(tp));


        label = new JLabel("");
        panel.add(button);
        panel.add(button_1);
        button.addActionListener(this);
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Node> list = tp.getNodes();
                for (Node n : list) {
                    n.setProperty("hc", false);
                    n.setProperty("nr", 0);
                }
                nr = -1;
            }
        } );
        window.add(panel, BorderLayout.BEFORE_FIRST_LINE);

        window.add(label, BorderLayout.AFTER_LAST_LINE);
        window.pack();
        window.setVisible(true);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);

        tp.start();
        Random r = new Random();
        //tp.addNode(1300, 700);


/*
        for(int i = 0; i<200; i++) {
            double y= r.nextInt(700);
            tp.addNode(y,350+ sqrt(350*350-y-(350-y)*(350-y)), new MyNode());

        }*/
        //boolean b = true;


        for(int j = 10; j <= 100; j++) {

            for (int i = 0; i <= j; i++) {

                double t = -2 * PI * r.nextDouble();
                double _t = -R * r.nextDouble();
                tp.addNode(350 + _t * cos(t), 350 + _t * sin(t));
                //Thread.sleep(50);
                t = -2 * PI * r.nextDouble();
                _t = -R * r.nextDouble();
                tp.addNode(350 + R + 2 * R + R + 1 + _t * cos(t), 350 + _t * sin(t));

            }


            Thread.sleep(11000);

            tp.addNode(350 + 2 * R, 350);

            Thread.sleep(6000);

            button_1.doClick();
            button.doClick();
            // tp.setDefaultNodeModel(Node.class);
            // tp.disableWireless();


            for(Node n : tp.getNodes()) {
                n.setProperty("tpCleaning", true);
            }


            for(Node n : tp.getNodes()) {
                tp.removeNode(n);
            }


            //tp.setDefaultNodeModel(MyNode.class);
        }



            /*
            if(y>=0 && y<350) {

                //if(350*350-(350-y)*(350-y) >= 0)
                if(b) {
                    tp.addNode(y, r1.nextInt((int)(abs(350 - sqrt(350 * 350 - (350 - y) * (350 - y))))), new MyNode());
                    b = false;
                } else {
                    tp.addNode(y, r1.nextInt((int)(abs(700 - 350 + sqrt(350 * 350 - (350 - y) * (350 - y))))), new MyNode());
                    b=true;
                }

                /*else
                    tp.addNode(y,350- sqrt(-350*350+(350-y)*(350-y)), new MyNode());*/

            /*

            } else {
                if(b) {
                    tp.addNode(y, r1.nextInt((int)(abs(350 + sqrt(350 * 350 - (350 - y) * (350 - y))))), new MyNode());
                    b=false;
                } else {
                    tp.addNode(y, r1.nextInt((int)(abs(700 - 350 - sqrt(350 * 350 - (350 - y) * (350 - y))))), new MyNode());
                    b=true;
                }
            } */


    /*
        for(int i = 0; i<60; i++) {

            tp.addNode(r.nextInt(500)+600,r.nextInt(700), new MyNode());

        }*/
        //Thread.sleep(10000);

        //tp.addNode(550, 350);

        //Thread.sleep(1000);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //tp.addNode(-1,-1);
        nhc = 0;
        nr=0;
        List<Node> list = tp.getNodes();
        int i = 0;



        for (Node n : list) {
            if((boolean)n.getProperty("hc"))
                nhc++;
            i++;
            if (nr == -1)
                nr = (Integer)n.getProperty("nr");
            else if((Integer)n.getProperty("nr") > nr)
                nr = (Integer)n.getProperty("nr");

        }
        try {
            File f = new File("performance.txt");
            if(!f.exists()) {
                PrintWriter writer = new PrintWriter("performance.txt", "UTF-8");
                writer.println("tolal number of nodes : " + i + ", sensitivity : " + nhc + ", latency : " + nr);
                writer.close();
            } else {
                BufferedWriter output = new BufferedWriter(new FileWriter("performance.txt", true));
                output.write("tolal number of nodes : " + i + ", sensitivity : " + nhc + ", latency : " + nr);
                output.close();
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        label.setText("tolal number of nodes : " + i + ", sensitivity : " + nhc + ", latency : " + nr);


    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException, UnsupportedEncodingException {
        new MessagePassing();
    }
}