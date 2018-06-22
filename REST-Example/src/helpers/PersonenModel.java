package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import jaxb.Name;
import jaxb.ObjectFactory;
import jaxb.PersonType;
import jaxb.Personen;


/**
 * Provides an interface to the "personen" document.
 */
public class PersonenModel {

    // constant file reference to the document
    private final File document;
	
    // load constant schema file
    private final Schema schema;
    
    // main element that holds people
    private Personen people = new Personen();
    
    // instance for marshalling and unmarshalling operations
    private final Marshalling marshalling;
    
	
    /** Constructor */
	public PersonenModel(String schema_path, String document_path) {
    	
		// try to load XML-Document (e.g. "personen.xml")
		try {
			document = new File(PersonenModel.class.getResource(document_path).getFile());
		}
		catch (NullPointerException e) {
			throw new RuntimeException("Could not load XML-Document!");
		}
		
		// load XML-Schema (e.g. "personen.xsd")
		schema = loadSchema(schema_path);
		
    	// create marshalling helper instance
    	marshalling = new Marshalling(schema);
    	
		// create the unmarshaller and marshaller instance
		marshalling.createUnmarshaller(people.getClass());
		marshalling.createMarshaller(people.getClass());
		
		// perform unmarshalling (get data from the XML-Document)
		try {
			people = unmarshal(document);
		}
		catch (UnmarshalException e) {
			throw new RuntimeException("Unmarshalling XML-Document failed! (" + e.getCause().getMessage() + ")", e);
		}
	}
	
	
    /**
     * Loads and instantiates the projects XML Schema file
     *
     * @return reference to a Schema object
     */
    private static Schema loadSchema(String xsd_path) {
        URL schemaFilePath = PersonenModel.class.getResource(xsd_path);

        try {
            return SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
                .newSchema(schemaFilePath);
        }
        catch (SAXException ex) {
            throw new RuntimeException("Error during schema parsing!", ex);
        }
        catch (NullPointerException ex) {
            throw new RuntimeException("Could not load schema!", ex);
        }
    }
    
    /** Get the according schema. */
    public Schema getSchema() {
    	return schema;
    }
    
    
    /** Get the marshalling helper object. */
    public Marshalling getMarshallingHelper() {
    	return marshalling;
    }
    
    
	/** Unmarshalling a collection by StreamSource. */
	public Personen unmarshal(StreamSource source) throws UnmarshalException {
		try {
			return marshalling.getUnmarshaller()
				.unmarshal(source, people.getClass())
				.getValue();
		}
		catch (JAXBException e) {
			throw new UnmarshalException("Unmarshalling failed! (" + e.getCause().getMessage() + ")", e);
		}
	}
	
	/** Unmarshalling a collection by a given file. */
	public Personen unmarshal(File source) throws UnmarshalException {
		return unmarshal(new StreamSource(source));
	}
	
	
	/** Unmarshalling of a person. */
	public PersonType unmarshalPerson(StreamSource source) throws UnmarshalException {
		try {
			return marshalling.getUnmarshaller()
				.unmarshal(source, PersonType.class)
				.getValue();
		}
		catch (UnmarshalException e) {
			throw new UnmarshalException("Unmarshalling failed! (" + e.getCause().getMessage() + ")", e);
		}
		catch (JAXBException e) {
			e.printStackTrace();
			throw new UnmarshalException("Unmarshalling failed!");
		}
	}
	
	
	/** Marshal single JAXB-Elements into a writer. */
    public void marshal(Object jaxbElement, PrintWriter writer)
    throws JAXBException {
    	marshalling.getMarshaller().marshal(jaxbElement, writer);
    }
	
    /** Marshal this model into a writer. */
    public void marshal(PrintWriter writer)
    throws JAXBException {
    	marshal(people, writer);
    }
    
