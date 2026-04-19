public class NewtonIteration {
    int iteration;
    double xZero, xOne, fxZero, derivativeFxZero, marginError;

    public NewtonIteration(int iteration, double xZero, double xOne,
                           double fxZero, double derivativeFxZero, double marginError) {
        this.iteration = iteration;
        this.xZero = xZero;
        this.xOne = xOne;
        this.fxZero = fxZero;
        this.derivativeFxZero = derivativeFxZero;
        this.marginError = marginError;
    }

    public int getIteration() { return iteration; }
    public double getxZero() { return xZero; }
    public double getxOne() { return xOne; }
    public double getFxZero() { return fxZero; }
    public double getDerivativeFxZero() { return derivativeFxZero; }
    public double getMarginError() { return marginError; }
}