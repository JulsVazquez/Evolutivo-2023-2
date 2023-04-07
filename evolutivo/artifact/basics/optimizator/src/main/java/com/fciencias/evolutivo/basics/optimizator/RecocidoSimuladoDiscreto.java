package com.fciencias.evolutivo.basics.optimizator;

import com.fciencias.evolutivo.binaryRepresentation.*;
import com.fciencias.evolutivo.evalFunctions.EvalFunction;

public class RecocidoSimuladoDiscreto extends RecocidoSimulado{

    protected double maxCost;
    protected EvalFunction weightCalculator;
    protected String fileInput;

    public RecocidoSimuladoDiscreto(EvalFunction evalFunction, long iterations, int representationalBits,int dimension, int hilo) {

        super(evalFunction, iterations, representationalBits, dimension, hilo);
        optimizeToMax();
    }

    public RecocidoSimuladoDiscreto(EvalFunction evalFunction, long iterations, int representationalBits,int dimension, BinaryRepresentation globalBinaryRepresentationState, int hilo) {

        super(evalFunction, iterations, representationalBits, dimension, globalBinaryRepresentationState,hilo);
        optimizeToMax();
    }

    public void setMaxCost(double maxCost)
    {
        this.maxCost = maxCost;
    }

    public void setWeightCalculator(EvalFunction weightCalculator) {
        this.weightCalculator = weightCalculator;
    }

    @Override
    public void resetMetaParams()
    {
        globalParams.replace(TOTAL_ITERATIONS,0L);
        temp = INIT_TEMP;
        totalIterations = 0;
        globalParams.replace(FINALIZED_THREADS, new boolean[totalThreads]);
    }

    @Override
    public void initOptimizator() {
        
        BinaryDiscreteState binaryDiscreteState = new BinaryDiscreteState(representationalBits);
        globalBinaryRepresentationState = binaryDiscreteState.getRandomState(1, binaryDiscreteState.getRealValue());
        bestValue = evalFunction.evalSoution(globalBinaryRepresentationState.getRealValue());
    }

    @Override
    public BinaryRepresentation[] getNewStates() {
        
        return new BinaryRepresentation[]{globalBinaryRepresentationState.getRandomState(1, globalBinaryRepresentationState.getRealValue())};
    }

    @Override
    public boolean isValidState(BinaryRepresentation state) {
        return weightCalculator.evalSoution(state.getRealValue()) < maxCost;
    }

    @Override
    protected BinaryRepresentation cheapSolution() {

        return new BinaryDiscreteState(representationalBits);
    }

    @Override
    public AbstractOptimizator createOptimizator(int hilo, boolean logTrack) {

        RecocidoSimuladoDiscreto recocidoSimulado = new RecocidoSimuladoDiscreto(evalFunction, iterations, representationalBits, dimension, globalBinaryRepresentationState, hilo);
        recocidoSimulado.setLogTrack(logTrack);
        recocidoSimulado.setMaxCost(maxCost);
        recocidoSimulado.setWeightCalculator(weightCalculator);
        recocidoSimulado.setTotalThreads(totalThreads);
        recocidoSimulado.setOutputPath(outputPath);
        recocidoSimulado.setGlobalParams(globalParams);
        recocidoSimulado.bestValue = bestValue;
        recocidoSimulado.globalParams.replace(MINIMUN_VALUE, bestValue);
        recocidoSimulado.globalParams.replace(MAXIMUN_VALUE, bestValue);
        recocidoSimulado.globalParams.put(TEMPERATURA, temp);
        return recocidoSimulado;
    }
    
}
