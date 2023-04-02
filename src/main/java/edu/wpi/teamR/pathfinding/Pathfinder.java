package edu.wpi.teamR.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import static java.lang.Math.abs;

public class Pathfinder {
    private NodeDAO nodes;
    private EdgeDAO edges;
    public Pathfinder(NodeDAO nodes, EdgeDAO edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public Path aStarPath(String startID, String endID) throws Exception{
        Path path = new Path();
        HashMap<String, String> cameFrom = new HashMap<>();
        HashMap<String, Integer> costSoFar = new HashMap<>();
        PriorityQueue<QueueNode> pQueue = new PriorityQueue<>();
        pQueue.add(new QueueNode(startID, 0));
        String currentNode;
        while(!pQueue.isEmpty()){
            currentNode = pQueue.remove().getNodeID();

            if(currentNode.equals(endID)){ break; }

            ArrayList<String> neighbors = edges.getConnectedNodes(currentNode);
            for (String neighbor : neighbors) {
                int newCost = costSoFar.get(currentNode) + nodeDist(currentNode, neighbor);
                if (costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)){
                    costSoFar.replace(neighbor, newCost);
                    int priority = newCost + hueristic(neighbor, endID);
                    pQueue.add(new QueueNode(neighbor, priority));
                    cameFrom.put(neighbor, currentNode);
                }
            }
        }

        currentNode = endID;
        while (!currentNode.equals(startID)) {
            path.add(currentNode);
            currentNode = cameFrom.get(currentNode);
        }

        return path;
    }

    //TODO - actually return a hueristic
    private int hueristic(String nodeID, String endID){
        //returns A* hueristic for node

        return 0;
    }

    private ArrayList<String> findNeighboringNodes(String nodeID, String endID) throws Exception {
        // ArrayList<Node> neighbors = new ArrayList<Node>();
        ArrayList<String> neighbors = edges.getConnectedNodes(nodeID);
        for (String neighbor : neighbors) {
            if (neighbor.equals(endID)) {
                continue;
            }
            Node node = nodes.getNodeByID(neighbor);
            String type = node.getNodeType();
            if (!type.equals("HALL") && !type.equals("ELEV") && !type.equals("STAI")) {
                neighbors.remove(neighbor);
            }
        }

        return neighbors;
    }

    private int nodeDist(String currentNodeID, String nextNodeID) {
        return nodeDist(currentNodeID, nextNodeID, 0);
    }

    //TODO account for changing floors
    private int nodeDist(String currentNodeID, String nextNodeID, int constant) {
        //finds difference in x,y
        Node currNode = nodes.getNodeByID(currentNodeID);
        Node nextNode = nodes.getNodeByID(nextNodeID);

        int xDif = abs(currNode.getXCoord() - nextNode.getXCoord());
        int yDif = abs(currNode.getYCoord() - nextNode.getYCoord());
        int zDif = 0;

        if (currNode.getNodeType().equals("STAI")) && nextNode.getNodeType.equals("STAI")) {
            zDif = 1000;
        }

        return xDif + yDif; //returns distance
    }

    //outputs the floor number 0 indexed from the lowest floor (L2)
    private int floorNumAsInt(String floorNum){
        int output;
        switch(floorNum){
            case "L1": output = 1;
            case "L2": output = 0;
            default: output = Integer.parseInt(floorNum) + 1;
        }
        return output;
    }
}
