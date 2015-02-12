package my.qlearningapplication;

import java.util.HashMap;
import java.util.Scanner;


public class Application {

	HashMap<String, State> states;
	HashMap<String, Action> actions;
	QLearningModule<State, Action> module;
	double alpha;
	double beta;
	
	public Application() {
		this.states = new HashMap<String,State>();
		this.actions = new HashMap<String, Action>();
		this.alpha = 0.5;
		this.beta = 0.5;
		this.module = new QLearningModule<State, Action>(1000, true, this.alpha, this.beta);
	}
	
	public static void main(String[] args) {
		boolean exit = false;
		Application app = new Application();
		
		Scanner in = new Scanner(System.in);
		while(!exit) {
			// Get initial action information <-- enter all possible actions in a field
			// Create the first state, with all actions initialized to default values
			
			// Ask the Q-learning module which action to take
			// Take the action
			// Input information about the reward received for the state-action pair 
			// and the next state (which may need to be added...)
			// Recompute the Q "table"
			// Repeat
		}
		// be able to export Q values to csv
	}

}
