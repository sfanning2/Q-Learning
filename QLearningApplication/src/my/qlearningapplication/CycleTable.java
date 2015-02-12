package my.qlearningapplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

public class CycleTable<I, O, A extends Comparable<? super A>, R extends Comparable<? super R>, T extends Comparable<? super T>> {

	/**
	 * Maps states to maps of states, actions, and number of occurances of output states from state action pairs
	 */
	private HashMap<I, HashMap<O, HashMap<A, HashMap<R, T>>>> stateCycleTable;
        private Stack<Object> stack;
        private HashMap<I, A> stateTable;
        private int numberOfStates;

	public CycleTable() {
		stateCycleTable = new HashMap<I, HashMap<O, HashMap<A, HashMap<R, T>>>>();
                stack = new Stack<Object>();
        }

	/**
	 * Add or re-initialize a state with the provided actions
	 * 
	 * @param state
	 * @param actions
	 */
	public void addIteration(I input, O action, A output, R result) {
            if(stateCycleTable.get(input)== null){
                //no hash with action and other associated with input
                HashMap<O, HashMap<A, HashMap<R, T>>> nH = new HashMap<O, HashMap<A, HashMap<R, T>>>();
                stateCycleTable.put(input, nH);
            }
            if(stateCycleTable.get(input).get(action)==null){
                //no hash with output and numOccurancess associated with action 
                HashMap<A, HashMap<R, T>> ar = new HashMap<A, HashMap<R, T>>();
                stateCycleTable.get(input).put(action, ar);
            }
            if(stateCycleTable.get(input).get(action).get(output) == null){
                HashMap<R, T> rt = new HashMap<R,T>();
                stateCycleTable.get(input).get(action).put(output, rt);
            }
            if(stateCycleTable.get(input).get(action).get(output).get(result) == null){
                // num time cycles
                stateCycleTable.get(input).get(action).get(output).put(result, (T)Integer.valueOf(0));
            }
            Integer r = (Integer) stateCycleTable.get(input).get(action).get(output).get(result);
            r++;
            stateCycleTable.get(input).get(action).get(output).put(result, (T)r);
            numberOfStates++;
            stateTable.put(input, output);
        }
        public A getMostLikelyA(I inputState, O action){
             // returns A (outputState) most likely from I, O input
            T maxValue = null;
            A currentOutput = null;
            for (Entry<A, HashMap<R, T>> actionValueEntry : stateCycleTable.get(inputState).get(action).entrySet()){
                if(maxValue==null){
                    currentOutput = actionValueEntry.getKey();
                    for(Entry<R, T> actionValueEntry2: stateCycleTable.get(inputState).get(action).get(currentOutput).entrySet()){
                        if(maxValue == null){
                            maxValue = actionValueEntry2.getValue();
                        }else if(actionValueEntry2.getValue().compareTo(maxValue)>0){
                            maxValue = actionValueEntry2.getValue();
                        }
                    }
                }else {
                    for(Entry<R, T> actionValueEntry2: stateCycleTable.get(inputState).get(action).get(currentOutput).entrySet()){
                        if(actionValueEntry2.getValue().compareTo(maxValue)>0){
                            currentOutput = actionValueEntry.getKey();
                            maxValue = actionValueEntry2.getValue();
                        }
                    }
                }
                    
                    
                }
            return currentOutput;
        }
        public O getBestOForIA(I inputState, A outputState){
            // returns the action which results in the highest reward for a given input and output
            O bestAction = null;
            R maxReward = null;
            for(Entry<O, HashMap<A,HashMap<R, T>>> actionValueEntry : stateCycleTable.get(inputState).entrySet() ){
                if(maxReward == null){
                bestAction = actionValueEntry.getKey();
                for(Entry<A, HashMap<R, T>> actionValueEntry2 : stateCycleTable.get(inputState).get(bestAction).entrySet()){
                    if(actionValueEntry2.getKey().compareTo(outputState)==0){
                        for(Entry<R, T> actionValueEntry3 : stateCycleTable.get(inputState).get(bestAction).get(outputState).entrySet()){
                           if(maxReward==null){
                                maxReward = actionValueEntry3.getKey();
                                bestAction = actionValueEntry.getKey();
                            }else if(actionValueEntry3.getKey().compareTo(maxReward) > 0){
                                maxReward = actionValueEntry3.getKey();
                                bestAction = actionValueEntry.getKey();
                        }
                        }
                    }
                }
            }else {
                    for(Entry<A, HashMap<R, T>> actionValueEntry2 : stateCycleTable.get(inputState).get(bestAction).entrySet()){
                    if(actionValueEntry2.getKey().compareTo(outputState) == 0){
                        for(Entry<R, T> actionValueEntry3 : stateCycleTable.get(inputState).get(bestAction).get(outputState).entrySet()){
                           if(actionValueEntry3.getKey().compareTo(maxReward) > 0){
                                maxReward = actionValueEntry3.getKey();
                                bestAction = actionValueEntry.getKey();
                        }
                        }
                    }
                }
            }
                
            }
    return bestAction;
          
        }
	/**
	 * Get the action which maximizes utility for this state
	 * 
	 * @param state
	 * @return the action
	 */
//	public O getRowMaxColumn(I state) {
//		O action = null;
//		A maxValue = null;
//		for (Entry<O, A> actionValueEntry : this.stateCycleTable.get(state)
//				.entrySet()) {
//			if (maxValue == null) {
//				// set a starting action/value
//				action = actionValueEntry.getKey();
//				maxValue = actionValueEntry.getValue();
//			} else if (actionValueEntry.getValue().compareTo(maxValue) > 0) {
//				// update the action/value
//				action = actionValueEntry.getKey();
//				maxValue = actionValueEntry.getValue();
//			}
//		}
//		return action;
//	}
//
//	/**
//	 * Get the expected-utility value of a state action pair
//	 * 
//	 * @param state
//	 * @param action
//	 * @return
//	 */
//	public A getValue(I state, O action) {
//		return this.stateCycleTable.get(state).get(action);
//	}
//
//	/**
//	 * 
//	 * @param state
//	 * @param action
//	 * @param utility
//	 */
//	public void updateValue(I state, O action, A utility) {
//		// Update or create the value
//		stateCycleTable.get(state).put(action, utility);
//	}