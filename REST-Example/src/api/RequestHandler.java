package api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import helpers.PersonenModel;


public abstract class RequestHandler {

	private final PersonenModel model;
	

	public RequestHandler(PersonenModel model) {
		this.model = model;
	}
	
	
	/** Get the model */
	public PersonenModel getModel() {
		return model;
	}

	
	public abstract void handleRequest(HttpServletRequest request, HttpServletResponse response, String id)
	throws ServletException, IOException;
	
	
	/**
	 * Returns a String array with elements of the path.
	 */
	public static String[] parsePath(String path) {
		
		if (path == null) { return new String[]{}; }
		
		//String[] pathSplit = request.getPathInfo().split("^/(0|[1-9]+[0-9]*)/.*$");
		String[] pathSplit = path.split("/");
		for (String part : pathSplit) {
			part.replaceAll("/", "");
		}
		
		return pathSplit;
	}
	
	
	/**
	 * Creates a success message in XML-Format.
	 */
	public static String successMessage() {
		
		StringBuilder xml = new StringBuilder();
		
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("\n<response>");
		xml.append("\n  <success>true</success>");
		xml.append("\n</response>");
		
		return xml.toString();
	}
	
	
	/**
	 * Creates a success message in XML-Format.
	 */
	public static String failureMessage(String reason) {
		
		StringBuilder xml = new StringBuilder();
		
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xml.append("\n<response>");
		xml.append("\n  <success>false</success>");
		xml.append("\n  <reason>" + reason + "</reason>");
		xml.append("\n</response>");
		
		return xml.toString();
	}
	
}
