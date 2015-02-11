package my.qlearningapplication;
public class State {
	private String name = "s_default";
	
	public State(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
