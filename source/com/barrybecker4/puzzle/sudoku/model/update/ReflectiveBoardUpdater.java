/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.sudoku.model.update;

import com.barrybecker4.puzzle.sudoku.model.board.Board;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessControlException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Responsible for updating a board given a list of updaters to apply.
 * Unfortunately we cannot use reflection in an applet without making is a signed applet
 * (or have signed jars in the case of webstart), so we use NonReflectiveBoardUpdater in deployed version.
 * @author Barry Becker
 */
public class ReflectiveBoardUpdater implements IBoardUpdater {

    private List<Class<? extends AbstractUpdater>> updaterClasses;

    /**
     * Constructor
     * @param updaterClasses the updater classes to use when updating the board during an interaction of the solver.
     */
    public ReflectiveBoardUpdater(List<Class<? extends AbstractUpdater>> updaterClasses) {
        this.updaterClasses = updaterClasses;
    }

    public ReflectiveBoardUpdater(Class<? extends AbstractUpdater>... classes) {
        updaterClasses = Arrays.asList(classes);
    }

    /**
     * Update candidate lists for all cells then set the unique values that are determined.
     * First create the updaters using reflection, then apply them.
     */
    @Override
    public void updateAndSet(Board board) {

        List<IUpdater> updaters = createUpdaters(board);

        for (IUpdater updater : updaters) {
            updater.updateAndSet();
        }
    }

    /**
     * Creates the updater instances using reflection. Cool.
     * @param board the board
     * @return list of updaters to apply
     */
    private List<IUpdater> createUpdaters(Board board)   {

        List<IUpdater> updaters = new LinkedList<IUpdater>();

        for (Class<? extends IUpdater> clazz : updaterClasses) {
            Constructor<? extends IUpdater> ctor;
            try {
                ctor = clazz.getDeclaredConstructor(Board.class);
                try {
                    ctor.setAccessible(true);
                    IUpdater updater = ctor.newInstance(board);
                    updaters.add(updater);
                } catch (InstantiationException e) {
                    throw new IllegalStateException("Could not instantiate " + clazz.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Could not access constructor of " + clazz.getName(), e);
                } catch (InvocationTargetException e) {
                    throw new IllegalStateException("Could not invoke constructor of " + clazz.getName(), e);
                } catch (AccessControlException e) {
                    System.out.println("allowing access when should not");
                }

            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("Could not find constructor for " + clazz.getName(), e);
            }
        }
        return updaters;
    }
}
