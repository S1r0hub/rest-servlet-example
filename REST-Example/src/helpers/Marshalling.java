package helpers;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;


/**
 * Helper for marshalling and unmarshalling operations.
 */
public class Marshalling {

	private final Schema xml_schema;
	
	// private constructor for singleton pattern
	public Marshalling(Schema xml_schema) {
		this.xml_schema = xml_schema;
	}
	
	
	private Unmarshaller unmarshaller;
	private Marshaller marshaller;
	
	
	/** Create the Unmarshaller */
	public void createUnmarshaller(Class<?> context_type) {
		
		// create context and Unmarshaller
		try {
			JAXBContext context = JAXBContext.newInstance(context_type);
			unmarshaller = context.createUnmarshaller();
			unmarshaller.setSchema(xml_schema);
		} catch (JAXBException e) {
			throw new RuntimeException("Failed to create Unmarshaller!", e);
		}
	}
	
	/** Get the Unmarshaller. Can be null if not initialized yet! */
	public Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}
	
	
	/** Create the Marshaller */
	public void createMarshaller(Class<?> context_type) {
		
		// create context and Marshaller
		try {
			JAXBContext context = JAXBContext.newInstance(context_type);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			marshaller.setSchema(xml_schema);
		} catch (JAXBException e) {
			throw new RuntimeException("Failed to create Marshaller!", e);
		}
	}
	
	
	/** Get the XML-Schema used by this instance. */
	public Schema getSchema() {
		return xml_schema;
	}
	
	
	/** Get the Marshaller. Can be null if not initialized yet! */
	public Marshaller getMarshaller() {
		return marshaller;
	}
	

    /** Validates a content object against the schema using the Marshaller instance. */
    public void validate(Object contentObject, Class<?> context_type) {
        try {
            Validator validator = xml_schema.newValidator();
            JAXBSource jaxbSource = new JAXBSource(getMarshaller(), contentObject);
            validator.validate(jaxbSource);
        } catch (JAXBException | SAXException | IOException e) {
            throw new RuntimeException("Could not validate object", e);
        }
    }
}
