public class SimpsonIteration {
    int index;
    String label;
    double x;
    double fx;
    int mod;
    int simpsonRule;
    double fxi;

    public SimpsonIteration(int index, String label, double x, double fx, int mod, int simpsonRule, double fxi) {
        this.index       = index;
        this.label       = label;
        this.x           = x;
        this.fx          = fx;
        this.mod         = mod;
        this.simpsonRule = simpsonRule;
        this.fxi         = fxi;
    }

    public int    getIndex()       { return index; }
    public String getLabel()       { return label; }
    public double getX()           { return x; }
    public double getFx()          { return fx; }
    public int    getMod()         { return mod; }
    public int    getSimpsonRule() { return simpsonRule; }
    public double getFxi()         { return fxi; }
}