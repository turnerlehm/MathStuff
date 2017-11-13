package logic;

public abstract class Gate
{
    private boolean lastEval;
    protected boolean binary;
    private boolean eval;

    public abstract Gate evaluate(boolean x1, boolean x2);
    public abstract Gate evaluate(boolean x);
    public abstract Gate evaluate(boolean[] vars);
    public abstract Gate evaluate(Gate x1, Gate x2);
    public abstract Gate evaluate(Gate x);
    public abstract Gate evaluate(Gate[] gates);
    public boolean getEvaluation(){return this.eval;}
    protected void setEval(boolean res){this.eval = res;}
}
