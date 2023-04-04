package edu.wpi.teamR.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static java.lang.Math.abs;

import edu.wpi.teamR.database.*;

public class Pathfinder {
    private NodeDAO nodes;
    private EdgeDAO edges;
    private LocationNameDAO locationNames;
    private MoveDAO moves;
    public Pathfinder(NodeDAO nodes, EdgeDAO edges, MoveDAO moves, LocationNameDAO locationNames) {
        this.nodes = nodes;
        this.edges = edges;
        this.moves = moves;
        this.locationNames = locationNames;
    }

    //if accessible is True, the algorithm will not suggest staircases
    public Path aStarPath(int startID, int endID, boolean accessible) throws Exception{
        Path path = new Path();
        HashMap<Integer, Integer> cameFrom = new HashMap<>();
        HashMap<Integer, Integer> costSoFar = new HashMap<>();
        PriorityQueue<QueueNode> pQueue = new PriorityQueue<>();
        pQueue.add(new QueueNode(startID, 0));
        int currentNode;
        costSoFar.put(startID, 0);
        while(!pQueue.isEmpty()){
            currentNode = pQueue.remove().getNodeID();

            if(currentNode == endID){ break; }

            ArrayList<Integer> neighbors = edges.getAdjacentNodeIDs(currentNode);
            for (int neighbor : neighbors) {
                //remove stair nodes if accessible is checked
                if(accessible && findNodeType(currentNode).equals("STAI") && findNodeType(neighbor).equals("STAI")) {
                    continue;
                }
                int newCost = costSoFar.get(currentNode) + nodeDist(currentNode, neighbor);
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)){
                    costSoFar.put(neighbor, newCost);
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
        path.add(startID);

        return path;
    }

    private String findNodeType(int nodeID) {
        return locationNames.selectLocationNames(moves.selectMoves(nodeID, null, null).get(0).getLongName() ,null, null).get(0).getNodeType();
    }

    private int hueristic(int nodeID, int endID) throws NotFoundException {
        //returns A* hueristic for node
        return nodeDist(nodeID, endID, 200);
    }

    private ArrayList<Integer> findNeighboringNodes(int nodeID, int endID) throws NotFoundException {
        // ArrayList<Node> neighbors = new ArrayList<Node>();
        ArrayList<Integer> neighbors = edges.getAdjacentNodeIDs(nodeID);
        for (Integer neighbor : neighbors) {
            if (neighbor.equals(endID)) {
                continue;
            }
            String type = findNodeType(neighbor);
            if (!type.equals("HALL") && !type.equals("ELEV") && !type.equals("STAI")) {
                neighbors.remove(neighbor);
            }
        }

        return neighbors;
    }

    private int nodeDist(int currentNodeID, int nextNodeID) throws NotFoundException {
        return nodeDist(currentNodeID, nextNodeID, 100);
    }

    private int nodeDist(int currentNodeID, int nextNodeID, int zDifMultiplier) throws NotFoundException {
        //finds difference in x,y
        Node currNode = nodes.selectNodeByID(currentNodeID);
        Node nextNode = nodes.selectNodeByID(nextNodeID);

        int xDif = abs(currNode.getxCoord() - nextNode.getxCoord());
        int yDif = abs(currNode.getyCoord() - nextNode.getyCoord());
        int zDif = abs(floorNumAsInt(currNode.getFloorNum()) - floorNumAsInt(nextNode.getFloorNum()));

        if (findNodeType(currentNodeID).equals("STAI") && findNodeType(nextNodeID).equals("STAI")) {
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
            case "L1":
                output = 1;
                break;
            case "L2":
                output = 0;
                break;
            default:
                output = Integer.parseInt(floorNum) + 1;
        }
        return output;
    }
}
