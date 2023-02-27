package fciencias.tarea1;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fciencias.tarea1.binaryRepresentation.BinaryState;
import fciencias.tarea1.complements.FileManager;
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

    private int hilo;

    private static final String FINALIZED_THREADS = "Finalized threads";
    
    private static final String MINIMUN_VALUE = "Min value";

    private static final String MAXIMUN_VALUE = "Maximun value";

    private static final String MEAN_VALUE = "Mean value";

    private static final String TOTAL_ITERATIONS = "Total iterations";

    private static final int TOTAL_THREADS = 25;

    private static final String OUTPUT_FILE_NAME = "evaluationTracking.txt";

    private static final String PROGRESS_INDICATOR = "progress indicator";


    

    public RandomOptimization(EvalFunction evalFunction, double[] interval, long iterations, int representationalBits,int dimension) {
        this.evalFunction = evalFunction;
        this.interval = interval;
        this.iterations = iterations;
        this.representationalBits = representationalBits;
        this.dimension = dimension;
    }

    public RandomOptimization(EvalFunction evalFunction, double[] interval, long iterations, int representationalBits,BinaryState globalState, int dimension, int hilo) {
        this.evalFunction = evalFunction;
        this.interval = interval;
        this.iterations = iterations;
        this.representationalBits = representationalBits;
        this.globalBinaryState = globalState;
        this.dimension = dimension;
        this.hilo = hilo;
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

    public double[] getRandomVector(double[] interval,int n, int randomize)
    {
        Date date = new Date();
        long time = date.getTime();
        Random rand = new Random(time*randomize);

        double[] randVector = new double[n];
        for(int i = 0; i < n; i ++)
        {
            randVector[i] = rand.nextDouble()*(interval[1] - interval[0]) + interval[0];
        }
        return randVector;
    }

    public BinaryState getRandomState(double[] interval, int dimension, int bits,int randomize)
    {
        return new BinaryState(getRandomVector(interval, dimension,randomize),bits, interval);
    }

    public BinaryState optimize()
    {
        for(int i = 0; i < iterations; i ++)
        {
            
            BinaryState binaryState = getRandomState(interval,dimension,representationalBits,hilo);
            double currentStateValue = evalFunction.evalSoution(binaryState.getRealValue()); 
            StringBuilder vector = new StringBuilder("(");
            for(double xi : binaryState.getRealValue())
                vector.append(xi).append(" ");
            vector.append(")");
            
            int totaIterations = (Integer)globalParams.get(TOTAL_ITERATIONS);
            globalParams.replace(TOTAL_ITERATIONS, (Integer)globalParams.get(TOTAL_ITERATIONS) + 1);
            globalParams.replace(PROGRESS_INDICATOR, (Double)globalParams.get(PROGRESS_INDICATOR) + 1);
            double mean = (((Double)globalParams.get(MEAN_VALUE))*totaIterations);
            mean = mean + currentStateValue;
            mean = mean/(totaIterations + 1);
            globalParams.replace(MEAN_VALUE, mean);

            double minValue = ((Double)globalParams.get(MINIMUN_VALUE));
            double maxValue = ((Double)globalParams.get(MAXIMUN_VALUE));
            if(currentStateValue < minValue)
            {
                globalParams.replace(MINIMUN_VALUE, currentStateValue);
                globalBinaryState = binaryState;
                minValue = currentStateValue;
                FileManager fileManager = new FileManager();
                String fileName = OUTPUT_FILE_NAME;
                long fileIndex = fileManager.openFile(fileName,true);
                fileManager.writeFile(fileIndex,"Function: "+ evalFunction.getFunctionName() + ", Thread: "+ hilo + ",Iteration: " + i + ",Vector: " + vector  + ",Value: " + minValue + "\n",false);
            }
            else if(currentStateValue > maxValue)
            {
                globalParams.replace(MAXIMUN_VALUE, currentStateValue);
            }

        }

        return globalBinaryState;
    }

    @Override
    public void run() {
        
        optimize();
        ((ArrayList<Integer>)(globalParams.get(FINALIZED_THREADS))).add(hilo);
    }

    public static String runEvaluationProcess(EvalFunction evalFunction, int dimension, double[] interval, int representationalBits, long iterations, boolean appendFile, Map<String,Object> globalParams)
    {

        FileManager fileManager = new FileManager();
        long fileIndex = fileManager.openFile(OUTPUT_FILE_NAME,appendFile);
        fileManager.writeFile(fileIndex,(appendFile ? "\n" : "") + "Tracking for " + evalFunction.getFunctionName() + "\n",appendFile);

        RandomOptimization optimumRandomOptimization = new RandomOptimization(evalFunction, interval, iterations, representationalBits,dimension);
        BinaryState optimuBinaryState = optimumRandomOptimization.getRandomState(interval, dimension, representationalBits,0);
        optimumRandomOptimization.setGlobalBinaryState(optimuBinaryState);

        
        double initValue = evalFunction.evalSoution(optimuBinaryState.getRealValue());
        globalParams.replace(MAXIMUN_VALUE, initValue);
        globalParams.replace(MINIMUN_VALUE, initValue);
        globalParams.replace(MEAN_VALUE, initValue);

        long initTime = new Date().getTime();

        for(int k = 1; k < TOTAL_THREADS + 1; k++)
        {
            RandomOptimization randomOptimization = new RandomOptimization(evalFunction, interval, iterations, representationalBits,optimuBinaryState,dimension,k);
            randomOptimization.setGlobalParams(globalParams);
            new Thread(randomOptimization).start();
        }

        /* El indicador de progreso se debe optimizar debido a que existen casos donde uno o más hilos incrementan el contador al mismo tiempo
         * y no toman el valor mas actualizado de contador global.
         *
         * double currentProgres = ((Double)globalParams.get(PROGRESS_INDICATOR))/(totalThreads*iterations);
         * double currentIndicator = 0.05;
         * while(currentProgres < 1)
         * {
         *    if(currentProgres > currentIndicator)
         *   {
         *         System.out.println("====== Progreso: " + Math.round(currentProgres*10000)/100 + " ======");
         *         currentIndicator += 0.05;
         *     }   
         *     currentProgres  = ((Double)globalParams.get(PROGRESS_INDICATOR))/(totalThreads*iterations);
         * }
         */

        while(((ArrayList<Integer>)globalParams.get(FINALIZED_THREADS)).size() < TOTAL_THREADS);

        long finalTime = new Date().getTime();

        long deltatime = finalTime - initTime;

        StringBuilder endMessage = new StringBuilder("Results for " + evalFunction.getFunctionName() + " using dimension " + dimension + " and " + iterations+ " iterations \n")
        .append("Execution time: " + deltatime/1000.0 + "s \n")
        .append("Best value: " + globalParams.get(MINIMUN_VALUE) + "\n")
        .append("Avg value: " + globalParams.get(MEAN_VALUE) + "\n")
        .append("Worst value: " + globalParams.get(MAXIMUN_VALUE) + "\n");
        
        return endMessage.toString();
    }

    public static void main(String[] args) {
        
        int dimension = 3;
        int representationalBits = 64;
        long iterations = 10000000;

        Map<String,Object> globalParams = new HashMap<>();

        FileManager fileManager = new FileManager();
        
        String results = "";
        
        globalParams.put(FINALIZED_THREADS,new ArrayList<>(TOTAL_THREADS));
        globalParams.put(MAXIMUN_VALUE,0.0);
        globalParams.put(MINIMUN_VALUE,0.0);
        globalParams.put(MEAN_VALUE,0.0);
        globalParams.put(TOTAL_ITERATIONS,0);
        globalParams.put(PROGRESS_INDICATOR,0.0);


        EvalFunction evalFunction = new SphereFunction();
        double[] interval = new double[]{-5.12,5.12};
        results = results + RandomOptimization.runEvaluationProcess(evalFunction, dimension, interval, representationalBits, iterations,false,globalParams);

        globalParams.replace(FINALIZED_THREADS,new ArrayList<>(TOTAL_THREADS));
        globalParams.replace(MAXIMUN_VALUE,0.0);
        globalParams.replace(MINIMUN_VALUE,0.0);
        globalParams.replace(MEAN_VALUE,0.0);
        globalParams.replace(TOTAL_ITERATIONS,0);
        globalParams.replace(PROGRESS_INDICATOR,0.0);
        evalFunction = new AckleyFunction();
        interval = new double[]{-30,30};
        results = results + "\n" + RandomOptimization.runEvaluationProcess(evalFunction, dimension, interval, representationalBits, iterations,true,globalParams);


        globalParams.replace(FINALIZED_THREADS,new ArrayList<>(TOTAL_THREADS));
        globalParams.replace(MAXIMUN_VALUE,0.0);
        globalParams.replace(MINIMUN_VALUE,0.0);
        globalParams.replace(MEAN_VALUE,0.0);
        globalParams.replace(TOTAL_ITERATIONS,0);
        globalParams.replace(PROGRESS_INDICATOR,0.0);
        evalFunction = new GriewankFunction();
        interval = new double[]{-600,600};
        results = results + "\n" + RandomOptimization.runEvaluationProcess(evalFunction, dimension, interval, representationalBits, iterations,true,globalParams);

        globalParams.replace(FINALIZED_THREADS,new ArrayList<>(TOTAL_THREADS));
        globalParams.replace(MAXIMUN_VALUE,0.0);
        globalParams.replace(MINIMUN_VALUE,0.0);
        globalParams.replace(MEAN_VALUE,0.0);
        globalParams.replace(TOTAL_ITERATIONS,0);
        globalParams.replace(PROGRESS_INDICATOR,0.0);
        evalFunction = new ThenthPowerFunction();
        interval = new double[]{-5.12,5.12};
        results = results + "\n" + RandomOptimization.runEvaluationProcess(evalFunction, dimension, interval, representationalBits, iterations,true,globalParams);



        globalParams.replace(FINALIZED_THREADS,new ArrayList<>(TOTAL_THREADS));
        globalParams.replace(MAXIMUN_VALUE,0.0);
        globalParams.replace(MINIMUN_VALUE,0.0);
        globalParams.replace(MEAN_VALUE,0.0);
        globalParams.replace(TOTAL_ITERATIONS,0);
        globalParams.replace(PROGRESS_INDICATOR,0.0);
        evalFunction = new RastriginFunction();
        interval = new double[]{-5.12,5.12};
        results = results + "\n" + RandomOptimization.runEvaluationProcess(evalFunction, dimension, interval, representationalBits, iterations,true,globalParams);


        globalParams.replace(FINALIZED_THREADS,new ArrayList<>(TOTAL_THREADS));
        globalParams.replace(MAXIMUN_VALUE,0.0);
        globalParams.replace(MINIMUN_VALUE,0.0);
        globalParams.replace(MEAN_VALUE,0.0);
        globalParams.replace(TOTAL_ITERATIONS,0);
        globalParams.replace(PROGRESS_INDICATOR,0.0);
        evalFunction = new RosenbrockFunction();
        interval = new double[]{-2.048,2.048};
        results = results + "\n" + RandomOptimization.runEvaluationProcess(evalFunction, dimension, interval, representationalBits, iterations,true,globalParams);
        fileManager.writeFile(fileManager.openFile("Results.txt", false),results,true);
    }

}