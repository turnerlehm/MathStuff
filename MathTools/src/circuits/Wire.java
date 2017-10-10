package circuits;

import java.util.TreeSet;

public class Wire
{
    private State current;
    //private TreeSet<Wire> branches_from;
    private TreeSet<Wire> branches_to;

    public Wire(State start)
    {
        this.current = start;
        branches_to = new TreeSet<Wire>();
    }

    //make a new wire that is initially off
    public Wire()
    {
        this.current = State.LOW;
        branches_to = new TreeSet<Wire>();
    }

    public void toggle()
    {
        current = current == State.HIGH ? State.LOW : State.HIGH;
        for(Wire w : branches_to)
            w.toggle();
    }

    //probe the wire to see if its HIGH or LOW
    public State probe()
    {
        return current;
    }
}
