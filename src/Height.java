import io.jbotsim.core.Node;

public class Height  implements Comparable<Height>{
    private int tau;
    private  int oid;

    public Height(int tau, int oid, int r, int delta, int nlts, int lid) {
        this.tau = tau;
        this.oid = oid;
        this.r = r;
        this.delta = delta;
        this.nlts = nlts;
        this.lid = lid;
    }

    private int r;

    private int delta;

    private int nlts;
    private int lid;

    public void setLid(int lid) {
        this.lid = lid;
    }



    // private char id;

    public int getTau() {
        return tau;
    }

    public void setTau(int tau) {
        this.tau = tau;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public int getNlts() {
        return nlts;
    }

    public void setNlts(int nlts) {
        this.nlts = nlts;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(char lid) {
        this.lid = lid;
    }


    public Height(int tau, int oid, int r, int delta, int nlts, char lid) {
        this.tau = tau;
        this.oid = oid;
        this.r = r;
        this.delta = delta;
        this.nlts = nlts;
        this.lid = lid;
        //this.id = id;
    }

    @Override
    public String toString() {
        return "Height = ("+this.tau+", "+this.oid+", "+this.r+", "+this.delta+", "+this.nlts+", "+this.lid+", ";
    }
    
    @Override

    public int compareTo(Height h2) {
        if (this.getTau() > h2.getTau()) {
            return 1;
        } else if (this.getTau() < h2.getTau()) {
            return -1;
        } else {
            if (this.getOid()> h2.getOid()) {
                return 1;
            } else if (this.getOid()< h2.getOid()) {
                return -1;
            } else {
                if (this.getR() > h2.getR()) {
                    return 1;
                } else if (this.getR() < h2.getR()) {
                    return -1;
                } else {
                    if (this.getDelta() > h2.getDelta()) {
                        return 1;
                    } else if (this.getDelta() < h2.getDelta()) {
                        return -1;
                    } else {
                        if (this.getNlts() > h2.getNlts()) {
                            return 1;
                        } else if (this.getNlts() < h2.getNlts()) {
                            return -1;
                        } else {
                            if (this.getLid() > h2.getLid()) {
                                return 1;
                            } else if (this.getLid() < h2.getLid()) {
                                return -1;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }
}
