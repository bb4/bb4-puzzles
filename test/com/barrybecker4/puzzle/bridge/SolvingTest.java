package com.barrybecker4.puzzle.bridge;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.common.i18n.StubMessageContext;
import com.barrybecker4.puzzle.bridge.model.BridgeMove;
import com.barrybecker4.puzzle.bridge.model.InitialConfiguration;
import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.barrybecker4.puzzle.bridge.model.InitialConfiguration.ALTERNATIVE_PROBLEM;
import static com.barrybecker4.puzzle.bridge.model.InitialConfiguration.DIFFICULT_PROBLEM;
import static com.barrybecker4.puzzle.bridge.model.InitialConfiguration.STANDARD_PROBLEM;
import static com.barrybecker4.puzzle.bridge.model.InitialConfiguration.TRIVIAL_PROBLEM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * These are more integration tests than unit tests.
 * @author Barry Becker
 */
public class SolvingTest {

    /** initial states that have solutions */
    private static final List<TestCase> A_STAR_CASES = Arrays.asList(
            new TestCase(TRIVIAL_PROBLEM, 8),
            new TestCase(STANDARD_PROBLEM, 15),
            new TestCase(ALTERNATIVE_PROBLEM, 60),
            new TestCase(DIFFICULT_PROBLEM, 47)
    );


    @BeforeClass
    public static void initialSetUp() {
        AppContext.injectMessageContext(new StubMessageContext());
    }

    @AfterClass
    public static void finalTearDown() {
        AppContext.injectMessageContext(null);
    }

    @Test
    public void testSolvingTestsWithAStar() throws Exception {
        runSolvingTests(Algorithm.A_STAR_SEQUENTIAL, A_STAR_CASES);
    }

    /**  these will not find the shortest path
    @Test
    public void testSolvingSimpleSequentialTests() throws Exception {
        runSolvingTests(Algorithm.SIMPLE_SEQUENTIAL, SIMPLE_SEQUENTIAL_CASES);
    }

    @Test
    public void testSolvingConcurrentDepthTests() throws Exception {
        runSolvingTests(Algorithm.CONCURRENT_DEPTH, CONCURRENT_DEPTH_CASES);
    }

    @Test
    public void testSolvingConcurrentBreadthTests() throws Exception {
        runSolvingTests(Algorithm.CONCURRENT_BREADTH, CONCURRENT_BREADTH_CASES);
    }
    */

    /**
     *
     */
    public void runSolvingTests(Algorithm algorithm, List<TestCase> cases) throws Exception {

        BridgePuzzleController controller = new BridgePuzzleController(null);

        for (TestCase testCase : cases) {
            controller.setConfiguration(testCase.config.getPeopleSpeeds());
            PuzzleSolver<BridgeMove> solver =  algorithm.createSolver(controller);
            System.out.println("initial pos = " + controller.initialState());
            List<BridgeMove> path = solver.solve();
            assertNotNull("No solution found for case: " + testCase.config.getLabel(), path);

            String msg = "Unexpected minimum amount of time to cross for (" + testCase.config.getLabel() + ") " +
                        "for " + algorithm.getLabel() + ". The path was " + path + ". ";

            assertEquals(msg + "path length =" + path.size(),
                    testCase.expectedTimeToCross, pathCost(path));
        }
    }


    private int pathCost(List<BridgeMove> path) {
        int totalCost = 0;
        for (BridgeMove move : path) {
            totalCost += move.getCost();
        }
        return totalCost;
    }

    private static class TestCase {
        InitialConfiguration config;
        int expectedTimeToCross;

        TestCase(InitialConfiguration config, int expectedTimeToCross) {
            this.config = config;
            this.expectedTimeToCross = expectedTimeToCross;
        }
    }
}
