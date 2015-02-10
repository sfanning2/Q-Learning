
public class NumberTable<T,K,V> {

	public NumberTable(NumberTable<T, K, V> utilityTable) {
		// TODO Auto-generated constructor stub
	}

	public NumberTable(V defaultUtilityValue) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param state
	 * @param action
	 * @param utility
	 */
	public void updateValue(T state, K action, V utility) {
		
	}
	
	/**
	 * Get the expected-utility value of a state action pair
	 * @param state
	 * @param action
	 * @return
	 */
	public V getValue(T state, K action){
		return null;		
	}
	
	/**
	 * Get the action which maximizes utility for this state
	 * @param state
	 * @return the action
	 */
	public K getRowMaxColumn(T state) {
		return null;
	}
	
	public void addValue(T state, K action, V utility) {
		
	}
}
