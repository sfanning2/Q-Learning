/* 
 * Copyright 2009-13 IAMAS Group, University of Nebraska-Lincoln.
 * All right reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */



//import edu.nebraska.lincoln.iamas.datastructures.table.NumberTable;

/**
 * The {@code QLearningModule} class provides an agent intelligence module
 * capable of performing Q-Learning on parameterized 
 * {@code State}/{@code Action} types.
 *
 * The module provides support for fixed learning and discount rates, and a
 * discounted learning rate (producing a true average for each 
 * {@code State}/{@code action} pair).
 *
 * @param <State> The class representing states in the algorithm
 * @param <Action> The class reprseneting actions in the algorithm
 *
 * @author Adam Eck
 */
package my.qlearningapplication;

public class QLearningModule<State, Action> {
    /** The table of utilities, indexed by {@code State} and {@code Action}. */
    private final NumberTable<State, Action, Double> utilityTable;

    /**
     * The table counting the number of utility updates, indexed by
     * {@code State} and {@code Action}.
     */
    private final NumberTable<State, Action, Integer> utilityUpdates;

    /** The default utility value for the utility table. */
    private final double defaultUtility;

    /** The learning rate of the algorithm, in [0.0, 1.0]. */
    private  double learningRate;

    /** The discount rate of the algorithm, in [0.0, 1.0]. */
    private  double discountRate;

    /**
     * Denotes whether or not the learning rate should be discounted
     * over time.
     */
    private final boolean useDiscountedLearning;

    /** The total number of updates to the learned utilities. */
    private int totalNumUpdates;

    /** A default value for the learning rate. */
    private static final double DEFAULT_LEARNING_RATE = 0.5;

    /** A discount value for the discount rate. */
    private static final double DEFAULT_DISCOUNT_RATE = 0.5;

    /**
     * Creates a new default {@code QLearningModule}.
     *
     * @param pDefaultUtility The default utility to use in the utility table.
     */
    public QLearningModule(double pDefaultUtility) {
        // create a default module
        this(pDefaultUtility, false);
    }

    /**
     * Creates a new default {@code QLearningModule} with a specified
     * {@code useDiscountedLearning}.
     *
     * @param pDefaultUtility The default utility to use in the utility table.
     * @param pUseDiscountedLearning Whether or not to use discounted learning
     */
    public QLearningModule(double pDefaultUtility, 
            boolean pUseDiscountedLearning) {
        // create a default module
        this(pDefaultUtility, pUseDiscountedLearning, DEFAULT_LEARNING_RATE,
                DEFAULT_DISCOUNT_RATE);
    }

    /**
     * Creates a new parameterized {@code QLearningModule}.
     *
     * @param pDefaultUtility The default utility to use in the utility table
     * @param pUseDiscountedLearning Whether or not to use discounted learning
     * @param pLearningRate The learning rate of the algorithm
     * @param pDiscountRate The discount rate of the algorithm
     */
    public QLearningModule(double pDefaultUtility, 
            boolean pUseDiscountedLearning, double pLearningRate,
            double pDiscountRate) {
        // save the params
        defaultUtility = pDefaultUtility;
        useDiscountedLearning = pUseDiscountedLearning;
        discountRate = Math.min(1.0, Math.max(0.0, pDiscountRate));
        learningRate = useDiscountedLearning
                ? 1.0
                : Math.min(1.0, Math.max(0.0, pLearningRate));

        // set the initial values
        totalNumUpdates = 0;
        
        // create the collections
        utilityTable = new NumberTable<State, Action, Double>(
                Double.valueOf(defaultUtility));
        utilityUpdates = new NumberTable<State, Action, Integer>(
                Integer.valueOf(0));
    }
    public void setLearningRate(double rate){
        learningRate  = useDiscountedLearning
                ? 1.0
                : Math.min(1.0, Math.max(0.0, rate));
    }
    public void setDiscountRate(double rate){
        discountRate = Math.min(1.0, Math.max(0.0, rate));
    }
    /**
     * Performs the learning function for the {@code QLearningModule}.
     *
     * @param state The current {@code State}
     * @param nextState The {@code State} resulting from performing
     *                  {@code action} in {@code state}
     * @param action The performed {@code Action}
     * @param reward The reward for performing {@code action} in {@code state}
     */
    public void learnUtility(State state, State nextState, Action action,
            double reward) {
        // update the table
        updateUtilityTable(state, nextState, action, reward);
    }

