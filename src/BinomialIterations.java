    public class BinomialIterations {
        int iteration;
        double xZero, xOne, xTwo, fxZero, fxOne, functionOfxTwo, marginError;

        public BinomialIterations(int iteration, double xZero, double xOne, double xTwo,
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

        // Getters and setters
        public int getIteration() { return iteration; }
        public void setIteration(int iteration) { this.iteration = iteration; }

        public double getxZero() { return xZero; }
        public void setxZero(double xZero) { this.xZero = xZero; }

        public double getxOne() { return xOne; }
        public void setxOne(double xOne) { this.xOne = xOne; }

        public double getxTwo() { return xTwo; }
        public void setxTwo(double xTwo) { this.xTwo = xTwo; }

        public double getFxZero() { return fxZero; }
        public void setFxZero(double fxZero) { this.fxZero = fxZero; }

        public double getFxOne() { return fxOne; }
        public void setFxOne(double fxOne) { this.fxOne = fxOne; }

        public double getFunctionOfxTwo() { return functionOfxTwo; }
        public void setFunctionOfxTwo(double functionOfxTwo) { this.functionOfxTwo = functionOfxTwo; }

        public double getMarginError() { return marginError; }
        public void setMarginError(double marginError) { this.marginError = marginError; }
    }
