//import com.sun.scenario.effect.impl.sw.java.JSWBlend_HARD_LIGHTPeer;
import io.jbotsim.core.Color;
import io.jbotsim.core.Link;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohamed Tbarka
 * @project Hierarchical Leader Election Algorithm with Remoteness Constraint
 */

public class MyNode extends Node implements Comparable<Node> {
    private boolean hc = false ;
    private int ms = 0;
    private HashMap<Integer, Node> forming = new HashMap<>();
    private HashMap<Integer, Height> heights = new HashMap<>();
    private HashMap<Integer, Node> neighbors = new HashMap<>();
    private Height h;
    private Height message;
    private SubLeaderPair slp;
    private LogicalClock lc;
    private final int D = 3;
    private Height myOldHeight;


    private int nlc;
    /*
        public MyNode() {
            h = new Height(0, 0, 0,0, -1,this.getID());
            SubLeaderPair slp = new SubLeaderPair(this.getID(), -1);
            LogicalClock lc = new LogicalClock(0);
            //this.setProperty("height", h);
            this.setProperty("slp", slp);
            this.setProperty("lc", lc);

            heights.put(this.getID(), h);
            this.setProperty("heights", heights);
            this.setProperty("neighbors", neighbors);
            this.setProperty("forming", forming);
            this.setProperty("hc", hc);

            Label l = new Label("hello");
            this.setLabel(l);
            this.setColor(Color.RED);
        }*/
    @Override
    public void onStart() {
        // System.out.println("hello");
        h = new Height(0, 0, 0,0, -1,this.getID());
        SubLeaderPair slp = new SubLeaderPair(this.getID(), -1);
        LogicalClock lc = new LogicalClock(0);
        //this.setProperty("height", h);
        this.setProperty("slp", slp);
        this.setProperty("lc", lc);

        heights.put(this.getID(), h);
        this.setProperty("heights", heights);
        this.setProperty("neighbors", neighbors);
        this.setProperty("forming", forming);
        this.setProperty("hc", hc);

        Label l = new Label("hello");
        this.setLabel(l);
        this.setColor(Color.RED);
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
        ms++;
        HashMap<Integer, Node> f = (HashMap<Integer, Node>) this.getProperty("forming");
        //link.setColor(Color.RED);
        System.out.println("[+] node "+this.getID()+" has made contact with node : "+f.get(n.getID()).getID());

    }

    @Override

