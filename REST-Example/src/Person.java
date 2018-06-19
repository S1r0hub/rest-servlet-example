public class Person {

	private String name, surname, job;
	
	public Person(String name, String surname, String job) {
		this.name = name;
		this.surname = surname;
		this.job = job;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getJob() {
		return job;
	}
	
	public void setJob(String job) {
		this.job = job;
	}
	
	/** Represent this object as JSON */
	public String toJSON(String tab) {
		StringBuilder json = new StringBuilder();
		
		json.append(tab + "{");
		json.append("\n" + tab + "\t\"name\": " + getName());
		json.append("\n" + tab + "\t\"surname\": " + getSurname());
		json.append("\n" + tab + "\t\"job\": " + getJob());
		json.append(tab + "\n}\n");
		
		return json.toString();
	}
	
	public String toJSON() {
		return toJSON("");
	}
}
