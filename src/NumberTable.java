import java.util.HashMap;
import java.util.Map.Entry;

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
	 * add with default utility
	 * @param state
	 * @param action
	 */
	public void addValue(T state, K action) {
		// Update or create the value
		stateActionTable.get(state).put(action, this.defaultValue);
	}
	
	public void addValue(T state, K action, V utility) {
		// Update or create the value
		stateActionTable.get(state).put(action, utility);
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
		for (Entry<K, V> actionValueEntry : this.stateActionTable.get(state).entrySet()) {
			if (maxValue == null) {
				// set a starting action/value
				action = actionValueEntry.getKey();
				maxValue = actionValueEntry.getValue();
			} else if (actionValueEntry.getValue().compareTo(maxValue) > 0){
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
}
