//import com.sun.scenario.effect.impl.sw.java.JSWBlend_HARD_LIGHTPeer;
import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Mohamed Tbarka
 * @project Hierarchical Leader Election Algorithm with Remoteness Constraint
 */

public class MyNode extends Node implements Comparable<Node> {

    private HashMap<Integer, Node> forming = new HashMap<>();
    private HashMap<Integer, Height> heights = new HashMap<>();
    private HashMap<Integer, Node> neighbors = new HashMap<>();
    private Height h;
    private Height message;
    private SubLeaderPair slp;
    private LogicalClock lc;
    private final int D = 2;
    private Height myOldHeight;

    @Override
    public void onStart() {
        System.out.println("hello");
        h = new Height(0, 0, 0,0, -1,this.getID());
        SubLeaderPair slp = new SubLeaderPair(this.getID(), '?');
        LogicalClock lc = new LogicalClock(0);
        //this.setProperty("height", h);
        this.setProperty("slp", slp);
        this.setProperty("lc", lc);

        heights.put(this.getID(), h);
        this.setProperty("heights", heights);
        this.setProperty("neighbors", neighbors);
        this.setProperty("forming", forming);

        Label l = new Label("hello");
        this.setLabel(l);


    }
    @Override
    public void setLabel(Object label) {
        heights = (HashMap<Integer, Height>)this.getProperty("heights");
        slp = (SubLeaderPair) this.getProperty("slp");
        lc = (LogicalClock) this.getProperty("lc");
        super.setLabel(heights.get(this.getID()).toString() + this.getID()+')'+slp.toString() + lc.toString());
        //System.out.println(h.toString() + this.getID() +")" + slp.toString() + lc.toString());
        // super.setLabel("("+ this.heights.get(this.id).getTau()+ ", " + this.heights.get(this.id).getOid() + ", " + this.heights.get(this.id).getR() + ", " + this.heights.get(this.id).getDelta() + ", " + this.heights.get(this.id).getNlts() + ", " + this.heights.get(this.id).getLid() + ", " + this.heights.get(this.id).getId()+ ") - (" + this.slid + ", " + this.pred + ") - LC : " + this.lc);
    }

    @Override
    public void onLinkAdded(Link link) {
        //link.set;
        Node n = link.getOtherEndpoint(this);
        //MyNode nn = (MyNode) n;
        forming = (HashMap<Integer, Node>) this.getProperty("forming");
        heights = (HashMap<Integer, Height>) this.getProperty("heights");

        h = heights.get(this.getID());

        forming.put(n.getID(), n);
        this.setProperty("forming", forming);
        //this.forming.put(Integer.toString(n.getID()).charAt(0), (MyNode)n);
        Message message = new Message(h);
        send(n, message);
        HashMap<Integer, Node> f = (HashMap<Integer, Node>) this.getProperty("forming");

        System.out.println("[+] node "+this.getID()+" has made contact with node : "+f.get(n.getID()).getID());
    }

    @Override

    public void onMessage(Message msg) {

        this.setColor(Color.getRandomColor());
        //System.out.println("We're at the level of node : "+this.getID());

        message = (Height) msg.getContent();
        Node n = msg.getSender();

        heights = (HashMap<Integer, Height>) this.getProperty("heights");
        forming = (HashMap<Integer, Node>) this.getProperty("forming");
        neighbors = (HashMap<Integer, Node>)this.getProperty("neighbors");

        h = heights.get(this.getID());

        forming.remove(n.getID());

        neighbors.put(n.getID(), n);

        myOldHeight = new Height(h);
        System.out.println(myOldHeight);

        heights.replace(n.getID(), message);

        if(h.getNlts() == message.getNlts() && h.getLid() == message.getLid()) {
            //System.out.println("we've entered !");
            System.out.println(neighbors.size());
            if (sink()) {
                Node m = null;
                int flag = 0;
                for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
                    if (m == null)
                        m = entry.getValue();
                    if (h.getNlts() == ((HashMap<Integer, Height>) m.getProperty("heights")).get(m.getID()).getNlts() && h.getLid() == ((HashMap<Integer, Height>) m.getProperty("heights")).get(m.getID()).getLid()) {
                        flag++;
                    }
                }
                if (flag == neighbors.size()) {
                    if (h.getTau() > 0 && h.getR() == 0)
                        reflectRefLevel(n);
                    else if (h.getTau() > 0 && h.getR() == 0 && h.getOid() == this.getID())
                        electself();
                    else
                        startNewRefLevel();
                } else
                    prograteLargestRefLevel();
            } else
                System.out.println("[+] nothin' has been done !");

        } else
            adoptLPIfPriority(n);

