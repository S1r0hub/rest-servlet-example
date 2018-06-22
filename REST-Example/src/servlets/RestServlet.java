package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.DeleteRequestHandler;
import api.GetRequestHandler;
import api.PostRequestHandler;
import api.PutRequestHandler;
import api.RequestHandler;
import helpers.PersonenModel;


/**
 * Servlet implementation class RestServlet
 */
@WebServlet("/people/*")
public class RestServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// prepare the model that holds the data and represents the personen.xml document
	private static PersonenModel model = new PersonenModel("personen.xsd", "personen.xml");
	
	private String id = null;
	
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestServlet() {
        super();
    }

    
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// nothing to do
	}

	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter responseWriter = response.getWriter();
		
		// set the content type of the response to return XML
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");
		
		// set default of id to be negative
		id = null;
		
		// get the single parts of the URL/path
		String path = request.getPathInfo();
		String[] pathSplit = RequestHandler.parsePath(path);
		
		if (pathSplit.length == 2) {
			id = pathSplit[1];
			if (id.isEmpty()) { id = null; }
		}
		else if (pathSplit.length > 2) {
			String message = "Invalid path!";
			String error = RequestHandler.failureMessage(message);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseWriter.println(error);
			return;
		}
		
		// print request information to the console
		System.out.println(
			"[" + request.getMethod() + "] " +
			"Path: " + (path != null ? path : "/") + " , " +
			"Client: " + request.getRemoteAddr()
		);
		
		super.service(request, response);
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		new GetRequestHandler(model).handleRequest(request, response, id);
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		new PostRequestHandler(model).handleRequest(request, response, id);
	}

	
	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		new PutRequestHandler(model).handleRequest(request, response, id);
	}

	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		new DeleteRequestHandler(model).handleRequest(request, response, id);
	}

}
