import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;
import java.util.Collections;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.scene.input.KeyCode.V;


public class MyNode extends Node {
    //private boolean heardSomeone = false;
    // here we gonna put the 7-tuple and the sub-leader pair.
    private Height height;

    private char slid;
    private char pred;

    private int lc;
    private boolean heightChanged;

    private HashMap<Integer, Height> neighbors;
    private HashMap<Integer, Height> forming;

    public MyNode(Height height, char slid, char pred, int lc, int d) {
        this.height = height;
        this.slid = slid;
        this.pred = pred;
        this.lc = lc;
        if(height.getDelta() % d == 0)
            this.setColor(Color.orange);
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public char getSlid() {
        return slid;
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
    public void onMessage(Message msg) {

        /*heardSomeone = true;*/
    }


    @Override
    public void setLabel(Object label) {
        super.setLabel("(" + height.getTau() + ", " + height.getOid() + ", " + height.getR() + ", " + height.getDelta() + ", " + height.getNlts() + ", " + height.getLid() + ", " + height.getId() + ") - (" + this.slid + ", " + this.pred + ") - LC : " + this.lc);
    }

    @Override
    public void onLinkRemoved(Link link) {
        Node n = link.getOtherEndpoint(this);
        MyNode nn = (MyNode) n;
        this.forming.remove(nn.getID());
        this.neighbors.remove(nn.getID());
        if(this.neighbors == null) {
            electself();
            Message message = new Message(this.getHeight());

            for (Map.Entry<Integer, Height> entry : this.forming.entrySet())
            {
                send(this.getNeighbors().get(entry.getKey()), message);
            }
        } else if(sink()) {
            startNewRefLevel();
            Map<Integer, Height> union = new HashMap<Integer, Height>(); ;
            union.putAll(this.neighbors);
            union.putAll(this.forming);


            for (Map.Entry<Integer, Height> entry : union.entrySet())
            {
                Message message = new Message(this.getHeight());
                send(this.getNeighbors().get(entry.getKey()), message);
            }
        }
        super.onLinkRemoved(link);
    }

    @Override
    public void onLinkAdded(Link link) {
        Node n = link.getOtherEndpoint(this);
        MyNode nn = (MyNode) n;
        this.forming.put(nn.getID(), nn.getHeight());
        Message message = new Message(nn.getHeight());
        send(nn, message);
        super.onLinkAdded(link);

    }

    public void electself() {
        height.setTau(0);
        height.setOid(0);
        height.setR(0);

        height.setNlts(this.getLc());
        height.setLid(height.getId());
        this.setSlid('?');
        this.setPred('?');

        this.setLc(this.getLc() + 1);
    }

    public void reflectRefLevel(MyNode n) {
        height.setTau(n.height.getTau());
        height.setOid(n.height.getOid());
        height.setR(1);
        height.setDelta(0);

        this.setSlid('?');
        this.setPred('?');

        this.setLc(this.getLc() + 1);
    }

    public void prograteLargestRefLevel(MyNode node) {
        //List<Node> nodes = node.getNeighbors();


        HashMap<Integer, Height> nodes_rl = neighbors;


        Map.Entry<Integer, Height> maxEntry = null;

        for (Map.Entry<Integer, Height> entry : neighbors.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        height.setTau(maxEntry.getValue().getTau());
        height.setOid(maxEntry.getValue().getOid());

        //Map.Entry<Integer, Height> minEntry = null;

        for (Map.Entry<Integer, Height> entry : nodes_rl.entrySet())
        {
            if (this.height.getTau() != entry.getValue().getTau() && this.height.getOid() != entry.getValue().getOid() && this.height.getR() != entry.getValue().getR())
            {
                nodes_rl.remove(entry.getKey());
            }
        }

        Map.Entry<Integer, Height> minEntry = null;

        for (Map.Entry<Integer, Height> entry : neighbors.entrySet())
        {
            if (minEntry == null || entry.getValue().compareTo(maxEntry.getValue()) < 0)
            {
                minEntry = entry;
            }
        }

        this.height.setDelta(minEntry.getValue().getDelta());
        this.slid = '?';
        this.pred = '?';

        node.lc = node.lc + 1;

    }

    public void startNewRefLevel() {
        this.height.setTau(this.lc);
        this.height.setOid(this.height.getId());
        this.height.setR(0);
        this.height.setDelta(0);

        this.slid = '?';
        this.pred = '?';

        this.lc = this.lc + 1;
    }

    public void adoptLPIfPriority(MyNode node) {
        if (node.height.getNlts() < this.height.getNlts()|| node.height.getNlts() == this.height.getNlts() && node.height.getLid() < this.height.getLid()) {
            this.height.setTau(node.height.getTau());
            this.height.setOid(node.getHeight().getOid());
            this.height.setR(node.getHeight().getR());

            this.height.setDelta(node.getHeight().getDelta() + 1);

            this.height.setNlts(node.getHeight().getNlts());
            this.height.setLid(node.getHeight().getLid());

        }
    }

    public boolean sink() {
        //Map.Entry<Integer, Height> minEntry = null;

        for (Map.Entry<Integer, Height> entry : neighbors.entrySet())
        {
            if (entry.getValue().getNlts() != this.height.getNlts() ||  this.height.compareTo(entry.getValue()) != -1 || this.height.getLid() != this.height.getId())
            {
                return false;
            }
        }
        return true;
    }



}