        Label l = new Label("hello");
        this.setLabel(l);
        this.setProperty("lc", lc);
        this.setProperty("slp", slp);
        this.setProperty("heights", heights);
        this.setProperty("neighbors", neighbors);
        this.setProperty("forming", forming);
        System.out.println(myOldHeight);
        System.out.println(((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID()));
        if (myOldHeight.compareTo(((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID())) != 0) {
            System.out.println("Heath Ledger !");

            HashMap<Integer, Node> union = new HashMap<>();
            union.putAll(forming);
            union.putAll(neighbors);
            Message msg_1 = new Message(h);

            for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
                send(entry.getValue(), msg_1);

                System.out.println("here we go !");
            }
            /*
            for(Node neighbor: this.getNeighbors()) {
                send(neighbor, msg_1);
            }*/
        }
        //this.setColor(Color.BLUE);
        /*
        for(Node node : this.getNeighbors()) {
            Link link = this.getCommonLinkWith(node);
            if(this.compareTo(node) == 1) {
                //Link l = new Link(this, node, Link.Type.DIRECTED);
                link.setColor(Color.GREEN);
            }
            else
                link.setColor(Color.RED);
        }*/


    }

    public void electself() {
        System.out.println("[+] Node : "+this.getID()+" executes ELECTSELF");
        heights = (HashMap<Integer, Height>)this.getProperty("heights");
        h = heights.get(this.getID());
        slp = (SubLeaderPair)this.getProperty("slp");
        lc = (LogicalClock)this.getProperty("lc");

        h.setTau(0);
        h.setOid(0);
        h.setR(0);

        h.setNlts(lc.getLc());
        h.setLid(this.getID());
        slp.setSlid('?');
        slp.setPred('?');

        lc.setLc(lc.getLc() + 1);
        lc.setLc(lc.getLc() + 1);
        heights.replace(this.getID(), h);
    }

    public void reflectRefLevel(Node n) {
        System.out.println("[+] Node : "+this.getID()+" executes REFLECTREFLEVEL");
        slp = (SubLeaderPair)this.getProperty("slp");
        lc = (LogicalClock)this.getProperty("lc");
        int i = message.getTau();
        h.setTau(i);
        h.setOid(message.getOid());
        h.setR(1);
        h.setDelta(0);

        slp.setSlid('?');
        slp.setPred('?');

        lc.setLc(lc.getLc() + 1);
        heights.replace(this.getID(), h);

    }

