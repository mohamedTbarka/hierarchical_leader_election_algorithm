import io.jbotsim.core.Message;
import io.jbotsim.core.Color;
import io.jbotsim.core.Node;
import io.jbotsim.core.Link.Type;

public class BroadcastNode extends Node {

    boolean informed;




    @Override
    public void onStart() {
        informed = false;
        setColor(null);
    }

    @Override
    public void onSelection() {
        informed = true;
        setColor(Color.RED);
        sendAll(new Message("My message"));
    }

    @Override
    public void onMessage(Message message) {
        if (! informed) {
            informed = true;
            setColor(Color.RED);
            sendAll(new Message(message.getContent()));
        }
    }
}