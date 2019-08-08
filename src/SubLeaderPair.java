/**
 * @author mohamed
 * @project IntelliJ IDEA
 */
public class SubLeaderPair {
    private int slid;
    private int pred;

    public int getSlid() {
        return slid;
    }

    public void setSlid(int slid) {
        this.slid = slid;
    }

    public int getPred() {
        return pred;
    }

    public SubLeaderPair(int slid, int pred) {
        this.slid = slid;
        this.pred = pred;
    }

    public void setPred(int pred) {
        this.pred = pred;
    }

    @Override
    public String toString() {
        return " - SLP = ("+this.slid+", "+this.pred+")";
    }
}
