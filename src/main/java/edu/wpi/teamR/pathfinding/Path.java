package edu.wpi.teamR.pathfinding;

import lombok.Getter;

import java.util.ArrayList;

public class Path {
    @Getter
    private ArrayList<String> path;

    Path() {
        path = new ArrayList<String>();
    }

    void add(String nodeID) {
        path.add(0, nodeID);
    }
}
