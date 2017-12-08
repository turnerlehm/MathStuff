package gentzen;

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
        if(vars.length < 2)
        {
            if(vars.length == 1)
                return this.evaluate(vars[0]);
            else
                throw new IllegalArgumentException("vars length < 1");
        }
        And a = new And();
        a.setEval(vars[0] && vars[1]);
        for(int i = 0; i < vars.length - 1; i++)
            a.setEval(a.getEvaluation() && vars[i] && vars[i + 1]);
        return a;
    }

    @Override
    public Gate evaluate(Gate x1, Gate x2)
    {
        if(x1 == null || x2 == null)
        {
            if(x1 == null)
                return this.evaluate(x2);
            else if(x2 == null)
                return this.evaluate(x1);
            else
                throw new NullPointerException("Gates x1 and x2 are null");
        }
        And a = new And();
        a.setEval(x1.getEvaluation() && x2.getEvaluation());
        return a;
    }

    @Override
    public Gate evaluate(Gate x)
    {
        if(x == null)
            throw new NullPointerException("Gate x is null");
        And a = new And();
        a.setEval(x.getEvaluation() && x.getEvaluation());
        return a;
    }

    @Override
    public Gate evaluate(Gate[] gates)
    {
        if(gates.length < 2)
        {
            if(gates.length == 1)
                return this.evaluate(gates[0]);
            else
                throw new IllegalArgumentException("gates length < 1");
        }
        And a = new And();
        a.setEval(gates[0].getEvaluation() && gates[1].getEvaluation());
        for(int i = 0; i < gates.length - 1; i++)
            a.setEval(a.getEvaluation() && gates[i].getEvaluation() && gates[i + 1].getEvaluation());
        return a;
    }
}
