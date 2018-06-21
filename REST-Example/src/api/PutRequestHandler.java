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
 * PUT-Request Handler<br/>
 * 1. Replace the entire collection with another collection.
 * 2. Replace the addressed member of the collection, or if it does not exist, create it.
 */
public class PutRequestHandler extends RequestHandler {
	
	
	public PutRequestHandler(PersonenModel model) {
		super(model);
	}


	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, int id)
	throws ServletException, IOException {

		// get writer and set the content type of the response to be XML
		PrintWriter responseWriter = response.getWriter();
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		
		try {
			if (id < 0) {
				// replace the whole collection
				// TODO
			}
			else {
				// replace a person (item of the collection)
				if (getModel().existsPerson(id)) {
					
					try {
						// replace person data and validate it
						replacePerson(id, request.getReader());
						
						// marshal the XML-Document (save changes to the file)
						getModel().marshal();
					}
					catch (Exception e) {
						//e.printStackTrace();
						throw new Exception("Marshalling failed. (" + e.getMessage() + ")");
					}
					
					// redirect the client to the created person
					//String address = request.getContextPath() + request.getServletPath() + "/" + id;
					System.out.println("Client replaced person with id (" + id + ") successfully.\nRedirecting...");
					
					// redirect and change method to GET
					//response.setStatus(HttpServletResponse.SC_SEE_OTHER);
					//response.setHeader("Location", address);
					
					// TODO: works for now, maybe change it do a redirect later
					new GetRequestHandler(getModel()).handleRequest(request, response, id);
				}
			}
		}
		catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseWriter.println(failureMessage(e.getMessage()));
		}
		
	}
	
	
	/** Replace an existing person using the passed body data. */
	private void replacePerson(int id, BufferedReader data)
	throws Exception {
		
		// validate the data
		/*
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder xmlBuilder = factory.newDocumentBuilder();
		
		try { xmlBuilder.parse(new InputSource(data)); }
		catch (Exception e) { throw new IOException("Data is invalid!"); }
		*/
		
		// try to unmarshal to a Person object
		PersonType newPerson = getModel().unmarshal(new StreamSource(data));
		getModel().replacePerson(id, newPerson);
	}

	
	/** Replace an existing person using parameters. */
	/*
	private void replacePerson(int id, HttpServletRequest request)
	throws Exception {
		
		if (request.getParameterMap().keySet().size() == 0) {
			throw new Exception("No parameters given!");
		}
		
		// get parameters
		String name = request.getParameter("name");
		String surname = request.getParameter("surname");
		String birthday = request.getParameter("birthday");
		
		// get jobs and set default value to be nothing
		String jobs = request.getParameter("jobs");
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
