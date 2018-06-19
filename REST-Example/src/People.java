import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class People {
	
	List<Person> people = new ArrayList<>();
	
	
	public People() {	
	}
	
	public People(Collection<Person> people) {
		this.people = new ArrayList<>(people);
	}
	
	
	public List<Person> getPeople() {
		return people;
	}
	
	public void setPeople(List<Person> people) {
		this.people = people;
	}
	
	public void addPerson(Person person) {
		people.add(person);
	}
	
	
	/** Represents the people in JSON format */
	public String toJSON(String tab) {
		StringBuilder json = new StringBuilder();
		
		json.append(tab + "{");
		json.append("\n" + tab + "\t\"people\": [");
		
		// indentation
		String tab_2 = tab + "\t\t";
		
		for (Person p : people) {
			json.append(p.toJSON(tab_2));
		}
		
		json.append("\n" + tab + "\t]");
		json.append("\n" + tab + "}\n");
		
		return json.toString();
	}
	
	public String toJSON() {
		return toJSON("");
	}
}
