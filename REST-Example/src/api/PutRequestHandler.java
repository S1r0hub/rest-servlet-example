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
import jaxb.Personen;


/**
 * PUT-Request Handler<br/>
 * 1. Replace the entire collection with another collection.<br/>
 * 2. Replace the addressed member of the collection, or if it does not exist, create it.
 */
public class PutRequestHandler extends RequestHandler {
	
	
	public PutRequestHandler(PersonenModel model) {
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
				// replace the whole collection
				replaceCollection(request, response);
			}
			else {
				// replace a person (single item of the collection)
				replacePerson(id, request, response);
			}
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseWriter.println(failureMessage(e.getMessage()));
		}
	}
	
	
	/** Replace existing person. */
	private void replacePerson(String id, HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		boolean replaced = false;
		
		// check if person exists
		if (getModel().existsPerson(id)) {
			// replace person data and validate it
			replacePerson(id, request.getReader());
			replaced = true;
		}
		else {
			// add person data and validate it
			//addPerson(id, request.getReader());
			new PostRequestHandler(getModel()).addPerson(id, request.getReader());
		}
		
		// marshal the XML-Document (save changes to the file)
		try {
			getModel().marshal();
		}
		catch (Exception e) {
			throw new Exception("Marshalling failed. (" + e.getMessage() + ")");
		}
		
		// print information to console
		if (replaced) {
			System.out.println("Person replaced (" + id + ").");
		}
		else {
			System.out.println("Person added (" + id + ").");
		}
		
		// redirect and change method to GET
		//response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		//response.setHeader("Location", address);
		
		// TODO: works for now, maybe change it to a redirect later
		new GetRequestHandler(getModel()).handleRequest(request, response, id);
	}
	
	/**
	 * Replace an existing person using the passed body data.<br/>
	 * (Will not write changes to the XML-Document)
	 */
	public void replacePerson(String id, BufferedReader data)
	throws Exception {
		
		/*
		// validate the XML data
		// to use this functionality, first store the data in a String instance
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder xmlBuilder = factory.newDocumentBuilder();
		
		try { xmlBuilder.parse(new InputSource(data)); }
		catch (Exception e) { throw new IOException("Data is invalid!"); }
		*/
		
		// check if the data is available (not empty)
		if (!data.ready()) {
			throw new IOException("Missing body data!");
		}
		
		// try to unmarshal to a Person object
		PersonType newPerson = getModel().unmarshalPerson(new StreamSource(data));
		
		// check that IDs match
		if (!newPerson.getId().equals(id)) {
			throw new Exception("IDs do not match!");
		}
		
		getModel().replacePerson(id, newPerson);
	}

	
	/** Replace the whole collection (write to XML-Document). */
	private void replaceCollection(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		// replace the collection with the body data
		replaceCollection(request.getReader());
		
		// marshal the changed XML-Document (save changes to file)
		try {
			getModel().marshal();
		}
		catch (Exception e) {
			throw new Exception("Marshalling failed. (" + e.getMessage() + ")");
		}
		
		// print information that a person was replaced
		System.out.println("Collection replaced.");
		
		// redirect and change method to GET
		//response.setStatus(HttpServletResponse.SC_SEE_OTHER);
		//response.setHeader("Location", address);
		
		// TODO: works for now, maybe change it to a redirect later
		new GetRequestHandler(getModel()).handleRequest(request, response, null);
	}
	
	
	/**
	 * Replace the whole collection using the passed body data.
	 * (Will not write changes to the XML-Document)
	 */
	public void replaceCollection(BufferedReader data)
	throws Exception {
		
		// check if the data is available (not empty)
		if (!data.ready()) {
			throw new IOException("Missing body data!");
		}
		
		// try to unmarshal to a "Personen" object
		Personen newCollection = getModel().unmarshal(new StreamSource(data));
		System.out.println("new col size = " + newCollection.getPerson().size());
		getModel().replaceCollection(newCollection);
	}
	
	
	/*
	// Replace an existing person using request parameters.
	private void replacePerson(int id, HttpServletRequest request)
	throws Exception {
		
		if (request.getParameterMap().keySet().size() == 0) {
			throw new Exception("No parameters given!");
		}
		
		// get parameters
		String name = request.getParameter("name");
		String surname = request.getParameter("nachname");
		String birthday = request.getParameter("geburtstag");
		
		// get jobs and set default value to be nothing
		String jobs = request.getParameter("berufe");
		if (jobs == null) { jobs = ""; }
		
		// add jobs separated by comma
		List<String> jobList = new ArrayList<>();
		for (String job : jobs.split(",")) {
			jobList.add(job);
		}
		
		// replace the person and validate it
		getModel().replacePerson(id, name, surname, birthday, jobList);
	}
	*/
	
}
