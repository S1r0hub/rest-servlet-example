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
 * PUT-Request Handler<br/>
 * 1. Replace the entire collection with another collection.
 * 2. Replace the addressed member of the collection, or if it does not exist, create it.
 */
public class PutRequestHandler extends RequestHandler {

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, PersonenModel model, int id)
	throws ServletException, IOException {

		JAXBElement<?> jaxbElementOut = null;
		String error = "Element not found!";
		
		// get the single parts of the URL/path
		String[] pathSplit = parsePath(request.getPathInfo());
		
		if (pathSplit.length > 0) {
			try {
				// get the id and remove the slash
				id = Integer.parseInt(pathSplit[1]);
				
				// get person by id
				PersonType person = model.getPerson(id);
				jaxbElementOut = new ObjectFactory().createPerson(person);
			}
			catch (NumberFormatException e) {
				error = "Failed to parse id (" + id + ")! Wrong type.";
			}
			catch (IndexOutOfBoundsException e) {
				error = "Person with id (" + id + ") does not exist!";
			}
		}
		
		
		PrintWriter responseWriter = response.getWriter();
		
		// set the content type of the response to return XML
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		
		try {
			response.setStatus(HttpServletResponse.SC_OK);
			
			// get all persons if id is negative
			if (id < 0) {
				model.marshal(responseWriter);
			}
			else {
				if (jaxbElementOut == null) {
					throw new IndexOutOfBoundsException(error);
				}
				model.marshal(jaxbElementOut, responseWriter);
			}
		}
		catch (JAXBException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseWriter.println(failureMessage("Marshalling failed!"));
		}
		catch (IndexOutOfBoundsException e) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseWriter.println(failureMessage(e.getMessage()));
		}

		responseWriter.close();
	}

}
