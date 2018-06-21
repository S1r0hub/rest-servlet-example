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


/**
 * GET-Request Handler<br/>
 * 1. List the URIs and perhaps other details of the collection's members.<br/>
 * 2. Retrieve a representation of the addressed member of the collection
 */
public class GetRequestHandler extends RequestHandler {

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, PersonenModel model, int id)
	throws ServletException, IOException {
		
		// get writer and set the content type of the response to be XML
		PrintWriter responseWriter = response.getWriter();
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		
		// the returned element (the person)
		JAXBElement<?> jaxbElementOut = null;
		String error = "Element not found!";
		
		if (id >= 0) {
			try {
				// get person by id
				PersonType person = model.getPerson(id);
				jaxbElementOut = new ObjectFactory().createPerson(person);
			}
			catch (IndexOutOfBoundsException e) {
				error = "Person with id (" + id + ") does not exist!";
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseWriter.println(failureMessage(error));
				return;
			}
		}
		
		
		try {
			response.setStatus(HttpServletResponse.SC_OK);
			
			if (id > 0) {
				// return the person
				model.marshal(jaxbElementOut, responseWriter);
			}
			else {
				// return all people
				model.marshal(responseWriter);
			}
		}
		catch (JAXBException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseWriter.println(failureMessage("Marshalling failed!"));
		}
	}
	
}