    /** Marshal this model into the given XML-Document file. */
    public void marshal()
    throws JAXBException, FileNotFoundException {
		marshal(new PrintWriter(document));
    }
    
    
    /** Validate a new entry */
    public void validate(Object contentObject, Class<?> context_type)
    throws JAXBException {
    	marshalling.validate(contentObject, context_type);
    }
    
    
    /** Reference to the people */
    public Personen getPeople() {
    	return people;
    }
    
    
    /** Get a reference to the person with a specific id.
     * @throws IndexOutOfBoundsException */
    public PersonType getPerson(String id)
    throws IndexOutOfBoundsException {
    	
		for (PersonType person : getPeople().getPerson()) {
			if (person.getId().equals(id)) {
				return person;
			}
		}
		
		throw new IndexOutOfBoundsException("Person not found!");
    }
    
    
    /** Tells if a person with that id exists. */
    public boolean existsPerson(String id) {
    	try {
    		getPerson(id);
    		return true;
    	}
    	catch (Exception e) {
    		return false;
    	}
    }
    
    
    /** Adds a new person */
    public void addPerson(String id, String name, String surname, String birthday, List<String> jobs) throws Exception { 
    	
    	// use factory created by jaxb to create instances
    	ObjectFactory factory = new ObjectFactory();
    	PersonType person = factory.createPersonType();
    	
    	Name person_name = factory.createName();
    	person_name.setVorname(name);
    	person_name.setNachname(surname);
    	
    	person.setId(id);
    	person.setName(person_name);
    	person.setGeburtstag(birthday);
    	
    	// add all the jobs
    	for (String job : jobs) {
    		person.getBeruf().add(job);
    	}
    	
    	// check that the new person object is valid against the schema
		JAXBElement<?> jaxbPerson = new ObjectFactory().createPerson(person);
    	marshalling.validate(jaxbPerson, jaxbPerson.getClass());
    	
    	// add the person to the people
    	people.getPerson().add(person);
    }
    
    /** Adds a new person without a job */
    public void addPerson(String id, String name, String surname, String birthday) throws Exception {
    	addPerson(id, name, surname, birthday, new ArrayList<String>());
    }
    
    /** Adds a new person with unknown birthday and without a job */
    public void addPerson(String id, String name, String surname) throws Exception {
    	addPerson(id, name, surname, "unknown");
    }
    
    /** Adds a new person from a given instance */
    public void addPerson(String id, PersonType person) throws Exception {
    	addPerson(
    		id,
    		person.getName().getVorname(),
    		person.getName().getNachname(),
    		person.getGeburtstag(),
    		person.getBeruf()
    	);
    }
    
 
	/** Replace a person by the given information. */
	public void replacePerson(String id, String name, String surname, String birthday, List<String> jobs) throws JAXBException {
		
		// get person by id
		PersonType person = getPerson(id);
		
		// create name object
		Name new_name = new ObjectFactory().createName();
		new_name.setVorname(name);
		new_name.setNachname(surname);
		
		// set person information
		person.setId(id);
		person.setName(new_name);
		person.setGeburtstag(birthday);
		
		// remove current jobs
		person.getBeruf().clear();
		person.getBeruf().addAll(jobs);
		
		// validate this person against the XML-Schema
		JAXBElement<?> jaxbPerson = new ObjectFactory().createPerson(person);
		marshalling.validate(jaxbPerson, jaxbPerson.getClass());
	}
	
	/** Replace a person by another. */
	public void replacePerson(String id, PersonType newPerson) throws JAXBException {
		
		replacePerson(
			id,
			newPerson.getName().getVorname(),
			newPerson.getName().getNachname(),
			newPerson.getGeburtstag(),
			newPerson.getBeruf()
		);
	}
	
	
	/** Replace the whole collection of people. */
	public void replaceCollection(Personen people) {
		
		// remove all previous people
		this.people.getPerson().clear();
		
		// add the new collection of people
		this.people.getPerson().addAll(people.getPerson());
	}
	
}
