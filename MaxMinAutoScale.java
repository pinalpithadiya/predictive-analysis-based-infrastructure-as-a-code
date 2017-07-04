package xyz;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
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
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * Servlet implementation class MaxMinAutoScale
 */
public class MaxMinAutoScale extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static String profileName = "default";
	private static Regions region = Regions.AP_SOUTH_1;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MaxMinAutoScale() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
 
		float y = (float) session.getAttribute("max");
		float z = (float) session.getAttribute("min");
		int index = (int) session.getAttribute("index");
		List<String> id = new ArrayList<>();
		id = (List<String>) session.getAttribute("IstanceId");
						
		
		if(y>70){ 

			        ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider(profileName);
					AmazonEC2Client amazonEC2Client = new AmazonEC2Client(credentialsProvider);
					amazonEC2Client.setRegion(Region.getRegion(region));
					
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
			         
			         response.sendRedirect("go.jsp");
			  // OR ---> Start Already Exist Instance.. 
			 }
			  
		else if(z<20){
			  
			String instanceId = (String) id.get(index); 
			
			ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider(profileName);
			AmazonEC2Client amazonEC2Client = new AmazonEC2Client(credentialsProvider);
			amazonEC2Client.setRegion(Region.getRegion(region));
							   
			TerminateInstancesRequest instanceRequest = new TerminateInstancesRequest().withInstanceIds(instanceId);
		    amazonEC2Client.terminateInstances(instanceRequest);
		    
		    response.sendRedirect("Done.jsp");
			  			  
			  // OR ---> Stop Instance
			  }
			 
		else{
			response.sendRedirect("done.jsp");
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

}
