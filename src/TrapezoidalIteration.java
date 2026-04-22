public class TrapezoidalIteration {
    int index;
    String label;
    double x;
    double fx;
    int trapRule;
    double fxi;

    public TrapezoidalIteration(int index, String label, double x, double fx, int trapRule, double fxi) {
        this.index = index;
        this.label = label;
        this.x = x;
        this.fx = fx;
        this.trapRule = trapRule;
        this.fxi = fxi;
    }

    public int getIndex()    { return index; }
    public String getLabel() { return label; }
    public double getX()     { return x; }
    public double getFx()    { return fx; }
    public int getTrapRule() { return trapRule; }
    public double getFxi()   { return fxi; }
}