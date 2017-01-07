package com.barrybecker4.puzzle.bridge;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.common.i18n.StubMessageContext;
import com.barrybecker4.puzzle.bridge.model.BridgeMove1;
import com.barrybecker4.puzzle.bridge.model.InitialConfiguration1;
import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.Option;
import scala.collection.JavaConversions;
import scala.collection.Seq;

import java.util.Arrays;
import java.util.List;

import static com.barrybecker4.puzzle.bridge.model.InitialConfiguration1.*;
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
            new TestCase(DIFFICULT_PROBLEM, 47) // supposed to be 41
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
        runSolvingTests(Algorithm1.A_STAR_SEQUENTIAL, A_STAR_CASES);
    }

    /**  these will not find the shortest path
    @Test
    public void testSolvingSimpleSequentialTests() throws Exception {
        runSolvingTests(Algorithm1.SIMPLE_SEQUENTIAL, SIMPLE_SEQUENTIAL_CASES);
    }

    @Test
    public void testSolvingConcurrentDepthTests() throws Exception {
        runSolvingTests(Algorithm1.CONCURRENT_DEPTH, CONCURRENT_DEPTH_CASES);
    }

    @Test
    public void testSolvingConcurrentBreadthTests() throws Exception {
        runSolvingTests(Algorithm1.CONCURRENT_BREADTH, CONCURRENT_BREADTH_CASES);
    }
    */

    private void runSolvingTests(Algorithm1 algorithm, List<TestCase> cases) throws Exception {

        BridgePuzzleController1 controller = new BridgePuzzleController1(null);

        for (TestCase testCase : cases) {
            controller.setConfiguration(testCase.config.getPeopleSpeeds());
            PuzzleSolver<BridgeMove1> solver =  algorithm.createSolver(controller);
            System.out.println("initial pos = " + controller.initialState());
            Option<Seq<BridgeMove1>> path = solver.solve();
            assertNotNull("No solution found for case: " + testCase.config.getLabel(), path);

            String msg = "Unexpected minimum amount of time to cross for (" + testCase.config.getLabel() + ") " +
                        "for " + algorithm.getLabel() + ". The path was " + path + ". ";

            assertEquals(msg + "path length =" + path.get().size(),
                    testCase.expectedTimeToCross, pathCost(path.get()));
        }
    }


    private int pathCost(Seq<BridgeMove1> path) {
        int totalCost = 0;
        for (BridgeMove1 move : JavaConversions.asJavaCollection(path)) {
            totalCost += move.getCost();
        }
        return totalCost;
    }

    private static class TestCase {
        InitialConfiguration1 config;
        int expectedTimeToCross;

        TestCase(InitialConfiguration1 config, int expectedTimeToCross) {
            this.config = config;
            this.expectedTimeToCross = expectedTimeToCross;
        }
    }
}
