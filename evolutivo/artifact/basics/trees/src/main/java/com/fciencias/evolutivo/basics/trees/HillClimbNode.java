package com.fciencias.evolutivo.basics.trees;

import java.util.HashMap;

import com.fciencias.evolutivo.binaryRepresentation.BinaryRepresentation;
import com.fciencias.evolutivo.evalFunctions.EvalFunction;

public class HillClimbNode extends AbstractNode{

    protected static final String LEVEL = "level";
    protected static final String VALUE = "value";
    protected EvalFunction evalFunction;
    protected boolean direction;
    
    public HillClimbNode(int level, Object value, boolean direction, EvalFunction evalFunction)
    {
        super();
        this.values = new HashMap<>();
        this.values.put(LEVEL, level);
        this.values.put(VALUE, value);
        this.evalFunction = evalFunction;
        this.direction = direction;
        
    }

    public int getLevel()
    {
        return (Integer)(values.get(LEVEL));
    }

    @Override
    public boolean compareNode(Node node) {
        
        BinaryRepresentation thisNodeValue = (BinaryRepresentation)(this.values.get(VALUE));
        BinaryRepresentation otherNodeValue = (BinaryRepresentation)(node.getValue().get(VALUE));

        double thisValue = evalFunction.evalSoution(thisNodeValue.getRealValue());
        double otherValue = evalFunction.evalSoution(otherNodeValue.getRealValue());
        
        return direction  ^ (thisValue > otherValue);
    }

    
    
}
