package edu.wpi.teamR.pathfinding;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class PathfinderTest {

    @Test
    void heuristicTest() throws NoSuchMethodException {
        Method method = Pathfinder.class.getDeclaredMethod("hueristic",
                String.class, String.class);
        assertTrue(true);
    }
}