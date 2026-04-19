public class SecantIteration {
    int iteration;
    double xZero, xOne, xTwo, fxZero, fxOne, functionOfxTwo, marginError;

    public SecantIteration(int iteration, double xZero, double xOne, double xTwo,
                           double fxZero, double fxOne, double functionOfxTwo, double marginError) {
        this.iteration = iteration;
        this.xZero = xZero;
        this.xOne = xOne;
        this.xTwo = xTwo;
        this.fxZero = fxZero;
        this.fxOne = fxOne;
        this.functionOfxTwo = functionOfxTwo;
        this.marginError = marginError;
    }

    public int getIteration() { return iteration; }
    public double getxZero() { return xZero; }
    public double getxOne() { return xOne; }
    public double getxTwo() { return xTwo; }
    public double getFxZero() { return fxZero; }
    public double getFxOne() { return fxOne; }
    public double getFunctionOfxTwo() { return functionOfxTwo; }
    public double getMarginError() { return marginError; }
}