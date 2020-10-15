package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Loan;

/**
 * Servlet implementation class Osap
 */
@WebServlet({ "/Osap", "/Osap/*"  })						// * means -> all url with osap  
public class Osap extends HttpServlet {
	private static final long serialVersionUID = 1L;
	double graceInterest = 0, payment = 0;					// check this out this is the new  computation system
	Loan ln =  new Loan();  								// method call for loan
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Osap() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = getServletContext();

		String principal = context.getInitParameter("principal");
		String userInterest = context.getInitParameter("interest");
		String period = context.getInitParameter("period");

		// Instantiate Loan object
		context.setAttribute("mLoan", new Loan());
	}
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		/* 
		 * input from web.xml [this are the default/built-in/hard-coded values]
		 * these reads the web.xml file and returns the parameter value of the param-name
		 * */
		String default_principal = this.getServletContext().getInitParameter("principal"); 																									 
		String default_period = this.getServletContext().getInitParameter("period");
		String fixed_interest = this.getServletContext().getInitParameter("fixed interest");
		String gracePeriod = this.getServletContext().getInitParameter("grace period");
		
		/*
		 * user values, servlet retrieving data from the form, [user input the values]
		 */
		String principal = request.getParameter("principal");
		String userInterest = request.getParameter("interest");
		String period = request.getParameter("period"); 				// grace period
		
		//exception
		String errorMsg = "";
		
		if (request.getParameter("calculate") == null ||
				Double.parseDouble(principal) < 0 || Double.parseDouble(userInterest) < 0 || Double.parseDouble(period) < 0) {
			
				if (Double.parseDouble(request.getParameter("principal")) <= 0 ) {
					errorMsg = "Principal must be greater than 0!";
				} else if (Double.parseDouble(request.getParameter("interest")) <= 0) {
					errorMsg = "Annual Interest rate must be greater than 0!";
				} else if (Double.parseDouble(request.getParameter("period")) <= 0) {
					errorMsg = "Payment Period rate must be greater than 0!";
				} else {
					errorMsg = "";
				}
			
			request.getServletContext().setAttribute("errorMessage", errorMsg);
			request.getSession().setAttribute("errorMessage", errorMsg);
			request.getRequestDispatcher("/UI.jspx").forward(request, response);  // send to UI page 

		} else if (request.getParameter("calculate") == null || 
				Double.parseDouble(principal) > 0 || Double.parseDouble(userInterest) > 0 || Double.parseDouble(period) > 0) {
			
			response.getWriter().append("Served at: ").append(request.getContextPath());
			response.setContentType("text/plain");
			Writer resOut = response.getWriter();
			ServletContext context = this.getServletContext();
			String appName = this.getServletContext().getInitParameter("applicationName"); // application name
			String applicant = this.getServletContext().getInitParameter("applicantName"); // applicant Name
			resOut.write("\n"); 															// optional for new line
			resOut.write("Hello, World!\n");
			String clientIP = request.getRemoteAddr(); 										// client IP
			int cPort = request.getLocalPort(); 											// client port or request.getServerPort();
			String cProtocol = request.getProtocol(); 										// protocol
			String cMethod = request.getMethod(); 											// method
			String clientQueryString = request.getQueryString(); 							// querystring
			String foo = request.getParameter("foo"); 
			String uri = request.getRequestURI().toString(); 								// URI
			String servletPath = request.getServletPath(); 									// servlet path [this reads the web.xml file]
			String contextPath = context.getContextPath(); 									// context path
			String realPath = context.getRealPath("Osap"); 									// real path			

	
				// input from query String [these are the user input values]
				if (request.getParameterMap().isEmpty()) {
					principal = default_principal; 				// the default values
					period = default_period;
					userInterest = fixed_interest;
				} else {
					principal = principal; 						// the user values
					period = period;
					userInterest = userInterest; 
				}
			
				try {
					if (request.getParameter("inputGrace") == null) {
						graceInterest  = 0;
					} else {
						graceInterest = ln.computeGraceInterest(principal, gracePeriod, userInterest, fixed_interest);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					if (request.getParameter("inputGrace") != null) {
						payment = ln.computePayment(principal, userInterest, userInterest, period, gracePeriod, fixed_interest) + 
								((graceInterest) / Double.parseDouble(gracePeriod));
					} else {
						payment = ln.computePayment(principal, userInterest, userInterest, period, gracePeriod, fixed_interest);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			
			// task E : save session, how do you get the data from servlet into the results page
			HttpSession session = request.getSession();
			request.getServletContext().setAttribute("APPNAME", appName);
			request.getSession().setAttribute("APPNAME", appName);
			
			request.getServletContext().setAttribute("PRINCIPAL", principal);
			request.getSession().setAttribute("PRINCIPAL", principal);
			
			request.getServletContext().setAttribute("ANNUALINTEREST",userInterest);
			request.getSession().setAttribute("ANNUALINTEREST",userInterest);

			request.getServletContext().setAttribute("PAYMENTPERIOD", period );
			request.getSession().setAttribute("PAYMENTPERIOD", period );
			
			request.getServletContext().setAttribute("GI", graceInterest);
			request.getSession().setAttribute("GI", graceInterest);
			
			request.getServletContext().setAttribute("PAY", payment);
			request.getSession().setAttribute("PAY", payment);

			//check
			session.getServletContext().setAttribute("PRINCIPAL", principal);
			session.getServletContext().setAttribute("ANNUALINTEREST",userInterest);
			session.getServletContext().setAttribute("PAYMENTPERIOD", period );
			session.getServletContext().setAttribute("GI", graceInterest);
			session.getServletContext().setAttribute("PAY", payment);
			
			request.getRequestDispatcher("/Results.jspx").forward(request, response);		// send to results page 
		
		} else if (request.getParameter("restart") == null) {
			request.getRequestDispatcher("/UI.jspx").forward(request, response);  // send to UI page 
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}

//debugs 
//System.out.println("--------------default values----------------------------");
//System.out.println("default_principal (trivial) = " + default_principal);
//System.out.println("default_period (trivial) =  " + default_period);
//System.out.println("fixed_interest =  " + fixed_interest);
//System.out.println("grace period =  " + gracePeriod);
//System.out.println("--------------user values----------------------------");
//System.out.println("principal " + principal);
//System.out.println("period " + period);
//System.out.println("user interest " + userInterest);
//System.out.println("------------------------------------------");
//System.out.println("Grace Period = " + gracePeriod);
//System.out.println("Grace Interest =  " + graceInterest);
//System.out.println("------------------------------------------");
//System.out.println("------------------------------------------");
//System.out.println("Final calc: " + payment);
//System.out.println("------------------------------------------");
//System.out.println("Content length = " + request.getContentLength() + "\n" + "Content type = " + request.getContentType());
//System.out.println("Hello, Got a " + cMethod + " request from Osap!");    
