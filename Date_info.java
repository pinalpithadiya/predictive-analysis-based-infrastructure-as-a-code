package xyz;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jdk.nashorn.internal.parser.Token;

/**
 * Servlet implementation class Date_info
 */
public class Date_info extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Date_info() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		PrintWriter out = response.getWriter();
		
		String date_1 = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());
		String time_1 = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
		List<String> Ids = new ArrayList<String>();
		
		//BufferedReader in = null;
		try {
		    		    
		    Scanner scanner = new Scanner(new File("C:\\eclipse-jee-mars-R-win32-x86_64\\StoredDataNew.txt"));
		            
		    while(scanner.hasNext()){
		        String[] tokens = scanner.nextLine().split("\t");
		        String date = tokens[tokens.length-2];
		        String time = tokens[tokens.length-1];
		        
		        String[] part_1 = date.split("/");
		        String[] part_2 = time.split(":");
		     
		        String id = null;
		       
		        
		        if (part_1[0].equals(date_1) && part_2[0].equals(time_1)){
		          	
		        		Ids.add(tokens[0]);
		        }
		      	           
		    }   
		    
		    Set<String> uniqueIds = new HashSet<String>(Ids);
		    
		    //out.println(uniqueIds.size());
		    
		    HttpSession session = request.getSession();
		    session.setAttribute("Number",uniqueIds.size());
		    response.sendRedirect("ScaleInstance");
		}              
		       
		
		    
		catch(Exception e){
			
			e.printStackTrace();
		} 
		    
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
