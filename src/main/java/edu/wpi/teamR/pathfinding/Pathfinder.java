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

    //if accessible is True, the algorithm will not suggest staircases
    public Path aStarPath(int startID, int endID, boolean accessible) throws Exception{
        Path path = new Path();
        HashMap<Integer, Integer> cameFrom = new HashMap<>();
        HashMap<Integer, Integer> costSoFar = new HashMap<>();
        PriorityQueue<QueueNode> pQueue = new PriorityQueue<>();
        pQueue.add(new QueueNode(startID, 0));
        int currentNode;
        while(!pQueue.isEmpty()){
            currentNode = pQueue.remove().getNodeID();

            if(currentNode == endID){ break; }

            ArrayList<Integer> neighbors = edges.getConnectedNodes(currentNode);
            for (int neighbor : neighbors) {
                //remove stair nodes if accessible is checked
                if(accessible && currentNode.getNodeType().equals("STAI") && neighbor.getNodeType().equals("STAI")){
                    continue;
                }
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
        while (currentNode != startID) {
            path.add(currentNode);
            currentNode = cameFrom.get(currentNode);
        }

        return path;
    }

    private int hueristic(int nodeID, int endID){
        //returns A* hueristic for node
        return nodeDist(nodeID, endID, 200);
    }

    private ArrayList<String> findNeighboringNodes(int nodeID, int endID) throws Exception {
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

    private int nodeDist(int currentNodeID, int nextNodeID) {
        return nodeDist(currentNodeID, nextNodeID, 100);
    }

    private int nodeDist(int currentNodeID, int nextNodeID, int zDifMultiplier) {
        //finds difference in x,y
        Node currNode = nodes.getNodeByID(currentNodeID);
        Node nextNode = nodes.getNodeByID(nextNodeID);

        int xDif = abs(currNode.getXCoord() - nextNode.getXCoord());
        int yDif = abs(currNode.getYCoord() - nextNode.getYCoord());
        int zDif = abs(floorNumAsInt(currNode.floorNum() - floorNumAsInt(nextNodeID.floorNum()));

        if (currNode.getNodeType().equals("STAI") && nextNode.getNodeType.equals("STAI")) {
            zDif = zDif * zDifMultiplier * 2;
        } else {
            zDif = zDif * zDifMultiplier;
        }

        return xDif + yDif + zDif; //returns distance
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
