package com.fciencias.evolutivo.evalFunctions;

public interface EvalFunction {

    public double evalSoution(double[] param);

    public double partialDerivative(double[] param, int n);

    public double[] gradientFuntion(double[] param);

    public String getFunctionName();
    
}
