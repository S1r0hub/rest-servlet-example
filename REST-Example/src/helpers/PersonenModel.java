package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
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
    private Personen personen = new Personen();
    
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
		marshalling.createUnmarshaller(personen.getClass());
		marshalling.createMarshaller(personen.getClass());
		
		// perform unmarshalling (get data from the XML-Document)
		personen = unmarshal(document);
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
    
    
	/** Unmarshalling of the given source file. */
	private Personen unmarshal(File source) {
		try {
			return marshalling.getUnmarshaller()
				.unmarshal(new StreamSource(source), personen.getClass())
				.getValue();
		}
		catch (JAXBException e) {
			throw new RuntimeException("Unmarshalling failed!", e);
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
    	marshal(personen, writer);
    }
    
    /** Marshal this model into the given XML-Document file. */
    public void marshal()
    throws JAXBException, FileNotFoundException {
		marshal(new PrintWriter(document));
    }
    
    
    /** Reference to the people */
    public Personen getPersonen() {
    	return personen;
    }
    
    
    /** Get a reference to the person with a specific id.
     * @throws IndexOutOfBoundsException */
    public PersonType getPerson(int id)
    throws IndexOutOfBoundsException {
    	for (PersonType person : getPersonen().getPerson()) {
    		if (person.getId() == id) {
    			return person;
    		}
    	}
    	throw new IndexOutOfBoundsException("Person not found!");
    }
    
    
    /** Adds a new person */
    public void addPerson(int id, String name, String surname, String birthday, List<String> jobs) { 
    	
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
    	marshalling.validate(person, person.getClass());
    	
    	// add the person to the people
    	personen.getPerson().add(person);
    }
    
    /** Adds a new person without a job */
    public void addPerson(int id, String name, String surname, String birthday) {
    	addPerson(id, name, surname, birthday, new ArrayList<String>());
    }
    
    /** Adds a new person with unknown birthday and without a job */
    public void addPerson(int id, String name, String surname) {
    	addPerson(id, name, surname, "unknown");
    }
}
