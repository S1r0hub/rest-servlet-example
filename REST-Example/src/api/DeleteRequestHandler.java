package api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import helpers.PersonenModel;
import jaxb.ObjectFactory;
import jaxb.PersonType;
import jaxb.Personen;


/**
 * DELETE-Request Handler<br/>
 * 1. Delete the entire collection.<br/>
 * 2. Delete the addressed member of the collection.
 */
public class DeleteRequestHandler extends RequestHandler {

	
	public DeleteRequestHandler(PersonenModel model) {
		super(model);
	}

	
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, String id)
	throws ServletException, IOException {

		// get writer and set the content type of the response to be XML
		PrintWriter responseWriter = response.getWriter();
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		
		// store a copy of the removed element
		JAXBElement<?> personCopy = null;
		Personen peopleCopy = null;
		
		try {
			response.setStatus(HttpServletResponse.SC_OK);
			
			if (id != null) {
				// delete a person (single item of the collection)
				personCopy = deletePerson(id, response);
			}
			else {
				// delete the whole collection
				peopleCopy = deleteCollection(response);
			}
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseWriter.println(failureMessage(e.getMessage()));
		}
		

		try {
			if (personCopy != null) {
				// return the copy of the deleted person
				getModel().marshal(personCopy, responseWriter);
			}
			else if (peopleCopy != null) {
				// return the copy of the deleted collection
				getModel().marshal(peopleCopy, responseWriter);
			}
		}
		catch (JAXBException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseWriter.println(failureMessage("Marshalling the copy failed!"));
			responseWriter.close();
		}
	}

	
	/**
	 * Delete a person from the XML-Document.<br/>
	 * (Writes changes to the file)
	 *
	 * @return A copy of the deleted element as a JAXBElement.
	 */
	private JAXBElement<?> deletePerson(String id, HttpServletResponse response)
	throws Exception {
		
		// add the person to the collection
		PersonType copy = getModel().deletePerson(id);
		
		// marshal the XML-Document (save changes to the file)
		try {
			getModel().marshal();
		}
		catch (Exception e) {
			throw new Exception("Marshalling failed. (" + e.getMessage() + ")");
		}
		
		// print information about added person
		System.out.println("Person deleted (" + copy.getId() + ").");
		
		// convert to jaxbElement for marshalling
		return new ObjectFactory().createPerson(copy);
	}

	
	/**
	 * Delete the whole collection of the XML-Document.<br/>
	 * (Will write changes to the file)
	 * @return 
	 * 
	 * @return A copy of the deleted collection.
	 */
	private Personen deleteCollection(HttpServletResponse response)
	throws Exception {
		
		Personen copy = getModel().deleteCollection();
		
		// marshal the XML-Document (save changes to the file)
		try {
			getModel().marshal();
		}
		catch (Exception e) {
			throw new Exception("Marshalling failed. (" + e.getMessage() + ")");
		}
		
		// print information about added person
		System.out.println("Collection deleted (" + copy.getPerson().size() + " items).");
		
		// convert to jaxbElement for marshalling
		return copy;
	}
}
