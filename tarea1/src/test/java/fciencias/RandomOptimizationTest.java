package fciencias;

import org.junit.Assert;
import org.junit.Test;

import fciencias.factories.RandomVectorsFactory;
import fciencias.tarea1.evalFunctions.EvalFunction;
import fciencias.tarea1.evalFunctions.SphereFunction;

/**
 * Unit test for simple App.
 */
public class RandomOptimizationTest 
{
    /**
     * Rigorous Test :-)
     */
    EvalFunction evalFunction;

    @Test
    public void sphereTest()
    {
        evalFunction = new SphereFunction();
        String[] fixedTestVector = RandomVectorsFactory.getRandomSphere();
        int i = 0;
        int k = 0;
        double[] inputVector = new double[10];
        double outputValue = 0;
        while(i < fixedTestVector.length)
        {
            if(fixedTestVector[i].equals("n"))
            {
                for(int j = 0; j < inputVector.length; j++)
                    System.out.print(" " + inputVector[j]);
                Assert.assertEquals(outputValue,evalFunction.evalSoution(inputVector),0.000000001);
                k = 0;
                inputVector = new double[10];
            }
            else if(!fixedTestVector[i+1].equals("n"))
                inputVector[k++] = Double.parseDouble(fixedTestVector[i]);
                
            else
                outputValue = Double.parseDouble(fixedTestVector[i]);
            i++;
        }   
    }
}
