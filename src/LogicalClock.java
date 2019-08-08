import io.jbotsim.core.Clock;

/**
 * @author mohamed
 * @project IntelliJ IDEA
 */
public class LogicalClock {
    private int lc;

    public int getLc() {
        return lc;
    }

    public LogicalClock(int lc) {
        this.lc = lc;
    }

    public void setLc(int lc) {
        this.lc = lc;
    }

    @Override
    public String toString() {
        return " - LC = "+this.lc;
    }
}
