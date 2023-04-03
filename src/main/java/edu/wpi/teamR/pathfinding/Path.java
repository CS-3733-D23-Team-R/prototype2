package edu.wpi.teamR.pathfinding;

import lombok.Getter;

import java.util.ArrayList;

public class Path {
    @Getter
    private ArrayList<Integer> path;

    Path() {
        path = new ArrayList<Integer>();
    }

    void add(int nodeID) {
        path.add(0, nodeID);
    }
}
