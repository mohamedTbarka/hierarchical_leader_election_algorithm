import io.jbotsim.core.Node;

public class Height implements Comparable<Height> {
    private int tau;
    private  int oid;
    private int r;

    private int delta;
    private int nlts;

    private char lid;
    private char id;

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

    public char getLid() {
        return lid;
    }

    public void setLid(char lid) {
        this.lid = lid;
    }

    public char getId() {
        return id;
    }

    public void setId(char id) {
        this.id = id;
    }

    public Height(int tau, int oid, int r, int delta, int nlts, char lid, char id) {
        this.tau = tau;
        this.oid = oid;
        this.r = r;
        this.delta = delta;
        this.nlts = nlts;
        this.lid = lid;
        this.id = id;
    }
    @Override
    public int compareTo(Height height) {

        if (this.tau > height.getTau()) {
            return 1;
        } else if (this.tau < height.getTau()) {
            return -1;
        } else {
            if (this.oid > height.getOid()) {
                return 1;
            } else if (this.oid < height.getOid()) {
                return -1;
            } else {
                if (this.r > height.getR()) {
                    return 1;
                } else if (this.r < height.getR()) {
                    return -1;
                } else {
                    if (this.delta > height.getDelta()) {
                        return 1;
                    } else if (this.delta < height.getDelta()) {
                        return -1;
                    } else {
                        if (this.nlts > height.getNlts()) {
                            return 1;
                        } else if (this.nlts < height.getNlts()) {
                            return -1;
                        } else {
                            if (this.lid > height.getLid()) {
                                return 1;
                            } else if (this.lid < height.getLid()) {
                                return -1;
                            } else {
                                if (this.id> height.getId()) {
                                    return 1;
                                } else if (this.id < height.getId()) {
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
