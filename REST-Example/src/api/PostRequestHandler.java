package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import helpers.PersonenModel;
import jaxb.PersonType;


/**
 * POST-Request Handler<br/>
 * 1. Create a new entry in the collection.
 */
public class PostRequestHandler extends RequestHandler {

	
	public PostRequestHandler(PersonenModel model) {
		super(model);
	}

	
	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, String id)
	throws ServletException, IOException {
		
		// get writer and set the content type of the response to be XML
		PrintWriter responseWriter = response.getWriter();
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		
		try {
			if (id == null) {
				// add a person (single item of the collection)
				addPerson(request, response);
			}
			else {
				// this is generally not used
				String error = "This method type is not allowed for the address.";
				response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				responseWriter.println(failureMessage(error));
				return;
			}
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseWriter.println(failureMessage(e.getMessage()));
		}
	}

	
	/** Add a new person to the XML-Document. */
	private void addPerson(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		// add the person to the collection
		String id = addPerson(null, request.getReader());
		
		// marshal the XML-Document (save changes to the file)
		try {
			getModel().marshal();
		}
		catch (Exception e) {
			throw new Exception("Marshalling failed. (" + e.getMessage() + ")");
		}
		
		// print information about added person
		System.out.println("Person added (" + id + ").");
		
		// redirect and change method to GET
		//response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		//response.setHeader("Location", address);
		
		// TODO: works for now, maybe change it to a redirect later
		new GetRequestHandler(getModel()).handleRequest(request, response, id);
	}
	
	
	/**
	 * Add a new person using the passed body data.
	 * @param id - can be null
	 * @return The id of the added person
	 */
	public String addPerson(String id, BufferedReader data)
	throws Exception {
		
		// check if the data is available (not empty)
		if (!data.ready()) {
			throw new IOException("Missing body data!");
		}
		
		// try to unmarshal to a Person object
		PersonType newPerson = getModel().unmarshalPerson(new StreamSource(data));
		
		// check that IDs match if the id is given
		if (id != null && !newPerson.getId().equals(id)) {
			throw new Exception("IDs do not match!");
		}
		
		// add person to collection
		getModel().addPerson(newPerson);
		
		return newPerson.getId();
	}
}
