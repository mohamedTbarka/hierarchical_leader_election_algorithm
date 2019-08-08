import com.sun.scenario.effect.impl.sw.java.JSWBlend_HARD_LIGHTPeer;
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

public class MyNode extends Node implements Comparable<MyNode> {
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
    public void setLabel(Object label) {
        Height h = (Height)this.getProperty("height");
        SubLeaderPair slp = (SubLeaderPair) this.getProperty("slp");
        LogicalClock lc = (LogicalClock) this.getProperty("lc");
        super.setLabel(h.toString() + slp.toString() + lc.toString());
        // super.setLabel("("+ this.heights.get(this.id).getTau()+ ", " + this.heights.get(this.id).getOid() + ", " + this.heights.get(this.id).getR() + ", " + this.heights.get(this.id).getDelta() + ", " + this.heights.get(this.id).getNlts() + ", " + this.heights.get(this.id).getLid() + ", " + this.heights.get(this.id).getId()+ ") - (" + this.slid + ", " + this.pred + ") - LC : " + this.lc);
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

    @Override
    public void onLinkAdded(Link link) {
        //link.set;
        Node n = link.getOtherEndpoint(this);
        //MyNode nn = (MyNode) n;
        //this.forming.put(Integer.toString(n.getID()).charAt(0), (MyNode)n);
        Message message = new Message(this.getHeights().get(this.getId()));
        send(n, message);
        //super.onLinkAdded(link);
        System.out.println(n.getInLinks());
    }

    @Override
    public void onMessage(Message msg) {
        Height height = (Height) msg.getContent();
        Node n_1 = msg.getSender();
        Height h = (Height)n_1.getProperty("height");
        System.out.println(h.getId());
        MyNode n = (MyNode) msg.getSender();
        this.neighbors.put(height.getId(), n);
        this.forming.remove(height.getId());
        this.heights.replace(height.getId(), height);
        Height myOldHeight=this.heights.get(this.getId());
        if(this.heights.get(this.id).getNlts() == n.getHeights().get(n.getId()).getNlts()) {
            if(sink()) {
                MyNode m = null;
                int flag=0;
                for (Map.Entry<Character, MyNode> entry : neighbors.entrySet())
                {
                    if(m == null)
                        m=entry.getValue();
                    if(entry.getValue().getHeights().get(entry.getKey()).getNlts() == m.getHeights().get(m.getId()).getNlts() && entry.getValue().getHeights().get(entry.getKey()).getLid() == m.getHeights().get(m.getId()).getLid()) {
                        flag++;
                    }

                }
                if(flag == neighbors.size()) {
                    if(this.heights.get(this.id).getTau() > 0 && this.heights.get(this.id).getR() == 0)
                        reflectRefLevel(n);
                    else if(this.heights.get(this.id).getTau() > 0 && this.heights.get(this.id).getR() == 0 && this.heights.get(this.id).getOid() == this.id)
                        electself();
                    else
                        startNewRefLevel();
                } else
                    prograteLargestRefLevel();
            } else
                adoptLPIfPriority(n);
            if(myOldHeight != this.heights.get(this.id)) {
                HashMap<Character, MyNode> union = new HashMap<>();
                union.putAll(this.forming);
                union.putAll(this.neighbors);
                Message msg_1 = new Message(this.heights.get(this.id));
                for (Map.Entry<Character, MyNode> entry : neighbors.entrySet()) {
                    send(entry.getValue(), msg_1);
                }
            }

        }
    }

    public void electself() {
        heights.get(this.id).setTau(0);
        heights.get(this.id).setOid(0);
        heights.get(this.id).setR(0);

        heights.get(this.id).setNlts(this.getLc());
        heights.get(this.id).setLid(heights.get(this.id).getId());
        this.setSlid('?');
        this.setPred('?');

        this.setLc(this.getLc() + 1);
    }

    public void reflectRefLevel(MyNode n) {
        heights.get(this.id).setTau(n.getHeights().get(n.getId()).getTau());
        heights.get(this.id).setOid(n.getHeights().get(n.getId()).getOid());
        heights.get(this.id).setR(1);
        heights.get(this.id).setDelta(0);

        this.setSlid('?');
        this.setPred('?');

        this.setLc(this.getLc() + 1);
    }

    public void prograteLargestRefLevel() {
        //List<Node> nodes = node.getNeighbors();

        HashMap<Character, MyNode> nodes_rl = this.neighbors;

        Map.Entry<Character, MyNode> maxEntry = null;

        for (Map.Entry<Character, MyNode> entry : this.neighbors.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        this.heights.get(this.id).setTau(maxEntry.getValue().getHeights().get(maxEntry.getKey()).getTau());
        this.heights.get(this.id).setOid(maxEntry.getValue().getHeights().get(maxEntry.getKey()).getOid());

        //Map.Entry<Integer, Height> minEntry = null;

        for (Map.Entry<Character, MyNode> entry : nodes_rl.entrySet())
        {
            if (this.heights.get(this.id).getTau() != entry.getValue().getHeights().get(this.id).getTau() && this.heights.get(this.id).getOid() != entry.getValue().getHeights().get(this.id).getOid() && this.heights.get(this.id).getR() != entry.getValue().getHeights().get(this.id).getR())
            {
                nodes_rl.remove(entry.getKey());
            }
        }

        Map.Entry<Character, MyNode> minEntry = null;

        for (Map.Entry<Character, MyNode> entry : this.neighbors.entrySet())
        {
            if (minEntry == null || entry.getValue().compareTo(minEntry.getValue()) < 0)
            {
                minEntry = entry;
            }
        }

        this.heights.get(this.id).setDelta(minEntry.getValue().getHeights().get(this.id).getDelta());
        this.slid = '?';
        this.pred = '?';

        this.lc = this.lc + 1;

    }

    public void startNewRefLevel() {
        this.heights.get(this.id).setTau(this.lc);
        this.heights.get(this.id).setOid(this.getId());
        this.heights.get((this.id)).setR(0);
        this.heights.get((this.id)).setDelta(0);

        this.slid = '?';
        this.pred = '?';

        this.lc = this.lc + 1;
    }

    public void adoptLPIfPriority(MyNode node) {
        if (node.getHeights().get(this.id).getNlts() < this.heights.get(this.id).getNlts()|| node.getHeights().get(this.id).getNlts() == this.heights.get(this.id).getNlts() && node.getHeights().get(node.getId()).getLid() < this.heights.get(this.id).getLid()) {
            this.heights.get(this.id).setTau(node.getHeights().get(this.id).getTau());
            this.heights.get(this.id).setOid(node.getHeights().get(node.getId()).getOid());
            this.heights.get(this.id).setR(node.getHeights().get(node.getId()).getR());

            this.heights.get(this.id).setDelta(node.getHeights().get(node.getId()).getDelta() + 1);

            this.heights.get(this.id).setNlts(node.getHeights().get(node.getId()).getNlts());
            this.heights.get(this.id).setLid(node.getHeights().get(this.id).getLid());

        }
    }

    public boolean sink() {
        //Map.Entry<Integer, Height> minEntry = null;

        for (Map.Entry<Character, MyNode> entry : this.neighbors.entrySet())
        {
            if (entry.getValue().getHeights().get(entry.getKey()).getNlts() != this.heights.get(this.id).getNlts() ||  this.heights.get(this.id).compareTo(entry.getValue().getHeights().get(entry.getKey())) != -1 || this.heights.get(this.id).getLid() != this.getId())
            {
                return false;
            }
        }
        return true;
    }*/

    @Override
    public void onStart() {
        Height h = new Height(0, 0, 0,0, -1,'?');
        SubLeaderPair slp = new SubLeaderPair(this.getID(), '?');
        LogicalClock lc = new LogicalClock(0);
        this.setProperty("height", h);
        this.setProperty("slp", slp);
        this.setProperty("lc", lc);
    }

    @Override
    public void onSelection() {

    }

    @Override
    public int compareTo(MyNode node) {
        Height h1 = (Height) this.getProperty("height");
        Height h2 = (Height) node.getProperty("height");

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