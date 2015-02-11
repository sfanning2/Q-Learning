package my.qlearningapplication;
public class Action {
	private String name = "a_default";
	
	public Action(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
