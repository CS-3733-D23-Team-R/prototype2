package edu.wpi.teamR.pathfinding;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QueueNode implements Comparable<QueueNode> {
    private String nodeID;
    private int value;

    QueueNode(String nodeID, int value) {
        this.nodeID = nodeID;
        this.value = value;
    }
    @Override
    public int compareTo(QueueNode q) {
        return value - q.getValue();
    }
}
