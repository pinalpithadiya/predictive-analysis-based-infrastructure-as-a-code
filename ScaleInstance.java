package xyz;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;

/**
 * Servlet implementation class ScaleInstance
 */
public class ScaleInstance extends HttpServlet {
	
	private static String profileName = "default";
	private static Regions region = Regions.AP_SOUTH_1;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScaleInstance() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider(profileName);
		AmazonEC2Client amazonEC2Client = new AmazonEC2Client(credentialsProvider);
		amazonEC2Client.setRegion(Region.getRegion(region));
		
		List<String> Ids = new ArrayList<String>();
		 HttpSession session = request.getSession();
		   int i = (Integer) session.getAttribute("Number");
		   String state = null;
		  
		   
		   
		DescribeInstancesResult describeInstances = amazonEC2Client.describeInstances();
       List<Reservation> reservations = describeInstances.getReservations();
     
       for (Reservation reservation : reservations) {
           List<Instance> instances = reservation.getInstances();
            for (Instance instance : instances) {
            	  state = instance.getState().toString();
            	 // System.out.println(state);
            	  
            	  if(state.equals("{Code: 16,Name: running}")){
            		  
                       Ids.add(instance.getInstanceId());                                               
                   }
            }
       }
       
       PrintWriter out = response.getWriter();
       int j = Ids.size();
       
      // out.println(Ids.size());
        System.out.println(i);   
        System.out.println(j);
       
        if(i < j){
        
    	   for(int k = j; k > i; k--){
    		   String instanceId = Ids.get(k);
   			   TerminateInstancesRequest instanceRequest = new TerminateInstancesRequest().withInstanceIds(instanceId);
   		       amazonEC2Client.terminateInstances(instanceRequest);
    	   }
    	   response.sendRedirect("go.jsp");
       }
       
       else if(i > j){
    	   for(int k = j; k < i; k++){
    		   
    		   List<String> securitygroup = new ArrayList<String>();
		         securitygroup.add("MySecurityGroup");
		
	             RunInstancesRequest instancesRequest = new RunInstancesRequest();
		         instancesRequest.setImageId("ami-f9daac96");
		         instancesRequest.setInstanceType(InstanceType.T2Micro);
		         instancesRequest.setMinCount(1);
		         instancesRequest.setMaxCount(1);
		         instancesRequest.setKeyName("test");
		         instancesRequest.setSecurityGroups(securitygroup);
		
		         amazonEC2Client.runInstances(instancesRequest);
    	   }
    	   
    	   response.sendRedirect("go.jsp");
       }
       
       else{
    	   response.sendRedirect("go.jsp");   	  
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
