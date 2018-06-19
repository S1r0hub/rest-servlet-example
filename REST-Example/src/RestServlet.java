import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class RestServlet
 */
@WebServlet("/people")
public class RestServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	// List of persons
	private static People people = new People();
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// add some default persons
		/*
		persons.add(new Person("Bob", "Marley", "Singer"));
		persons.add(new Person("Elvis", "Presley", "Singer"));
		persons.add(new Person("Kobe", "Bryant", "Basketball Player"));
		persons.add(new Person("Alan Mathison", "Turing", " Computer Scientist, Mathematician, Logician, Cryptanalyst"));
		*/
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
		
		// set the content type of the response to return JSON
		response.setContentType("application/json");
		
		PrintWriter pw = response.getWriter();
		pw.append("...");
		pw.close();
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
