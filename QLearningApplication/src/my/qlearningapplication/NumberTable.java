package my.qlearningapplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

public class NumberTable<T, K, V extends Comparable<? super V>> {

	/**
	 * Maps states to maps of actions and utility values
	 */
	private HashMap<T, HashMap<K, V>> stateActionTable;

	private V defaultValue;

	public NumberTable(NumberTable<T, K, V> utilityTable) {
		stateActionTable = new HashMap<T, HashMap<K, V>>();

		// copy values of old table
		for (Entry<T, HashMap<K, V>> entry : utilityTable.stateActionTable
				.entrySet()) {
			HashMap<K, V> innerMap = new HashMap<K, V>();
			for (Entry<K, V> innerEntry : entry.getValue().entrySet()) {
				// insert a value for each action
				innerMap.put(innerEntry.getKey(), innerEntry.getValue());
			}
			// insert in table
			stateActionTable.put(entry.getKey(), innerMap);
		}

	}

	public NumberTable(V defaultUtilityValue) {
		defaultValue = defaultUtilityValue;
		stateActionTable = new HashMap<T, HashMap<K, V>>();
	}

	/**
	 * Add or re-initialize a state with the provided actions
	 * 
	 * @param state
	 * @param actions
	 */
	public void addStateWithActions(T state, List<K> actions) {
		HashMap<K, V> actionValueMap = new HashMap<K, V>();
		for (K action : actions) {
			actionValueMap.put(action, defaultValue);
		}
		this.stateActionTable.put(state, actionValueMap);
	}

	/**
	 * Get the action which maximizes utility for this state
	 * 
	 * @param state
	 * @return the action
	 */
	public K getRowMaxColumn(T state) {
		K action = null;
		V maxValue = null;
		for (Entry<K, V> actionValueEntry : this.stateActionTable.get(state)
				.entrySet()) {
			if (maxValue == null) {
				// set a starting action/value
				action = actionValueEntry.getKey();
				maxValue = actionValueEntry.getValue();
			} else if (actionValueEntry.getValue().compareTo(maxValue) > 0) {
				// update the action/value
				action = actionValueEntry.getKey();
				maxValue = actionValueEntry.getValue();
			}
		}
		return action;
	}

	/**
	 * Get the expected-utility value of a state action pair
	 * 
	 * @param state
	 * @param action
	 * @return
	 */
	public V getValue(T state, K action) {
		return this.stateActionTable.get(state).get(action);
	}

	/**
	 * 
	 * @param state
	 * @param action
	 * @param utility
	 */
	public void updateValue(T state, K action, V utility) {
		// Update or create the value
		stateActionTable.get(state).put(action, utility);
	}

	/**
	 * 
	 * @return a csv style string of values
	 */
	public String toString() {
		StringBuilder mainBuilder = new StringBuilder("State\n");
	
		for (Entry<T, HashMap<K, V>> entry : this.stateActionTable.entrySet()) {
			// Add state
			mainBuilder.append(entry.getKey().toString());
			mainBuilder.append(",");
			
			// Prepare actions and values
			StringBuilder actionRow = new StringBuilder();
			StringBuilder valueRow = new StringBuilder();
			SortedSet<Entry<K, V>> entries = new TreeSet<Entry<K, V>>(new EntryComparator());
			entries.addAll(entry.getValue().entrySet());
			
			for (Entry<K, V> innerEntry : entries) {
				// Add actions
				actionRow.append(innerEntry.getKey().toString());
				actionRow.append(",");
				
				// Add values
				valueRow.append(",");
				valueRow.append(innerEntry.getValue().toString());
			}
			
			// add actions
			mainBuilder.append(actionRow.toString());
			mainBuilder.append("\n");
			
			// add values
			mainBuilder.append(valueRow.toString());
			mainBuilder.append("\n");
			
		}
		return mainBuilder.toString();
	}
}
