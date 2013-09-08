package com.barrybecker4.puzzle.twopails;

import com.barrybecker4.common.app.AppContext;
import com.barrybecker4.common.i18n.StubMessageContext;
import com.barrybecker4.puzzle.common.solver.PuzzleSolver;
import com.barrybecker4.puzzle.twopails.model.PailParams;
import com.barrybecker4.puzzle.twopails.model.PourOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * These are more integration tests than unit tests.
 * @author Barry Becker
 */
public class TestSolving {

    /** initial states that have solutions */
    private static final List<TestCase> A_STAR_CASES = Arrays.asList(
            new TestCase(new PailParams(9, 4, 6), 8),
            new TestCase(new PailParams(0, 0, 0), 0),
            new TestCase(new PailParams(1, 2, 1), 1),
            new TestCase(new PailParams(5, 2, 1), 4),
            new TestCase(new PailParams(55, 17, 3), 40),
            new TestCase(new PailParams(55, 7, 5), 32),
            new TestCase(new PailParams(97, 23, 32), 110)
    );

    /** initial states that have solutions */
    private static final List<TestCase> CONCURRENT_DEPTH_CASES = Arrays.asList(
            new TestCase(new PailParams(9, 4, 6), 18),
            new TestCase(new PailParams(0, 0, 0), 0),
            new TestCase(new PailParams(1, 2, 1), 2),
            new TestCase(new PailParams(5, 2, 1), 8),
            new TestCase(new PailParams(55, 17, 3), 102),
            new TestCase(new PailParams(55, 7, 5), 90),
            new TestCase(new PailParams(97, 23, 32), 130)
    );

    /** initial states that have solutions */
    private static final List<TestCase> CONCURRENT_BREADTH_CASES = Arrays.asList(
            new TestCase(new PailParams(9, 4, 6), 18),
            new TestCase(new PailParams(0, 0, 0), 0),
            new TestCase(new PailParams(1, 2, 1), 2),
            new TestCase(new PailParams(5, 2, 1), 8),
            new TestCase(new PailParams(55, 17, 3), 102),
            new TestCase(new PailParams(55, 7, 5), 32),
            new TestCase(new PailParams(97, 23, 32), 130)
    );

    private static final List<TestCase> SIMPLE_SEQUENTIAL_CASES = Arrays.asList(
            new TestCase(new PailParams(9, 4, 6), 18),
            new TestCase(new PailParams(0, 0, 0), 0),
            new TestCase(new PailParams(1, 2, 1), 1),
            new TestCase(new PailParams(5, 2, 1), 8),
            new TestCase(new PailParams(55, 17, 3), 102),
            new TestCase(new PailParams(55, 7, 5), 90),
            new TestCase(new PailParams(97, 23, 32), 130)
    );

    /** these initial states do not have solutions */
    private static final List<PailParams> NEGATIVE_CASES = Arrays.asList(
            new PailParams(2, 2, 5),
            new PailParams(2, 4, 3),
            new PailParams(8, 4, 6)
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

    @Test
    public void testSolvingSimpleSequentialTests() throws Exception {
        runSolvingTests(Algorithm.SIMPLE_SEQUENTIAL, SIMPLE_SEQUENTIAL_CASES);
    }

    @Test
    public void testSolvingConcurrentDepthTests() throws Exception {
        runSolvingTests(Algorithm.CONCURRENT_DEPTH, CONCURRENT_DEPTH_CASES, true);
    }

    @Test
    public void testSolvingConcurrentBreadthTests() throws Exception {
        runSolvingTests(Algorithm.CONCURRENT_BREADTH, CONCURRENT_BREADTH_CASES, true);
    }

    public void runSolvingTests(Algorithm algorithm, List<TestCase> cases) throws Exception {
         runSolvingTests(algorithm, cases, false);
    }

    /**
     * @param withMaxPathLen if true then the expected path length value is a maximum.
     *    This is needed for concurrent tests - which may not find the best deterministically.
     */
    public void runSolvingTests(Algorithm algorithm, List<TestCase> cases,
                                boolean withMaxPathLen) throws Exception {

        TwoPailsPuzzleController controller = new TwoPailsPuzzleController(null);

        for (TestCase testCase : cases) {
            controller.setParams(testCase.params);
            PuzzleSolver<PourOperation> solver =  algorithm.createSolver(controller);
            System.out.println("initial pos = " + controller.initialPosition());
            List<PourOperation> path = solver.solve();
            assertNotNull("No solution found for case params: " + testCase.params, path);

            String msg = "Unexpected number of steps to solve (" + testCase.params + ") " +
                        "for " + algorithm.getLabel() + ". The path was " + path + ". ";
            if (withMaxPathLen)     {
                assertTrue(msg + "path size=" + path.size(), path.size() <= testCase.expectedNumSteps);
            } else {
                assertEquals(msg, testCase.expectedNumSteps, path.size());
            }
        }
    }

    @Test
    public void testSolutionNotFound() throws Exception {
        TwoPailsPuzzleController controller = new TwoPailsPuzzleController(null);
        PuzzleSolver<PourOperation> solver =  Algorithm.A_STAR_SEQUENTIAL.createSolver(controller);

        for (PailParams testCase : NEGATIVE_CASES) {
            controller.setParams(testCase);
            List<PourOperation> path = solver.solve();
            assertNull("Solution unexpectedly found for params: " + testCase + " the path was " + path, path);
        }
    }


    private static class TestCase {
        PailParams params;
        int expectedNumSteps;

        TestCase(PailParams params, int expectedNumSteps) {
            this.params = params;
            this.expectedNumSteps = expectedNumSteps;
        }
    }
}
