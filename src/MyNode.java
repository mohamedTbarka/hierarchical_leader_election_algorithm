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
    //private boolean heardSomeone = false;
    // here we gonna put the 7-tuple and the sub-leader pair.
    //private Height height;
    //private char id;
    //private char slid;
    //private char pred;

    //private int lc;
    //private boolean heightChanged;

    //private HashMap<int, MyNode> neighbors;
    //private HashMap<int, MyNode> forming;

    //private HashMap<int, Height> heights;


    /*
    public MyNode(char id, HashMap<Character, Height> heights, char slid, char pred, int lc, int d) {

        this.id = id;
        this.slid = slid;
        this.pred = pred;
        this.lc = lc;
        this.heights = new HashMap<Character, Height>();
        this.heights=heights;
        this.neighbors=neighbors = new HashMap<>();
        this.forming=forming;

        if(this.heights.get(this.id).getDelta() % d == 0)
            this.setColor(Color.orange);
    }

    public MyNode() {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        char c = alphabet.charAt(rnd.nextInt(alphabet.length()));
        this.id = c;
        Height height = new Height(0, 0, 0, 0, -1, c, c);
        neighbors = new HashMap<Character, MyNode>();
        heights = new HashMap<Character, Height>();
        this.heights.put(c, height);
        MyNode n = new MyNode(c, this.heights, 'H', '?', 1, 2);
        this.neighbors.put(c, n);
        Label l = new Label("hello");
        this.slid = c;
        this.pred = '?';
        this.setLabel(l);
        System.out.println("hello");
        System.out.println(this.heights.get(c).getDelta());
    }*/

    @Override
    public void onStart() {
        System.out.println("hello");
        Height h = new Height(0, 0, 0,0, -1,this.getID());
        SubLeaderPair slp = new SubLeaderPair(this.getID(), '?');
        LogicalClock lc = new LogicalClock(0);
        //this.setProperty("height", h);
        this.setProperty("slp", slp);
        this.setProperty("lc", lc);
        HashMap<Integer, Node> neighbors= new HashMap<>();
        HashMap<Integer, Node> forming= new HashMap<>();

        HashMap<Integer, Height> heights = new HashMap<>();
        heights.put(this.getID(), h);
        this.setProperty("heights", heights);
        this.setProperty("neighbors", neighbors);
        this.setProperty("forming", forming);

        Label l = new Label("hello");
        this.setLabel(l);

    }
    @Override
    public void setLabel(Object label) {
        HashMap<Integer, Height> h = (HashMap<Integer, Height>)this.getProperty("heights");
        SubLeaderPair slp = (SubLeaderPair) this.getProperty("slp");
        LogicalClock lc = (LogicalClock) this.getProperty("lc");
        super.setLabel(h.get(this.getID()).toString() + this.getID()+')'+slp.toString() + lc.toString());
        //System.out.println(h.toString() + this.getID() +")" + slp.toString() + lc.toString());
        // super.setLabel("("+ this.heights.get(this.id).getTau()+ ", " + this.heights.get(this.id).getOid() + ", " + this.heights.get(this.id).getR() + ", " + this.heights.get(this.id).getDelta() + ", " + this.heights.get(this.id).getNlts() + ", " + this.heights.get(this.id).getLid() + ", " + this.heights.get(this.id).getId()+ ") - (" + this.slid + ", " + this.pred + ") - LC : " + this.lc);
    }

    @Override
    public void onLinkAdded(Link link) {
        //link.set;
        Node n = link.getOtherEndpoint(this);
        //MyNode nn = (MyNode) n;
        HashMap<Integer, Node> forming = (HashMap<Integer, Node>) this.getProperty("forming");
        HashMap<Integer, Height> heights = (HashMap<Integer, Height>) this.getProperty("heights");

        Height h = heights.get(this.getID());

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

        Height h1 = (Height) msg.getContent();
        Node n = msg.getSender();

        HashMap<Integer, Height> heights = (HashMap<Integer, Height>) this.getProperty("heights");
        HashMap<Integer, Node> forming = (HashMap<Integer, Node>) this.getProperty("forming");
        HashMap<Integer, Node> neighbors = (HashMap<Integer, Node>)this.getProperty("neighbors");

        Height h = heights.get(this.getID());
        //System.out.println(h.getId());
        //MyNode n = (MyNode) msg.getSender();
        MyNode oldNode = this;
        //heights.put(n.getID(), h1);
        forming.remove(n.getID());

        Height myOldHeight = h;

        heights.replace(n.getID(), h1);

        //heights.put(this.getID(), h);
        this.setProperty("heights", heights);
        this.setProperty("forming", forming);
        this.setProperty("neighbors", neighbors);

        //heights = (HashMap<Integer, Height>) this.getProperty("heights");
        //forming = (HashMap<Integer, Node>) this.getProperty("forming");
        //neighbors = (HashMap<Integer, Node>)this.getProperty("neighbors");
        //h = heights.get(this.getID());

        if(h.getNlts() == h1.getNlts() && h.getLid() == h1.getLid()) {
            //System.out.println("we've entered !");
            if (sink()) {
                Node m = null;
                int flag = 0;
                for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
                    if (m == null)
                        m = entry.getValue();
                    if (((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey()).getNlts() == ((HashMap<Integer, Height>) m.getProperty("heights")).get(m.getID()).getNlts() && ((HashMap<Integer, Height>) m.getProperty("heights")).get(entry.getKey()).getLid() == ((HashMap<Integer, Height>) m.getProperty("heights")).get(m.getID()).getLid()) {
                        flag++;
                    }
                }
                if (flag == neighbors.size()) {
                    if (heights.get(this.getID()).getTau() > 0 && heights.get(this.getID()).getR() == 0)
                        reflectRefLevel(n);
                    else if (heights.get(this.getID()).getTau() > 0 && heights.get(this.getID()).getR() == 0 && heights.get(this.getID()).getOid() == this.getID())
                        electself();
                    else
                        startNewRefLevel();
                } else
                    prograteLargestRefLevel();
            }
            //System.out.println("Pay attention !!!!!!\n" + myOldHeight.toString() + '\n' + ((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID()).toString());
            //System.out.println("here we go ! : " + myOldHeight.toString() + "\n" + heights.get(this.getID()).toString());
            //System.out.println("Boolean result : " + Boolean.toString(myOldHeight.compareTo(((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID())) == 0));

        } else
            adoptLPIfPriority(n);
        if (myOldHeight.compareTo(((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID())) != 0) {
            System.out.println("Heath Ledger !");

            HashMap<Integer, Node> union = new HashMap<>();
            union.putAll(forming);
            union.putAll(neighbors);
            Message msg_1 = new Message(heights.get(this.getID()));
            /*
            for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
                send(entry.getValue(), msg_1);

                System.out.println("here we go !");
            }*/
            for(Node neighbor: this.getNeighbors()) {
                send(neighbor, msg_1);
            }
        }
        //this.setColor(Color.BLUE);
        Label l = new Label("hello");
        this.setLabel(l);
    }

    public void electself() {
        System.out.println("[+] Node : "+this.getID()+" executes ELECTSELF");
        HashMap<Integer, Height> heights = (HashMap<Integer, Height>)this.getProperty("heights");
        Height height = heights.get(this.getID());
        SubLeaderPair slp = (SubLeaderPair)this.getProperty("slp");
        LogicalClock lc = (LogicalClock)this.getProperty("lc");

        height.setTau(0);
        height.setOid(0);
        height.setR(0);

        height.setNlts(lc.getLc());
        height.setLid(this.getID());
        slp.setSlid('?');
        slp.setPred('?');

        lc.setLc(lc.getLc() + 1);
        lc.setLc(lc.getLc() + 1);
        this.setProperty("lc", lc);
        this.setProperty("slp", slp);
        heights.replace(this.getID(), height);
        this.setProperty("heights", heights);
    }

    public void reflectRefLevel(Node n) {
        System.out.println("[+] Node : "+this.getID()+" executes REFLECTREFLEVEL");

        HashMap<Integer, Height> heights = (HashMap<Integer, Height>)this.getProperty("heights");
        Height height = heights.get(this.getID());
        Height height_1 = heights.get(n.getID());
        SubLeaderPair slp = (SubLeaderPair)this.getProperty("slp");
        LogicalClock lc = (LogicalClock)this.getProperty("lc");

        height.setTau(height_1.getTau());
        height.setOid(height_1.getOid());
        height.setR(1);
        height.setDelta(0);

        slp.setSlid('?');
        slp.setPred('?');

        lc.setLc(lc.getLc() + 1);
        this.setProperty("lc", lc);
        this.setProperty("slp", slp);
        heights.replace(this.getID(), height);
        this.setProperty("heights", heights);

    }

    public void prograteLargestRefLevel() {
        System.out.println("[+] Node : "+this.getID()+" executes PROPAGATELARGESTREFLEVEL");

        //List<Node> nodes = node.getNeighbors();

        HashMap<Integer, Node> neighbors = (HashMap<Integer, Node>)this.getProperty("neighbors");
        HashMap<Integer, Node> ns = (HashMap<Integer, Node>)this.getProperty("neighbors");
        HashMap<Integer, Height> heights = (HashMap<Integer, Height>)this.getProperty("heights");
        Height height = heights.get(this.getID());

        SubLeaderPair slp = (SubLeaderPair)this.getProperty("slp");
        LogicalClock lc = (LogicalClock)this.getProperty("lc");

        Map.Entry<Integer, Node> maxEntry = null;

        for (Map.Entry<Integer, Node> entry : neighbors.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        height.setTau(((HashMap<Integer, Height>) maxEntry.getValue().getProperty("heights")).get(maxEntry.getKey()).getTau());
        height.setOid(((HashMap<Integer, Height>) maxEntry.getValue().getProperty("heights")).get(maxEntry.getKey()).getOid());

        //Map.Entry<Integer, Height> minEntry = null;

        for (Map.Entry<Integer, Node> entry : ns.entrySet())
        {
            if (height.getTau() != ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey()).getTau() && height.getOid() != ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey()).getOid() && height.getR() != ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey()).getR())
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

        height.setDelta(((HashMap<Integer, Height>) minEntry.getValue().getProperty("heights")).get(minEntry.getKey()).getDelta() - 1);
        slp.setSlid('?');
        slp.setPred('?');

        lc.setLc(lc.getLc() + 1);

        this.setProperty("lc", lc);
        this.setProperty("slp", slp);
        heights.replace(this.getID(), height);
        this.setProperty("heights", heights);

    }

    public void startNewRefLevel() {
        System.out.println("[+] Node : "+this.getID()+" executes STARTNEWREFLEVEL");

        HashMap<Integer, Height> heights = (HashMap<Integer, Height>) this.getProperty("heights");
        Height height = heights.get(this.getID());
        SubLeaderPair slp = (SubLeaderPair)this.getProperty("slp");
        LogicalClock lc = (LogicalClock)this.getProperty("lc");
        height.setTau(lc.getLc());
        height.setOid(this.getID());
        height.setR(0);
        height.setDelta(0);

        slp.setSlid('?');
        slp.setPred('?');

        lc.setLc(lc.getLc() + 1);
        heights.replace(this.getID(), height);
        this.setProperty("heights", heights);
        this.setProperty("slp", slp);
        this.setProperty("lc", lc);
    }

    public void adoptLPIfPriority(Node node) {
        System.out.println("[+] Node : "+this.getID()+" executes ADOPTLPIFPRIORITY");

        System.out.println("we've entered !---");
        Height height = ((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID());
        Height height_1 = ((HashMap<Integer, Height>) node.getProperty("heights")).get(node.getID());

        if (height_1.getNlts() < height.getNlts()|| height_1.getNlts() == height.getNlts() && height_1.getLid() < height.getLid()) {
            height.setTau(height_1.getTau());
            height.setOid(height_1.getOid());
            height.setR(height_1.getR());

            height.setDelta(height_1.getDelta() + 1);

            height.setNlts(height_1.getNlts());
            height.setLid(height_1.getLid());
        }
        HashMap<Integer, Height> heights = (HashMap<Integer, Height>) this.getProperty("heights");
        heights.replace(this.getID(), height);
        this.setProperty("heights", heights);
    }

    public boolean sink() {
        System.out.println("[+] SINK has been executed ELECTSELF");

        //Map.Entry<Integer, Height> minEntry = null;
        Height height = ((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID());
        HashMap<Integer, Node> neighbors = (HashMap<Integer, Node>) this.getProperty("neighbors");
        for (Map.Entry<Integer, Node> entry : neighbors.entrySet())
        {
            //HashMap<Integer, Height> hs = (HashMap<Integer, Height>)entry.getValue().getProperty("heights");
            Height height1 = ((HashMap<Integer, Height>)entry.getValue().getProperty("heights")).get(this.getID());
            if (height1.getNlts() != height.getNlts() ||  this.compareTo(entry.getValue()) != -1 || height.getLid() != this.getID())
            {
                return false;
            }
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
        Height h1 = ((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID());
        Height h2 = ((HashMap<Integer, Height>) node.getProperty("heights")).get(node.getID());

        if (h1.getTau() > h2.getTau()) {
            return 1;
        } else if (h1.getTau() < h2.getTau()) {
            return -1;
        } else {
            if (h1.getOid()> h2.getOid()) {
                return 1;
            } else if (h1.getOid()< h2.getOid()) {
                return -1;
            } else {
                if (h1.getR() > h2.getR()) {
                    return 1;
                } else if (h1.getR() < h2.getR()) {
                    return -1;
                } else {
                    if (h1.getDelta() > h2.getDelta()) {
                        return 1;
                    } else if (h1.getDelta() < h2.getDelta()) {
                        return -1;
                    } else {
                        if (h1.getNlts() > h2.getNlts()) {
                            return 1;
                        } else if (h1.getNlts() < h2.getNlts()) {
                            return -1;
                        } else {
                            if (h1.getLid() > h2.getLid()) {
                                return 1;
                            } else if (h1.getLid() < h2.getLid()) {
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