package com.fciencias.evolutivo.implementations.tarea3;

import java.util.HashMap;
import java.util.Map;

import com.fciencias.evolutivo.basics.optimizator.*;
import com.fciencias.evolutivo.binaryRepresentation.BinaryRepresentation;
import com.fciencias.evolutivo.evalFunctions.DiscreteWeightFunction;
import com.fciencias.evolutivo.libraries.FileManager;
import com.fciencias.evolutivo.libraries.ParamsValidator;

/**
 * Implementacion de algoritmo de busqueda ILS.
 * Ofrece una solucion a quedar atrapados en optimos locales realizando una
 * ligera perturbacion a un estado optimo y retomar la busqueda desde el 
 * nuevo estado perturbado.
 */
public class LocalIterativeSearch 
{
    private Optimizator optimizator;
    private long iterations;
    private BinaryRepresentation initialSolution;
    private BinaryRepresentation solution;
    private double solutionEvaluation;
    private boolean optimizationDirection;
    private double totalExecutionTime = 0;

    private static final double DISTURB_RATE = 0.001;

    

    public LocalIterativeSearch(Optimizator optimizator, long iterations, boolean optimizationDirection) 
    {
        this.optimizator = optimizator;
        this.iterations = iterations;
        this.optimizationDirection = optimizationDirection;
    }

    public BinaryRepresentation getSolution() 
    {
        return solution;
    }

    public double getSolutionEvaluation() 
    {
        return solutionEvaluation;
    }

    public double getTotalExecutionTime()
    {
        return totalExecutionTime;
    }

    public BinaryRepresentation getInitialSolution()
    {
        return initialSolution;
    }

    public boolean finishCondition(long currentIterations)
    {
        return currentIterations >= iterations;
    }

    public void runIterativeLocalSearch()
    {
        long noIterations = 0L;
        optimizator.startMultiThreadOptimization(false, true);
        solution = optimizator.getOptimumState();
        initialSolution = solution;
        solutionEvaluation = optimizator.getOptimumValuation();

        while(!finishCondition(noIterations++))
        {
            BinaryRepresentation disturbSolution = solution.getRandomState(1, solution.getRealValue());
            optimizator.setInitialState(disturbSolution);
            long deltaTime = optimizator.startMultiThreadOptimization(false, true);
            totalExecutionTime += deltaTime*1.0;
            BinaryRepresentation newOptimumSolution = optimizator.getOptimumState();
            double disturbValue = optimizator.getOptimumValuation();
            if((optimizationDirection ^ (disturbValue < solutionEvaluation)) && (disturbValue != solutionEvaluation))
            {
                solution = newOptimumSolution;
                solutionEvaluation = disturbValue;
            }            
        }
        totalExecutionTime = totalExecutionTime/1000.0;
        
        
    }




    public static void main( String[] args )
    {
        ParamsValidator.validate(args);
        long iterations = ParamsValidator.getIterations();
        int totalThreads = ParamsValidator.getThreads();
        String fileInput = ParamsValidator.getFileIntput();
        String fileOutput = ParamsValidator.getFileOutput();
        if(fileOutput.equals(""))
            fileOutput = fileInput;

        FileManager fileManager = new FileManager();
        long fileIndex = fileManager.openFile("inputs/" + fileInput, true);
        int touples = Integer.parseInt(fileManager.readFileLine(fileIndex, 0));
        double[] w = new double[touples];
        double[] p = new double[touples];
        

        for(int i = 0; i < touples; i++)
        {
            String[] toupleValues = fileManager.readFileLine(fileIndex, i+1).split(" ");
            p[i] = Double.parseDouble(toupleValues[1]);
            w[i] = Double.parseDouble(toupleValues[2]);
            
        }
       
        double maxCost = Double.parseDouble(fileManager.readFileLine(fileIndex, touples + 1));
        Map<String,Object> globalParams = new HashMap<>();
        globalParams.put("TEMP", 50.0);
        RecocidoSimuladoDiscreto recocidoSimulado = new RecocidoSimuladoDiscreto(new DiscreteWeightFunction(p), 10000, touples, touples, 0);
        recocidoSimulado.setWeightCalculator(new DiscreteWeightFunction(w));
        recocidoSimulado.setMaxCost(maxCost);
        recocidoSimulado.setTotalThreads(totalThreads);
        recocidoSimulado.setGlobalParams(globalParams);
        recocidoSimulado.setOutputPath("outputs/tarea3/ILS/" + fileOutput);
        recocidoSimulado.resetGlobalParams();
        LocalIterativeSearch localIterativeSearch = new LocalIterativeSearch(recocidoSimulado, iterations,true);
        localIterativeSearch.runIterativeLocalSearch();

        StringBuilder resultLog = new StringBuilder("\nResumen de ejecucion: \n");
        resultLog.append("\nEstado Inicial: " + localIterativeSearch.getInitialSolution())
        .append("\nResultado Optimo: " + localIterativeSearch.getSolution())
        .append("\nValor Optimo: " + localIterativeSearch.getSolutionEvaluation())
        .append("\nTiempo de ejecucion: " + localIterativeSearch.getTotalExecutionTime() + "s");

        fileIndex = fileManager.openFile("outputs/tarea3/ILS/" + fileOutput + "_Results.txt", true);
        fileManager.writeLine(fileIndex, resultLog.toString());

        fileIndex = fileManager.openFile("outputs/tarea3/ILS/" + fileOutput + "_table_Results.txt", true);
        StringBuilder stringBuilder = new StringBuilder();
        String separatorChar = ",";

        stringBuilder.append(totalThreads).append(separatorChar)
        .append(localIterativeSearch.iterations).append(separatorChar)
        .append(localIterativeSearch.getSolutionEvaluation()).append(separatorChar)
        .append(localIterativeSearch.getTotalExecutionTime())
        .append("s");
        fileManager.writeLine(fileIndex, stringBuilder.toString());

    }
}