    /**
     * Assigns a utility to the table, assuming perfect knowledge of utility.
     *
     * @param state The current {@code State}
     * @param nextState The {@code State} resulting from performing
     *                  {@code action} in {@code state}
     * @param action The performed {@code Action}
     * @param utility The utility for performing {@code action} in
     *      {@code state}, asssuming perfect knowledge of utility
     */
    public void assignPerfectUtility(State state, State nextState, Action action,
            double utility) {
        // assign the value to the table
        utilityTable.updateValue(state, action, utility);
    }

    /**
     * Updates the utility table based on performing a given {@code Action}
     * in a {@code State}, leading to a new {@code State nextState} and a
     * {@code reward}.
     *
     * @param state The current {@code State}
     * @param nextState The {@code State} resulting from performing
     *                  {@code action} in {@code state}
     * @param action The performed {@code Action}
     * @param reward The reward for performing {@code action} in {@code state}
     */
    private void updateUtilityTable(State state, State nextState, Action action,
            double reward) {
        // increment the number of updates
        utilityUpdates.addValue(state, action, Integer.valueOf(1));
        totalNumUpdates++;

        // compute the new utility in the table
        double newUtility = computeNewUtility(state, nextState, action,
                reward);

        // update the table
        utilityTable.updateValue(state, action, newUtility);
    }

    /**
     * Computes the new expected utility for a given
     * {@code State}/{@code Action} pair based on the next {@code State} and
     * reward for performing {@code action} in {@code state}.
     *
     * @param state The current {@code State}
     * @param nextState The {@code State} resulting from performing
     *                  {@code action} in {@code state}
     * @param action The performed {@code Action}
     * @param reward The reward for performing {@code action} in {@code state}
     *
     * @return The new expected utility for {@code state} and {@code action}
     */
    private double computeNewUtility(State state, State nextState,
            Action action, double reward) {
        // get the old utility
        double oldUtility = utilityTable.getValue(state, action).doubleValue();

        // get the best next utility
        double bestNextUtility = bestUtility(nextState);

        // compute the new value
        double lRate = useDiscountedLearning
                ? learningRate 
                    / utilityUpdates.getValue(state, action).intValue()
                : learningRate;

        return oldUtility * (1 - lRate)
                + lRate * (reward + discountRate * bestNextUtility);
    }

    /**
     * Computes the best utility possible for a given {@code State}.
     * This is the one that computes the V in the Q-learning algorithm,
     * i.e., V is the maximum value for state,bestAction.
     *
     * @param state The {@code State} to compute the best utility for.
     *
     * @return The maximum expected utility for a given {@code State}.
     */
    public double bestUtility(State state) {
        // get the best action for the  state
        Action bestAction = bestAction(state);

        // find the utility for this action
        return (bestAction != null)
                ? utilityTable.getValue(state, bestAction)
                : defaultUtility;
    }

    /**
     * Determines the best {@code Action} for a given {@code State}.
     *
     * @param state The {@code Stpublic void setLearningRate(double rate){
        learningRate  = rate;
    }ate} to find the best {@code Action} for.
     *
     * @return The {@code Action} with maximum expected utility for
     *         {@code state}.
     */
    public Action bestAction(State state) {
        // get the action which maximizes utility for this state
        return utilityTable.getRowMaxColumn(state);
    }

    /**
     * Returns the expected utility for taking an {@code Action} in a given
     * {@code State}.
     *
     * @param state The current {@code State}
     * @param action The chosen {@code Action}
     *
     * @return The expected utility of taking {@code action} in {@code state}
     */
    public double expectedUtility(State state, Action action) {
        // grab the expected utility
        return utilityTable.getValue(state, action).doubleValue();
    }

    /**
     * Grabs the number of times the learner has been updated across all
     * table entries.
     *
     * @return The number of times the learner has been updated
     */
    public int getNumberOfUpdates() {
        return totalNumUpdates;
    }

    /**
     * Grabs the number of times the learner has been updated for a particular
     * {@code State} and {@code Action} pair.
     *
     * @param state The current {@code State}
     * @param action The chosen {@code Action}
     *
     * @return The number of times the learner has been updated for entry
     *         {@code state} and {@code action}
     */
    public int getNumberOfUpdates(State state, Action action) {
        // grab the number of updates
        return utilityUpdates.getValue(state, action).intValue();
    }
    
    
    /**
     * Returns a deep-copy of the utility table.
     *
     * @return The table of utilities indexed by {@code State}/{@code Action}
     *         pairs
     */
    public NumberTable<State, Action, Double> getUtilityTable() {
        return new NumberTable<State, Action, Double>(utilityTable);
    }
    
}
