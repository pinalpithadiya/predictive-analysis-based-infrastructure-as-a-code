package xyz;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;

/**
 * Servlet implementation class ListOfInstance
 */
public class ListOfInstanceByRegion extends HttpServlet {
	
	private static String profileName = "default";
	private static Regions region = Regions.AP_SOUTH_1;
	
	private static final long serialVersionUID = 1L;
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
		List<String> Ids = new ArrayList<String>();
		 List<String> Addr = new ArrayList<String>();
		 List<String> Types = new ArrayList<String>();
		  
		
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider(profileName);
		AmazonEC2Client amazonEC2Client = new AmazonEC2Client(credentialsProvider);
		amazonEC2Client.setRegion(Region.getRegion(region));
 
		DescribeInstancesResult describeInstances = amazonEC2Client.describeInstances();
       List<Reservation> reservations = describeInstances.getReservations();
     
       for (Reservation reservation : reservations) {
           List<Instance> instances = reservation.getInstances();
            for (Instance instance : instances) {
               // out.println("Instance Id :: " + instance.getInstanceId());
              //  out.println("Instance Public Adress:: " + instance.getPublicIpAddress());
                Ids.add(instance.getInstanceId());
                Addr.add(instance.getPublicIpAddress());
                Types.add(instance.getInstanceType());
                
                                 
            }
       }
       
       System.out.println("Size of addr :: "+Addr.size());
             
       HttpSession session = request.getSession();
       session.setAttribute("IstanceIds",Ids);
       session.setAttribute("PublicAdreess",Addr);
       session.setAttribute("Types", Types);
       response.sendRedirect("Connect1");
      
       
       		
		
	}

}