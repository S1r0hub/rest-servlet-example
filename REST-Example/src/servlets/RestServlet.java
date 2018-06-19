package servlets;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import helpers.PersonenModel;


/**
 * Servlet implementation class RestServlet
 */
@WebServlet("/people")
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
		
		/*
		// add all the persons to the attribute "persons"
		request.setAttribute("persons", "value");
		request.getRequestDispatcher("page.jsp").forward(request, response);
		*/
		
		PrintWriter responseWriter = response.getWriter();
		
		// set the content type of the response to return JSON
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");

		try {
			response.setStatus(HttpServletResponse.SC_OK);
			model.marshal(responseWriter);
		} catch (JAXBException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseWriter.println("Marshalling failed!");
		}

		responseWriter.close();
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
		// TODO Auto-generated method stub
	}

	
	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
