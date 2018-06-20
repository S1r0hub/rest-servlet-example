package servlets;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.GetRequestHandler;
import api.PutRequestHandler;
import helpers.PersonenModel;


/**
 * Servlet implementation class RestServlet
 */
@WebServlet("/people/*")
public class RestServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// prepare the model that holds the data and represents the personen.xml document
	private static PersonenModel model = new PersonenModel("personen.xsd", "personen.xml");
	
    
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

	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		new GetRequestHandler().handleRequest(request, response, model);
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
		new PutRequestHandler().handleRequest(request, response, model);
	}

	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
