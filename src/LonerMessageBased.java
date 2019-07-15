import io.jbotsim.core.Color;
import io.jbotsim.core.Message;
import io.jbotsim.core.Node;

public class LonerMessageBased extends Node {
    private boolean heardSomeone = false;
    private String l ="hello";

    @Override
    public void onClock(){
        if (heardSomeone){
            setColor(Color.red);
            setLabel("(0,0,0,0,-LC,  ");
        } else {
            setColor(Color.green);
        }
        heardSomeone = false;
        sendAll(new Message());
    }

    @Override
    public void onMessage(Message msg) {
        heardSomeone = true;
    }



}