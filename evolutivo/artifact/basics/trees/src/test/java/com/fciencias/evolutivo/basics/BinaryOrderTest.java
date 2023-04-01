package com.fciencias.evolutivo.basics;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.fciencias.evolutivo.basics.trees.BinaryOrder;
import com.fciencias.evolutivo.basics.trees.HillClimbNode;
import com.fciencias.evolutivo.basics.trees.Node;
import com.fciencias.evolutivo.binaryRepresentation.BinaryMappingState;
import com.fciencias.evolutivo.evalFunctions.*;

public class BinaryOrderTest 
{
    
    @Test
    public void orderTest()
    {
        List<Node> disOrderList = new LinkedList<>();
        EvalFunction evalFunction = new SphereFunction();

        for(int i = 0; i < 15000; i++)
        {
            disOrderList.add(new HillClimbNode(0, 
                new BinaryMappingState(new double[]{Math.round(Math.random()*100)}), 
                true, 
                evalFunction));
        }
        BinaryOrder binaryOrder = new BinaryOrder(disOrderList);
        binaryOrder.orderList();
       
        double initValue = 0;
        boolean ordered = true;
        for(Node node : binaryOrder.getOrderedList())
        {
            BinaryMappingState binState = (BinaryMappingState)(node.getValue().get("value"));
            double currentValue = evalFunction.evalSoution(binState.getRealValue());
            if(currentValue < initValue)
            {
                ordered = false;
                break;
            }
        }
        assertTrue(ordered);
    }
}