    public void prograteLargestRefLevel() {
        System.out.println("[+] Node : "+this.getID()+" executes PROPAGATELARGESTREFLEVEL");

        //List<Node> nodes = node.getNeighbors();

        HashMap<Integer, Node> ns = (HashMap<Integer, Node>)this.getProperty("neighbors");


        Map.Entry<Integer, Node> maxEntry = null;

        for (Map.Entry<Integer, Node> entry : neighbors.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        h.setTau(((HashMap<Integer, Height>) maxEntry.getValue().getProperty("heights")).get(maxEntry.getKey()).getTau());
        h.setOid(((HashMap<Integer, Height>) maxEntry.getValue().getProperty("heights")).get(maxEntry.getKey()).getOid());

        //Map.Entry<Integer, Height> minEntry = null;

        for (Map.Entry<Integer, Node> entry : ns.entrySet())
        {
            if (h.getTau() != ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey()).getTau() && h.getOid() != ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey()).getOid() && h.getR() != ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey()).getR())
            {
                ns.remove(entry.getKey());
            }
        }

        Map.Entry<Integer, Node> minEntry = null;

        for (Map.Entry<Integer, Node> entry : ns.entrySet())
        {
            if (minEntry == null || entry.getValue().compareTo(minEntry.getValue()) < 0)
            {
                minEntry = entry;
            }
        }

        h.setDelta(((HashMap<Integer, Height>) minEntry.getValue().getProperty("heights")).get(minEntry.getKey()).getDelta() - 1);
        slp.setSlid('?');
        slp.setPred('?');

        lc.setLc(lc.getLc() + 1);
        heights.replace(this.getID(), h);
    }

    public void startNewRefLevel() {
        System.out.println("[+] Node : "+this.getID()+" executes STARTNEWREFLEVEL");
        System.out.println(neighbors.size());

        heights = (HashMap<Integer, Height>) this.getProperty("heights");
        h = heights.get(this.getID());
        slp = (SubLeaderPair)this.getProperty("slp");
        lc = (LogicalClock)this.getProperty("lc");
        h.setTau(lc.getLc());
        h.setOid(this.getID());
        h.setR(0);
        h.setDelta(0);

        slp.setSlid('?');
        slp.setPred('?');

        lc.setLc(lc.getLc() + 1);
        heights.replace(this.getID(), h);

    }

    public void adoptLPIfPriority(Node n) {
        System.out.println("[+] Node : "+this.getID()+" executes ADOPTLPIFPRIORITY");

        System.out.println("we've entered !---");
        //Height height = ((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID());

        //Height height_1 = ((HashMap<Integer, Height>) node.getProperty("heights")).get(node.getID());

        if (message.getNlts() < h.getNlts()|| message.getNlts() == h.getNlts() && message.getLid() < h.getLid()) {
            h.setTau(message.getTau());
            h.setOid(message.getOid());
            h.setR(message.getR());

            h.setDelta(message.getDelta() + 1);

            h.setNlts(message.getNlts());
            h.setLid(message.getLid());
        }
        System.out.println("[+] "+myOldHeight);
        heights.replace(this.getID(), h);
        System.out.println("[+] "+myOldHeight);

        lc.setLc(lc.getLc() + 1);
        System.out.println(neighbors.size());
    }

    public boolean sink() {
        System.out.println("[+] SINK has been executed !");

        for (Map.Entry<Integer, Node> entry : neighbors.entrySet())
        {
            Height height1 = ((HashMap<Integer, Height>)entry.getValue().getProperty("heights")).get(entry.getValue().getID());
            System.out.println(h);
            System.out.println(height1);
            System.out.println(this.compareTo(entry.getValue()) == -1);
            if (height1.getNlts() != h.getNlts() || height1.getLid() != h.getLid() ||  this.compareTo(entry.getValue()) == 1 || h.getLid() == this.getID())
                return false;
        }
        return true;
    }
/*
    @Override
    public void onLinkRemoved(Link link) {
        System.out.println("hello");
        Node n = link.getOtherEndpoint(this);
        //MyNode nn = (MyNode) n;
        this.forming.remove(nn.getID());
        this.neighbors.remove(nn.getID());
        if(this.neighbors == null) {
            electself();
            Message message = new Message(this.getHeights().get(this.id));

            for (Map.Entry<Character, MyNode> entry : forming.entrySet())
            {
                send(entry.getValue(), message);
            }

        } else if(sink()) {
            startNewRefLevel();
            HashMap<Character, MyNode> union = new HashMap<>();
            union.putAll(this.forming);
            union.putAll(this.neighbors);

            for (Map.Entry<Character, MyNode> entry : forming.entrySet())
            {
                Message message = new Message(this.getHeights().get(this.id));
                send(entry.getValue(), message);
            }
        }
        //super.onLinkRemoved(link);
    }
    *
 */

/*

    /*


    //

    */


    @Override
    public int compareTo(Node node) {

        Height h2 = ((HashMap<Integer, Height>) node.getProperty("heights")).get(node.getID());

        if (h.getTau() > h2.getTau()) {
            return 1;
        } else if (h.getTau() < h2.getTau()) {
            return -1;
        } else {
            if (h.getOid()> h2.getOid()) {
                return 1;
            } else if (h.getOid()< h2.getOid()) {
                return -1;
            } else {
                if (h.getR() > h2.getR()) {
                    return 1;
                } else if (h.getR() < h2.getR()) {
                    return -1;
                } else {
                    if (h.getDelta() > h2.getDelta()) {
                        return 1;
                    } else if (h.getDelta() < h2.getDelta()) {
                        return -1;
                    } else {
                        if (h.getNlts() > h2.getNlts()) {
                            return 1;
                        } else if (h.getNlts() < h2.getNlts()) {
                            return -1;
                        } else {
                            if (h.getLid() > h2.getLid()) {
                                return 1;
                            } else if (h.getLid() < h2.getLid()) {
                                return -1;
                            } else {
                                if (this.getID()> node.getID()) {
                                    return 1;
                                } else if (this.getID()< node.getID()) {
                                    return -1;
                                }
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

}