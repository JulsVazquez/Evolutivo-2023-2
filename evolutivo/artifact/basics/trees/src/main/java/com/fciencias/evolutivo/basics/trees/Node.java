package com.fciencias.evolutivo.basics.trees;

import java.util.List;

public interface Node {
    
    public Node getParent();

    public List<Node> getNeighborhoods();

    public List<Node> getChildrens();

    public Object getValue();

}
