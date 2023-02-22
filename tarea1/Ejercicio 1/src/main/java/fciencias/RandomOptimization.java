package fciencias;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import fciencias.tarea1.binaryRepresentation.BinaryState;
import fciencias.tarea1.evalFunctions.*;

public class RandomOptimization implements Runnable
{

    private EvalFunction evalFunction;

    private double[] interval;

    private long iterations;

    private int representationalBits;

    private int dimension;

    private BinaryState globalBinaryState;

    private Map<String,Object> globalParams;

    

    public RandomOptimization(EvalFunction evalFunction, double[] interval, long iterations, int representationalBits,int dimension) {
        this.evalFunction = evalFunction;
        this.interval = interval;
        this.iterations = iterations;
        this.representationalBits = representationalBits;
        this.dimension = dimension;
    }

    public RandomOptimization(EvalFunction evalFunction, double[] interval, long iterations, int representationalBits,int dimension, BinaryState globalBinaryState) {
        this.evalFunction = evalFunction;
        this.interval = interval;
        this.iterations = iterations;
        this.representationalBits = representationalBits;
        this.dimension = dimension;
        this.globalBinaryState = globalBinaryState;
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

    public long getIterations() {
        return iterations;
    }

    public void setIterations(long iterations) {
        this.iterations = iterations;
    }

    public int getRepresentationalBits() {
        return representationalBits;
    }

    public void setRepresentationalBits(int representationalBits) {
        this.representationalBits = representationalBits;
    }

    
    public BinaryState getGlobalBinaryState() {
        return globalBinaryState;
    }

    public void setGlobalBinaryState(BinaryState globalBinaryState) {
        this.globalBinaryState = globalBinaryState;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Map<String, Object> getGlobalParams() {
        return globalParams;
    }

    public void setGlobalParams(Map<String, Object> globalParams) {
        this.globalParams = globalParams;
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

    public BinaryState getRandomState(double[] interval, int dimension, int bits)
    {
        return new BinaryState(getRandomVector(interval, dimension),bits, interval);
    }

    public BinaryState optimize()
    {
        for(int i = 0; i < iterations; i ++)
        {
            BinaryState binaryState = getRandomState(interval,dimension,representationalBits);
            double currentStateValue = evalFunction.evalSoution(binaryState.getRealValue()); 
            double minValue = evalFunction.evalSoution(globalBinaryState.getRealValue());
            if(currentStateValue < minValue)
            {
                globalBinaryState = binaryState;
                minValue = currentStateValue;
                System.out.println(minValue);
            }

        }

        return globalBinaryState;
    }

    @Override
    public void run() {
        
        optimize();
        System.out.println(globalBinaryState);
    }

    public static void main( String[] args )
    {
        double[] interval = new double[]{-5.12,5.12};
        int dimension = 3;
        int representationalBits = 90;
        long iterations = 100000000;
        
        
        RandomOptimization randomOptimization = new RandomOptimization(new SphereFunction(), interval, iterations, representationalBits,dimension);
        BinaryState optimuBinaryState = randomOptimization.getRandomState(interval, dimension, representationalBits);
        randomOptimization.setGlobalBinaryState(optimuBinaryState);

        for(int k = 0; k < 12; k++)
            new Thread(randomOptimization).start();
  
    }

}