    public void onMessage(Message msg) {


        boolean f = true;

        //this.setColor(Color.getRandomColor());
        System.out.println("We're at the level of node : "+this.getID());

        message = (Height) msg.getContent();
        Node n = msg.getSender();
        nlc = ((LogicalClock)n.getProperty("lc")).getLc();

        heights = (HashMap<Integer, Height>) this.getProperty("heights");
        forming = (HashMap<Integer, Node>) this.getProperty("forming");
        neighbors = (HashMap<Integer, Node>)this.getProperty("neighbors");

        h = heights.get(this.getID());

        forming.remove(n.getID());


        neighbors.put(n.getID(), n);

        myOldHeight = new Height(h);
        System.out.println("[+] the number of links : "+this.getLinks(true).size());
        //this.getLinks(true);
        //this.getlin
        //this.getCommonLinkWith(n).setColor(null);
        if(this.compareTo(n) == 1)
            this.getOutLinkTo(n).setColor(null);
        else if(this.compareTo(n) == -1)
            this.getInLinkFrom(n).setColor(null);
        // System.out.println(myOldHeight);

        heights.replace(n.getID(), message);
        // System.out.println("-------------------------------");
        // System.out.println("[+] current height : "+h);
        // System.out.print("[+] message received : "+message);

        System.out.println("[+] current height : "+h);

        System.out.println("[+] current message : "+message+" from node "+n.getID());

        if(h.getNlts() == message.getNlts() && h.getLid() == message.getLid()) {
            //System.out.println("we've entered !");
            // System.out.println(neighbors.size());
            // System.out.println();
            if (sink()) {
                Height m = null;
                Height hh = null;
                int flag = 0;

                for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
                    if (m == null)
                        m = ((HashMap<Integer, Height>)(entry.getValue().getProperty("heights"))).get(entry.getKey());
                    else {
                        hh = ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey());
                        if(m.getTau() == hh.getTau() && m.getOid() == hh.getOid() && m.getR() == hh.getR())
                            flag++;
                    }
                }
                // System.out.println(flag);
                if (flag == neighbors.size()-1) {
                    if (m.getTau() > 0 && m.getR() == 0) {
                        reflectRefLevel(n);
                        this.setColor(null);

                    }
                    else if (m.getTau() > 0 && m.getR() == 1 && m.getOid() == this.getID())
                        electself();
                    else {
                        startNewRefLevel();
                        this.setColor(null);

                    }
                } else {
                    prograteLargestRefLevel();
                    this.setColor(null);
                }
            } else
                System.out.println("[+] node "+this.getID()+" has done nothing !");

        } else {
            adoptLPIfPriority(n);
            f=false;
            if(h.getDelta() % D == 0 && h.getDelta() != 0)
                this.setColor(Color.GREEN);
        }
        if(h.getLid() == this.getID())
            this.setColor(Color.RED);





        Label l = new Label("hello");
        this.setLabel(l);
        this.setProperty("lc", lc);
        this.setProperty("slp", slp);
        this.setProperty("heights", heights);
        this.setProperty("neighbors", neighbors);
        this.setProperty("forming", forming);
        // System.out.println(myOldHeight);
        // System.out.println(((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID()));

        // System.out.println(myOldHeight.compareTo(((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID())) != 0);
        // System.out.println(myOldHeight);
        // System.out.println(h);


        if (myOldHeight.compareTo(h) != 0) {

            System.out.println("[+] the height has been changed !");

            if(this.getID() != h.getLid() &&
                    h.getDelta() % D != 0 && h.getDelta() != 0)
                this.setColor(null);

            HashMap<Integer, Node> union = new HashMap<>();
            union.putAll(forming);
            union.putAll(neighbors);
            Message msg_1 = new Message(h);

            for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
                send(entry.getValue(), msg_1);

                if(f)
                    this.getCommonLinkWith(entry.getValue()).setWidth(1);
                // System.out.println("here we go !");

            }
            ms++;
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
        hc=true;
        this.setProperty("hc", hc);

        System.out.println("[+] Node : "+this.getID()+" executes ELECTSELF");
        heights = (HashMap<Integer, Height>)this.getProperty("heights");
        h = heights.get(this.getID());
        slp = (SubLeaderPair)this.getProperty("slp");
        lc = (LogicalClock)this.getProperty("lc");

        h.setTau(0);
        h.setOid(0);
        h.setR(0);

        h.setNlts(-lc.getLc());
        h.setLid(this.getID());
        slp.setSlid(this.getID());
        slp.setPred(-1);

        if(nlc < lc.getLc())
            lc.setLc(lc.getLc() + 1);
        else
            lc.setLc(nlc + 1);
        heights.replace(this.getID(), h);
    }

    public void reflectRefLevel(Node n) {
        hc=true;
        this.setProperty("hc", hc);

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
        if(nlc < lc.getLc())
            lc.setLc(lc.getLc() + 1);
        else
            lc.setLc(nlc + 1);
        heights.replace(this.getID(), h);
        slp.setSlid(-1);
        slp.setSlid(-1);

    }

    public void prograteLargestRefLevel() {
        hc=true;
        this.setProperty("hc", hc);

        System.out.println("[+] Node : "+this.getID()+" executes PROPAGATELARGESTREFLEVEL");

        //List<Node> nodes = node.getNeighbors();

        HashMap<Integer, Node> ns = new HashMap<>();

        for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
            ns.put(entry.getKey(), entry.getValue());
        }


        Height maxEntry = null;

        for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
            Height he = ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey());
            // System.out.println("list of neighbors : \n"+he);
            if (maxEntry == null || he.getTau() > maxEntry.getTau() || he.getTau() == maxEntry.getTau() && he.getOid() > maxEntry.getOid() || he.getTau() == maxEntry.getTau() && he.getOid() == maxEntry.getOid() && he.getR() > maxEntry.getR())
            {
                maxEntry = he;
            }
        }

        // System.out.println("max entry : "+maxEntry);
        h.setTau(maxEntry.getTau());
        h.setOid(maxEntry.getOid());
        h.setR(maxEntry.getR());
        //Node n1 = new Node();
        //Node n2 = new Node();
        //Link l = new Link(n1, n2, Link.Type.DIRECTED);
        //l.type = Link.Type.DIRECTED;


        //Map.Entry<Integer, Height> minEntry = null;
        // System.out.println(ns.size());

        for (Map.Entry<Integer, Node> entry : neighbors.entrySet())
        {
            Height he = ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey());
            // System.out.println("-------");
            // System.out.println(he);
            if (h.getTau() != he.getTau() || h.getOid() != he.getOid() || h.getR() != he.getR())
            {
                ns.remove(entry.getKey());
            }
        }

        // System.out.println(neighbors.size());
        // System.out.println(ns.size());
        Height minEntry = null;

        for (Map.Entry<Integer, Node> entry : ns.entrySet())
        {
            Height he = ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(entry.getKey());

            if (minEntry == null || he.getTau() > maxEntry.getTau() || he.getTau() == minEntry.getTau() && he.getOid() > minEntry.getOid() || he.getTau() == minEntry.getTau() && he.getOid() > minEntry.getOid() && he.getR() > minEntry.getR())
            {
                minEntry = he;
            }
        }
        // System.out.println(minEntry);
        h.setDelta(minEntry.getDelta() - 1);

        slp.setSlid('?');
        slp.setPred('?');

        if(nlc < lc.getLc())
            lc.setLc(lc.getLc() + 1);
        else
            lc.setLc(nlc + 1);
        heights.replace(this.getID(), h);

        slp.setSlid(-1);
        slp.setSlid(-1);
        // System.out.println("hello world !");
        // System.out.println("current height : "+h);
        // System.out.println(h);
    }

    public void startNewRefLevel() {
        hc=true;
        this.setProperty("hc", hc);

        System.out.println("[+] Node : "+this.getID()+" executes STARTNEWREFLEVEL");
        // System.out.println(neighbors.size());

        heights = (HashMap<Integer, Height>) this.getProperty("heights");
        h = heights.get(this.getID());
        slp = (SubLeaderPair)this.getProperty("slp");
        lc = (LogicalClock)this.getProperty("lc");
        h.setTau(lc.getLc());
        h.setOid(this.getID());
        h.setR(0);
        h.setDelta(0);

        slp.setSlid(-1);
        slp.setSlid(-1);

        if(nlc < lc.getLc())
            lc.setLc(lc.getLc() + 1);
        else
            lc.setLc(nlc + 1);
        heights.replace(this.getID(), h);

    }

    public void adoptLPIfPriority(Node n) {
        hc=true;
        this.setProperty("hc", hc);

        System.out.println("[+] Node : " + this.getID() + " executes ADOPTLPIFPRIORITY");

        // System.out.println("we've entered !---");
        //Height height = ((HashMap<Integer, Height>) this.getProperty("heights")).get(this.getID());

        SubLeaderPair nslp = ((SubLeaderPair) n.getProperty("slp"));
        // System.out.println(message);


        if (message.getNlts() < h.getNlts() || message.getNlts() == h.getNlts() && message.getLid() < h.getLid() || message.getNlts() == h.getNlts() && message.getLid() == h.getLid() && n.getID() < slp.getPred()) {
            h.setTau(message.getTau());
            h.setOid(message.getOid());
            h.setR(message.getR());

            h.setDelta(message.getDelta() + 1);

            h.setNlts(message.getNlts());
            h.setLid(message.getLid());
            if (message.getDelta() % D != 0 && message.getDelta() != 0) {
                slp.setSlid(nslp.getSlid());
                slp.setPred(n.getID());
                System.out.println("[+] delta is not kD");

            } else if (message.getDelta() == 0) {
                slp.setSlid(n.getID());
                slp.setPred(n.getID());
                System.out.println("[+] delta is 0");
                System.out.println(n.getID());
            } else {
                slp.setPred(n.getID());
                slp.setSlid(n.getID());
                System.out.println("[+] delta is kD");
            }
            Link l = this.getCommonLinkWith(n);
            l.setWidth(3);
            for(Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
                if(n.getID() != entry.getValue().getID()) {
                    this.getCommonLinkWith(entry.getValue()).setWidth(1);
                    //this.getCommonLinkWith(entry.getValue()).type = Link.Type.DIRECTED;
                    System.out.println("hello");
                }
            }
        }
        // System.out.println("[+] "+myOldHeight);
        heights.replace(this.getID(), h);
        // System.out.println("[+] "+myOldHeight);
        if (nlc < lc.getLc())
            lc.setLc(lc.getLc() + 1);
        else
            lc.setLc(nlc + 1);
        System.out.println("[+] current message : "+message);
/*
        Node minEntry = null;

        Height hn = ((HashMap<Integer, Height>)n.getProperty("heights")).get(n.getID());
        System.out.println("We're in the min loop !");
        for (Map.Entry<Integer, Node> entry : neighbors.entrySet())
        {
            Height hentry = ((HashMap<Integer, Height>)entry.getValue().getProperty("heights")).get(entry.getValue().getID());
            SubLeaderPair slpentry = ((SubLeaderPair)entry.getValue().getProperty("slp"));

            System.out.println(hentry);

            if(hentry.getDelta() != hn.getDelta())
                continue;

            if (minEntry == null && hentry.getDelta() == hn.getDelta() || entry.getValue().compareTo(minEntry) == -1  && hn.getDelta() == hentry.getDelta())
            {

                minEntry = entry.getValue();
            }
        }*/


        /*
        SubLeaderPair minslp = ((SubLeaderPair)minEntry.getProperty("slp"));

        System.out.println("[+] the min entry : "+minEntry.getID());


        if (message.getDelta() % D != 0 && message.getDelta() != 0) {
            slp.setSlid(minslp.getSlid());
            slp.setPred(minEntry.getID());
            System.out.println("[+] delta is not kD");

        } else if (message.getDelta() == 0) {
            slp.setSlid(minEntry.getID());
            slp.setPred(minEntry.getID());
            System.out.println("[+] delta is 0");
            System.out.println(minEntry.getID());
        } else {
            slp.setPred(minEntry.getID());
            slp.setSlid(minEntry.getID());
            System.out.println("[+] delta is kD");

        }*/



        // System.out.println(neighbors.size());
    }

    public boolean sink() {
        // System.out.println("[+] SINK has been executed !");

        for (Map.Entry<Integer, Node> entry : neighbors.entrySet())
        {
            Height height1 = ((HashMap<Integer, Height>)entry.getValue().getProperty("heights")).get(entry.getValue().getID());
            // System.out.println(h);
            // System.out.println(height1);
            // System.out.println(this.compareTo(entry.getValue()) == -1);
            if (height1.getNlts() != h.getNlts() || height1.getLid() != h.getLid() ||  this.compareTo(entry.getValue()) == 1 || h.getLid() == this.getID())
                return false;
        }
        return true;
    }

    @Override
    public void onLinkRemoved(Link link) {
        System.out.println("[+] one of node's " + this.getID() + " links has gone down !");
        heights = (HashMap<Integer, Height>) this.getProperty("heights");
        forming = (HashMap<Integer, Node>) this.getProperty("forming");
        neighbors = (HashMap<Integer, Node>) this.getProperty("neighbors");
        slp = (SubLeaderPair) this.getProperty("slp");
        lc = (LogicalClock) this.getProperty("lc");
        h = heights.get(this.getID());
        // System.out.println("hello");
        Node n = link.getOtherEndpoint(this);
        forming.remove(n.getID());
        neighbors.remove(n.getID());
        if (neighbors.size() == 0) {
            electself();
            Message message = new Message(h);

            for (Map.Entry<Integer, Node> entry : forming.entrySet()) {
                send(entry.getValue(), message);
            }
            ms++;

        } else if (sink()) {
            startNewRefLevel();
            HashMap<Integer, Node> union = new HashMap<>();

            union.putAll(forming);
            union.putAll(neighbors);

            for (Map.Entry<Integer, Node> entry : union.entrySet()) {
                // System.out.println("Joker - "+h);
                Message message = new Message(h);
                send(entry.getValue(), message);
            }
            ms++;
        } else if (slp.getPred() == n.getID() && slp.getSlid() != n.getID()) {
            Node minEntry = null;

            Height hn = ((HashMap<Integer, Height>) n.getProperty("heights")).get(n.getID());

            for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
                Height hentry = ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(n.getID());

                if (minEntry == null || entry.getValue().compareTo(minEntry) == -1 && hn.getDelta() == hentry.getDelta()) {
                    minEntry = entry.getValue();
                }
            }

            slp.setSlid(((SubLeaderPair) minEntry.getProperty("slp")).getSlid());
            slp.setPred(minEntry.getID());
            this.getCommonLinkWith(minEntry).setWidth(3);
        } else if (slp.getPred() == n.getID() && slp.getSlid() == n.getID() && h.getLid() != n.getID()) {
            Node minEntry = null;

            Height hn = ((HashMap<Integer, Height>) n.getProperty("heights")).get(n.getID());

            for (Map.Entry<Integer, Node> entry : neighbors.entrySet()) {
                Height hentry = ((HashMap<Integer, Height>) entry.getValue().getProperty("heights")).get(n.getID());

                if (minEntry == null || entry.getValue().compareTo(minEntry) == -1 && hn.getDelta() == hentry.getDelta()) {
                    minEntry = entry.getValue();
                }
            }

            slp.setSlid(minEntry.getID());
            slp.setPred(minEntry.getID());
            this.getCommonLinkWith(minEntry).setWidth(3);
        }


        if (h.getLid() == this.getID())
            this.setColor(Color.RED);
        else
            this.setColor(null);
        Label l = new Label("hello");
        this.setLabel(l);
        this.setProperty("lc", lc);
        this.setProperty("slp", slp);
        this.setProperty("heights", heights);
        this.setProperty("neighbors", neighbors);
        this.setProperty("forming", forming);

    }


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