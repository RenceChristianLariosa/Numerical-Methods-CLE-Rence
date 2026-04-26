public class GaussSeidelIteration {
    int iteration;
    double x1, x2, x3;
    double newX1, newX2, newX3;
    double ea1, ea2, ea3;

    public GaussSeidelIteration(int iteration, double x1, double x2, double x3,
                                double newX1, double newX2, double newX3,
                                double ea1, double ea2, double ea3) {
        this.iteration = iteration;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.newX1 = newX1;
        this.newX2 = newX2;
        this.newX3 = newX3;
        this.ea1 = ea1;
        this.ea2 = ea2;
        this.ea3 = ea3;
    }

    public int getIteration() { return iteration; }
    public double getX1() { return x1; }
    public double getX2() { return x2; }
    public double getX3() { return x3; }
    public double getNewX1() { return newX1; }
    public double getNewX2() { return newX2; }
    public double getNewX3() { return newX3; }
    public double getEa1() { return ea1; }
    public double getEa2() { return ea2; }
    public double getEa3() { return ea3; }
}