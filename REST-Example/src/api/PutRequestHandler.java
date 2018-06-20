package api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helpers.PersonenModel;


/**
 * PUT-Request Handler<br/>
 * 1. Replace the entire collection with another collection.
 * 2. Replace the addressed member of the collection, or if it does not exist, create it.
 */
public class PutRequestHandler extends RequestHandler {

	@Override
	public void handleRequest(HttpServletRequest request, HttpServletResponse response, PersonenModel model)
	throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}
