import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.ui.painting.UIComponent;
import io.jbotsim.ui.painting.BackgroundPainter;

import java.awt.*;

public class MyBackgroundPainter implements BackgroundPainter {
    @Override
    public void paintBackground(UIComponent c, Topology tp) {
        Graphics2D g = (Graphics2D) c.getComponent();
        g.setColor(Color.gray);
        for (int i = 0; i < tp.getWidth(); i++){
            for (int j = 0; j < tp.getHeight(); j++){
                g.drawLine(i*50, 0, i*50, tp.getHeight());
                g.drawLine(0, j*50, tp.getWidth(), j*50);
            }
        }
    }
    public static void main(String[] args) {
        Topology tp = new Topology();
        JViewer jv = new JViewer(tp);
        jv.getJTopology().addBackgroundPainter(new MyBackgroundPainter());
        tp.start();
    }

}