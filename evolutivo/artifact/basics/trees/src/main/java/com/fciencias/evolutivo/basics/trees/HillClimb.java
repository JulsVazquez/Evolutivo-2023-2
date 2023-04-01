package com.fciencias.evolutivo.basics.trees;

import java.util.LinkedList;
import java.util.List;

public abstract class HillClimb {

    private Node root;
    private List<Node> nodesQueue;
    private List<Node> nodesPath;
    
    public HillClimb(Node root)
    {
        this.root = root;
        nodesQueue = new LinkedList<>();
        nodesPath = new LinkedList<>();
        nodesQueue.add(root);
    }

    public boolean traverseTree()
    {
        
        while(!nodesQueue.isEmpty())
        {
            Node currentNode = nodesQueue.get(0);
            List<Node> currentChildrens = generateChildrens(currentNode);
            BinaryOrder binaryOrder = new BinaryOrder(currentChildrens);
            binaryOrder.orderList();
            for(Node node : binaryOrder.getOrderedList())
                nodesQueue.add(node);

            


        }
        return false;
    }


    public abstract List<Node> generateChildrens(Node node);

    public abstract Node selectChildren(Node node1, Node node2);
    
}
