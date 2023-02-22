package fciencias;

import java.util.Date;
import java.util.Random;

import fciencias.tarea1.binaryRepresentation.BinaryState;
import fciencias.tarea1.evalFunctions.AckleyFunction;
import fciencias.tarea1.evalFunctions.EvalFunction;
import fciencias.tarea1.evalFunctions.GriewankFunction;
import fciencias.tarea1.evalFunctions.SphereFunction;
import fciencias.tarea1.evalFunctions.ThenthPowerFunction;

/**
 * Hello world!
 *
 */
public class RandomOptimization implements Runnable
{

    private EvalFunction evalFunction;

    private double[] interval;

    private int iterations;

    private int representationalBits;

    private int dimension;

    

    public RandomOptimization(EvalFunction evalFunction, double[] interval, int iterations, int representationalBits,int dimension) {
        this.evalFunction = evalFunction;
        this.interval = interval;
        this.iterations = iterations;
        this.representationalBits = representationalBits;
        this.dimension = dimension;
    }

    public EvalFunction getEvalFunction() {
        return evalFunction;
    }

    public void setEvalFunction(EvalFunction evalFunction) {
        this.evalFunction = evalFunction;
    }

    public double[] getInterval() {
        return interval;
    }

    public void setInterval(double[] interval) {
        this.interval = interval;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getRepresentationalBits() {
        return representationalBits;
    }

    public void setRepresentationalBits(int representationalBits) {
        this.representationalBits = representationalBits;
    }

    public double[] getRandomVector(double[] interval,int n)
    {
        Date date = new Date();
        long time = date.getTime();
        Random rand = new Random(time);

        double[] randVector = new double[n];
        for(int i = 0; i < n; i ++)
        {
            randVector[i] = rand.nextDouble()*(interval[1] - interval[0]) + interval[0];
        }
        return randVector;
    }

    public BinaryState getRandomState(double[] interval, int n, int bits)
    {
        return new BinaryState(getRandomVector(interval, n),bits, interval);
    }

    public BinaryState optimize()
    {
        BinaryState optimunBinaryState = getRandomState(interval,dimension,representationalBits);
        double minValue = evalFunction.evalSoution(optimunBinaryState.getRealValue());

        for(int i = 0; i < iterations; i ++)
        {
            BinaryState binaryState = getRandomState(interval,dimension,representationalBits);
            double currentStateValue = evalFunction.evalSoution(binaryState.getRealValue()); 
            if(currentStateValue < minValue)
            {
                optimunBinaryState = binaryState;
                minValue = currentStateValue;
                System.out.println(minValue);
            }

        }

        return optimunBinaryState;
    }

    public static void main( String[] args )
    {
        RandomOptimization randomOptimization = new RandomOptimization(new AckleyFunction(), new double[]{-5.12,5.12}, 500000000, 90,3);
        BinaryState binaryState = randomOptimization.optimize();
        System.out.println(binaryState + "\n" + new SphereFunction().evalSoution(binaryState.getRealValue()));
    }

    @Override
    public void run() {
        
        BinaryState binaryState = optimize();
    }

}


