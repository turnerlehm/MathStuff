package logic;

public class And extends Gate
{

    public And()
    {
        this.binary = true;
    }
    @Override
    public Gate evaluate(boolean x1, boolean x2)
    {
        And a = new And();
        a.setEval(x1 && x2);
        return a;
    }

    @Override
    public Gate evaluate(boolean x1)
    {
        return this.evaluate(x1, x1);
    }

    @Override
    public Gate evaluate(boolean[] vars)
    {
        And a = new And();
        for(int i = 0; i < vars.length - 1; i++)
            a.setEval(vars[i] && vars[i + 1]);
        return a;
    }

    @Override
    public Gate evaluate(Gate x1, Gate x2)
    {
        And a = new And();
        a.setEval(x1.getEvaluation() && x2.getEvaluation());
        return a;
    }

    @Override
    public Gate evaluate(Gate x)
    {
        And a = new And();
        a.setEval(x.getEvaluation() && x.getEvaluation());
        return a;
    }

    @Override
    public Gate evaluate(Gate[] gates)
    {
        And a = new And();
        for(int i = 0; i < gates.length - 1; i++)
            a.setEval(gates[i].getEvaluation() && gates[i + 1].getEvaluation());
        return a;
    }
}
