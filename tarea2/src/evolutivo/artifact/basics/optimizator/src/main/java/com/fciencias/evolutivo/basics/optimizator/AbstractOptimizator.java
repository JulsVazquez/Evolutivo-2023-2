package com.fciencias.evolutivo.basics.optimizator;

import java.util.Date;
import java.util.Map;

import com.fciencias.evolutivo.basics.RandomDistribution;
import com.fciencias.evolutivo.binaryRepresentation.BinaryRepresentation;
import com.fciencias.evolutivo.evalFunctions.EvalFunction;
import com.fciencias.evolutivo.evalFunctions.SphereFunction;
import com.fciencias.evolutivo.libraries.FileManager;

/**
 * Hello world!
 *
 */
public abstract class AbstractOptimizator implements Optimizator, Runnable
{
    protected EvalFunction evalFunction;
    protected double[] interval;
    protected long iterations;
    protected int representationalBits;
    protected int dimension;
    protected boolean optimizeDirection = false;
    protected double minimumValue;
    protected double maximumValue;
    protected double bestValue;
    protected boolean logTrack;
    protected RandomDistribution randomDistribution;
    protected FileManager fileManager;
    


    protected BinaryRepresentation globalBinaryRepresentationState;

    protected Map<String,Object> globalParams;

    protected int hilo;

    protected int totalThreads = 32;

    protected static final String FINALIZED_THREADS = "Finalized threads";
    
    protected static final String MINIMUN_VALUE = "Min value";

    protected static final String MAXIMUN_VALUE = "Maximun value";

    protected static final String MEAN_VALUE = "Mean value";

    protected static final String TOTAL_ITERATIONS = "Total iterations";

    protected static final String OUTPUT_FILE_NAME = "evaluationTracking.txt";

    protected static final String PROGRESS_INDICATOR = "progress indicator";

    protected static final String OPTIMUM_OBJECT = "optimum";
    
    protected AbstractOptimizator()
    {
        this.evalFunction = new SphereFunction();
        this.interval = new double[]{0,1};
        this.iterations = 1000;
        this.representationalBits = 8;
        this.dimension = 3;
        initOptimizator();
        fileManager = new FileManager();
    }

    protected AbstractOptimizator(EvalFunction evalFunction, double[] interval, long iterations, int representationalBits, int dimension, Map<String,Object> globalParams, int hilo) {
        this.evalFunction = evalFunction;
        this.interval = interval;
        this.iterations = iterations;
        this.representationalBits = representationalBits;
        this.dimension = dimension;
        this.globalParams = globalParams;
        this.hilo = hilo;
        resetGlobalParams();
        initOptimizator();
        fileManager = new FileManager();
    }

    protected AbstractOptimizator(EvalFunction evalFunction, double[] interval, long iterations, int representationalBits, int dimension, Map<String,Object> globalParams, BinaryRepresentation globalBinaryRepresentationState, int hilo) {
        this.evalFunction = evalFunction;
        this.interval = interval;
        this.iterations = iterations;
        this.representationalBits = representationalBits;
        this.dimension = dimension;
        this.globalParams = globalParams;
        this.globalBinaryRepresentationState = globalBinaryRepresentationState;
        this.hilo = hilo;
        resetGlobalParams();
        bestValue = evalFunction.evalSoution(globalBinaryRepresentationState.getRealValue());
        fileManager = new FileManager();
        
    }

    
    public void setRandomDistribution(RandomDistribution randomDistribution) {
        this.randomDistribution = randomDistribution;
    }

    public void setGlobalBinaryRepresentationState(BinaryRepresentation globalBinaryRepresentationState)
    {
        this.globalBinaryRepresentationState = globalBinaryRepresentationState;
    }

    public BinaryRepresentation getGlobalBinaryRepresentationState()
    {
        return globalBinaryRepresentationState;
    }

    public int getTotalThreads() {
        return totalThreads;
    }

    

    public void setTotalThreads(int totalThreads) {
        this.totalThreads = totalThreads;
    }

    @Override
    public void run() {
        
        optimize();
        ((boolean[])(globalParams.get(FINALIZED_THREADS)))[hilo-1] = true;
    }

    protected boolean brakCondition()
    {
        return false;
    }


    

    protected abstract BinaryRepresentation cheapSolution();

    @Override
    public void optimize() {
        
        while(((long)globalParams.get(TOTAL_ITERATIONS) < iterations) && !brakCondition())
        {
            BinaryRepresentation[] newElements = getNewStates();
            boolean foundBetter = false;
            for(BinaryRepresentation element : newElements)
            {    
                if(isMoreOptimunState(element))
                {
                    foundBetter = true;
                    globalBinaryRepresentationState = element;
                    globalParams.replace(OPTIMUM_OBJECT, element);
                    if(logTrack)
                    {
                        String fileName = OUTPUT_FILE_NAME;
                        long fileIndex = fileManager.openFile(fileName,true);
                        fileManager.writeFile(fileIndex,"Function: "+ evalFunction.getFunctionName() + ", Thread: "+ hilo + ",Iteration: " + globalParams.get(TOTAL_ITERATIONS) + ",Vector: " + globalBinaryRepresentationState.printRealValue()  + ",Value: " + bestValue + "\n",true);
                    }
                    
                }
                if(brakCondition())
                    break;
            }
            if(!foundBetter && globalParams.get(OPTIMUM_OBJECT) == null)
            {
                globalParams.replace(OPTIMUM_OBJECT, cheapSolution());
                globalBinaryRepresentationState = (BinaryRepresentation)globalParams.get(OPTIMUM_OBJECT);
            }

            globalParams.replace(TOTAL_ITERATIONS,(long)globalParams.get(TOTAL_ITERATIONS) + 1L);
        }
    }

