import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author mohamed
 * @project Hierarchical Leader Election Algorithm with Remoteness Constraint
 */

public class LonerMessageBased_1 extends Node {
    //private boolean heardSomeone = false;
    // here we gonna put the 7-tuple and the sub-leader pair.
    //private Height height;
    private char id;
    private char slid;
    private char pred;

    private int lc;
    private boolean heightChanged;

    private HashMap<Character, MyNode> neighbors;
    private HashMap<Character, MyNode> forming;

    private HashMap<Character, Height> heights;



    public LonerMessageBased_1(char id, HashMap<Character, Height> heights, char slid, char pred, int lc, int d) {

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

    public LonerMessageBased_1() {
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

    }



    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }

    public HashMap<Character, Height> getHeights() {
        return heights;
    }

    public void setHeights(HashMap<Character, Height> heights) {
        this.heights = heights;
    }

    public char getSlid() {
        return slid;
    }


    public HashMap<Character, MyNode> getNs() {
        return neighbors;
    }

    public void setNs(HashMap<Character, MyNode> neighbors) {
        this.neighbors = neighbors;
    }

    public HashMap<Character, MyNode> getForming() {
        return forming;
    }

    public void setForming(HashMap<Character, MyNode> forming) {
        this.forming = forming;
    }

    public void setSlid(char slid) {
        this.slid = slid;
    }

    public char getPred() {
        return pred;
    }

    public void setPred(char pred) {
        this.pred = pred;
    }

    public int getLc() {
        return lc;
    }

    public void setLc(int lc) {
        this.lc = lc;
    }


    @Override
    public void setLabel(Object label) {
        super.setLabel("("+ this.heights.get(this.id).getTau()+ ", " + this.heights.get(this.id).getOid() + ", " + this.heights.get(this.id).getR() + ", " + this.heights.get(this.id).getDelta() + ", " + this.heights.get(this.id).getNlts() + ", " + this.heights.get(this.id).getLid() + ", " + this.heights.get(this.id).getId()+ ") - (" + this.slid + ", " + this.pred + ") - LC : " + this.lc);
    }

    @Override
    public void onLinkRemoved(Link link) {
        System.out.println("hello");
        Node n = link.getOtherEndpoint(this);
        MyNode nn = (MyNode) n;
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
        MyNode n = (MyNode)msg.getSender();
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
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onSelection() {

    }

}