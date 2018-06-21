package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.GetRequestHandler;
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
	
	private int id = -1;
	
    
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
		id = -1;
		
		// get the single parts of the URL/path
		String[] pathSplit = RequestHandler.parsePath(request.getPathInfo());
		
		if (pathSplit.length == 2) {
			try {
				// get the id and remove the slash
				id = Integer.parseInt(pathSplit[1]);
			}
			catch (NumberFormatException e) {
				String message = "Failed to parse id (" + pathSplit[1] + ")! Wrong type.";
				String error = RequestHandler.failureMessage(message);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				responseWriter.println(error);
				return;
			}
		}
		else if (pathSplit.length > 2) {
			String message = "Not Found!";
			String error = RequestHandler.failureMessage(message);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseWriter.println(error);
			return;
		}
		
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
		// TODO Auto-generated method stub
		doGet(request, response);
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
		// TODO Auto-generated method stub
	}

}