    @Override
    public boolean isMoreOptimunState(BinaryRepresentation state) {
        
        double valuation = evalFunction.evalSoution(state.getRealValue());
        boolean isMoreOptimun = false;
        if(optimizeDirection)
        {
            
            if(valuation > (double)globalParams.get(MAXIMUN_VALUE))
            {
                globalParams.replace(MAXIMUN_VALUE, valuation);
                bestValue = valuation;
                isMoreOptimun = true;
                
                maximumValue = valuation;

            }
            else if(valuation < (double)globalParams.get(MINIMUN_VALUE))
            {
                globalParams.replace(MINIMUN_VALUE, valuation);
                minimumValue = valuation;
            }
        }
        else
        {
            if(valuation > (double)globalParams.get(MAXIMUN_VALUE))
            {
                globalParams.replace(MAXIMUN_VALUE, valuation);
                maximumValue = valuation;

            }
            else if(valuation < (double)globalParams.get(MINIMUN_VALUE))
            {
                globalParams.replace(MINIMUN_VALUE, valuation);
                bestValue = valuation;
                isMoreOptimun = true;
                minimumValue = valuation;
            }
        }
        return isMoreOptimun;
    }

    @Override
    public long startMultiThreadOptimization(boolean appendFile, boolean logTrack)
    {
        long fileIndex = fileManager.openFile(OUTPUT_FILE_NAME,appendFile);
        fileManager.writeFile(fileIndex,(appendFile ? "\n" : "") + "Tracking for " + evalFunction.getFunctionName() + "\n",appendFile);

        globalParams.replace(MAXIMUN_VALUE, bestValue);
        globalParams.replace(MINIMUN_VALUE, bestValue);
        globalParams.replace(MEAN_VALUE, bestValue);
        long initTime = new Date().getTime();

        for(int k = 1; k < totalThreads + 1; k++)
        {
            new Thread(createOptimizator(k,logTrack)).start();
        }

        boolean allThreads = false;
        long timeIni = new Date().getTime();
        while(!allThreads)
        {
            boolean currentAll = true;
            for(boolean threadFinished : (boolean[])globalParams.get(FINALIZED_THREADS))
            
                currentAll = currentAll && threadFinished;

            allThreads = currentAll;
            long currentTime = new Date().getTime();
            if(currentTime - timeIni >= 2000)
            {
                timeIni = currentTime;
                Double progress = Math.round(((long)globalParams.get(TOTAL_ITERATIONS))*10000.0/iterations)/100.0;
                if(progress > 100.0)
                    progress = 100.0;
                System.out.println(progress + "% completado");
                StringBuilder progressBar = new StringBuilder("|");
                int barLength = 100;
                for(int i = 0; i < barLength; i++)
                {
                    progressBar.append( i < barLength*progress/100.0 ? "=" : " ");
                }
                progressBar.append("|");
                System.out.println(progressBar);
                System.out.println(progressBar + "\n");
            }
        }
        long finalTime = new Date().getTime();
        return finalTime - initTime;

    }

    public abstract AbstractOptimizator createOptimizator(int hilo, boolean logTrack);

    public void resetGlobalParams()
    {
        if(!globalParams.containsKey(MAXIMUN_VALUE))
            globalParams.put(MAXIMUN_VALUE,0.0);
        else
            globalParams.replace(MAXIMUN_VALUE, 0.0);

        
        if(!globalParams.containsKey(MINIMUN_VALUE))
            globalParams.put(MINIMUN_VALUE,0.0);
        else
            globalParams.replace(MINIMUN_VALUE, 0.0);


        if(!globalParams.containsKey(MEAN_VALUE))
            globalParams.put(MEAN_VALUE,0.0);
        else
            globalParams.replace(MEAN_VALUE, 0.0);


        if(!globalParams.containsKey(TOTAL_ITERATIONS))
            globalParams.put(TOTAL_ITERATIONS,0L);
        else
            globalParams.replace(TOTAL_ITERATIONS, 0L);


        if(!globalParams.containsKey(PROGRESS_INDICATOR))
            globalParams.put(PROGRESS_INDICATOR,0.0);
        else
            globalParams.replace(PROGRESS_INDICATOR, 0.0);


        if(!globalParams.containsKey(FINALIZED_THREADS))
            globalParams.put(FINALIZED_THREADS,new boolean[totalThreads]);
        else
            globalParams.replace(FINALIZED_THREADS, new boolean[totalThreads]);

        if(!globalParams.containsKey(OPTIMUM_OBJECT))
            globalParams.put(OPTIMUM_OBJECT,null);
        else
            globalParams.replace(OPTIMUM_OBJECT, null);

    }

    public void optimizeToMax()
    {
        this.optimizeDirection = true;
    }

    public void optimizeToMin()
    {
        this.optimizeDirection = false;
    }

    public void setLogTrack(boolean logTrack) {
        this.logTrack = logTrack;
    }

    

}
