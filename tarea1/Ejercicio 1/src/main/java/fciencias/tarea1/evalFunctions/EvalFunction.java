package fciencias.tarea1.evalFunctions;

public interface EvalFunction {

    public double evalSoution(double[] param);

    public double partialDerivative(double[] param, int n);

    public double[] gradientFuntion(double[] param);
    
}
