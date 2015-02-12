package my.qlearningapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Application {

    private static Application instance = null;

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    private HashMap<String, State> states;
    private HashMap<String, Action> actions;
    private QLearningModule<State, Action> module;

    private Application() {
        this.states = new HashMap<String, State>();
        this.actions = new HashMap<String, Action>();
        this.module = new QLearningModule<State, Action>(1000, false, 0.8, 0.5);
    }

    public State addState(String newState) {
        State state = new State(newState);
        if (states.get(newState) == null) {
            states.put(newState, state);
            this.module.addStateWithActions(state, this.actions.values());
        }
        return state;
    }

    public Action addAction(String newAction) {
        Action action = new Action(newAction);
        if (actions.get(newAction) == null) {
            actions.put(newAction, action);
            if (!states.isEmpty()) {
                this.module.addAction(action);
            }
        }
        return action;
    }
    
    public void removeState(String state) {
        State stateObj = this.states.get(state);
        if (stateObj != null) {
            this.module.removeState(stateObj);
        }
    }
    
    public void removeAction(String action) {
        Action actionObj = this.actions.get(action);
        if (actionObj != null) {
            this.module.removeAction(actionObj);
        }
    }

//    public static void main(String[] args) {
//        boolean exit = false;
//        Application app = new Application();
//
//        Scanner in = new Scanner(System.in);
//        while (!exit) {
//			// Get initial action information <-- enter all possible actions in a field
//            // Create the first state, with all actions initialized to default values
//
//			// Ask the Q-learning module which action to take
//            // Take the action
//            // Input information about the reward received for the state-action pair 
//            // and the next state (which may need to be added...)
//            // Recompute the Q "table"
//            // Repeat
//        }
//        // be able to export Q values to csv
//    }
    /**
     * @return the states
     */
    public HashMap<String, State> getStates() {
        return states;
    }

    /**
     * @param states the states to set
     */
    public void setStates(HashMap<String, State> states) {
        this.states = states;
    }

    /**
     * @return the actions
     */
    public HashMap<String, Action> getActions() {
        return actions;
    }

    /**
     * @param actions the actions to set
     */
    public void setActions(HashMap<String, Action> actions) {
        this.actions = actions;
    }

    /**
     * @return the module
     */
    public QLearningModule<State, Action> getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(QLearningModule<State, Action> module) {
        this.module = module;
    }

}
