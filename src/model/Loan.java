package model;

public class Loan {
	
	//fields 
	private double principal;					// user: principal
	private double userInterest;				// user: annual interest 
	private double period; 						// user: payment period 
	
	
	String applicationName = "OSAP Calculator 2020";
	String applicantName = "Joe Doe";
	private final double fixedPrincipal = 10.0;
	private final double fixedPeriod = 24.0;
	private final double fixedInterest = 5.0;
	private final double gracePeriod = 6.0;
	
	private double interest = userInterest + fixedInterest;
	public double graceInterest;
	public double monthlyPayment;
	
	
	public Loan() {
		this.principal = 0.0;
		this.userInterest = 0.0;
		this.period = 0.0;
	}
	
//	public Loan(double principal, double userInterest, double period) {
//		this.principal = principal;
//		this.userInterest = userInterest;
//		this.period = period;
//		
//	}
	
	public double computePayment(String p, String a, String i, String g, String gp, String fi) throws Exception {
		principal = Double.parseDouble(p);
		userInterest = Double.parseDouble(i);
		interest = Double.parseDouble(i) + Double.parseDouble(fi);
		period = Double.parseDouble(g); 				// payment period
		double gpp = Double.parseDouble(gp); 			// grace-period = 6

		return (((interest / 100) / 12) * principal) / (1 - Math.pow(1 + ((interest / 100) / 12), -period));
	}
	
	public double computeGraceInterest(String p, String gp, String i, String fi) throws Exception {
		principal = Double.parseDouble(p);
		interest = Double.parseDouble (i) + Double.parseDouble(fi);
		period = Double.parseDouble(gp);    // graceperiod = 6 
		
		return  principal * ((interest / 100) / 12) * gracePeriod;
	}
	
	public static void main(String[] args) {
		Loan ln = new Loan();
		System.out.println("default_principal = " + ln.fixedPrincipal);
		System.out.println("default_period =  " + ln.fixedPeriod);
		System.out.println("default_interest =  " + ln.fixedInterest);
		System.out.println("------------------------------------------");
		System.out.println("principal " + ln.principal);
		System.out.println("period " + ln.period);
		System.out.println("user interest " + ln.userInterest);
		System.out.println("------------------------------------------");
		System.out.println("Grace Period = " + ln.gracePeriod);
		System.out.println("Grace Interest =  " + ln.graceInterest);
		System.out.println("------------------------------------------");
		System.out.println("Final calc: " + ln.monthlyPayment);
	
	}

}
