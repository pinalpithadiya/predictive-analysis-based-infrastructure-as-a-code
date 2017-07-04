package xyz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.Volume;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;

import jxl.Sheet;
import jxl.Workbook;



/**
 * Servlet implementation class Connect
 */
public class Connect1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Connect1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		 
		HttpSession session = request.getSession();
		List<String> ips = new ArrayList<>();
		List<String> id = new ArrayList<>();
		List<String> types = new ArrayList<>();
		ips = (List<String>) session.getAttribute("PublicAdreess");
		id = (List<String>) session.getAttribute("IstanceIds");
		types = (List<String>) session.getAttribute("Types");
		
		System.out.println("ip size :: " + ips.size());
		//System.out.println("id size :: " + id.size());
		List<Integer> k = new ArrayList<>();
		boolean check = true;
		List<Float> val = new ArrayList<Float>();
		//float arr[] = new float[4];
			    						
		String p = null;
		String t = null;
		String cpu = null;
		String g = null;
		float a = 0;
		
		String host = null;
		String date = null;
		String time = null;
		float size = 0;
		
		for (int i = 0; i < ips.size(); i++) {
			try {
 			
				JSch jsch = new JSch();
                              
				String user = "ec2-user";
				host = ips.get(i);
				System.out.println(host);
				int port = 22;
				String privateKey = "C:\\eclipse-jee-mars-R-win32-x86_64\\test.pem";
				String command1 = "grep 'cpu' /proc/stat| awk '{usage=($2+$4)*100/($2+$5+$4)}END {print usage}' ";
				String command2 = "awk '/MemTotal/ {print $2}' /proc/meminfo";
				jsch.addIdentity(privateKey);
				System.out.println("identity added ");

				com.jcraft.jsch.Session jschsession = jsch.getSession(user, host, port);
				System.out.println("session created.");

				java.util.Properties config = new java.util.Properties();
				config.put("StrictHostKeyChecking", "no");
				jschsession.setConfig(config);

				jschsession.connect();
				com.jcraft.jsch.Channel channel = jschsession.openChannel("exec");
				((ChannelExec) channel).setCommand(command1);
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);

				InputStream in = channel.getInputStream();
				channel.connect();
				byte[] tmp = new byte[1024];
				while (check) {
					while (in.available() > 0) {
						int m = in.read(tmp, 0, 1024);
						if (m < 0)
							break;
						 g = (new String(tmp, 0, m));
						 a = Float.parseFloat(g);
						 
						System.out.println("value in float :: " + a);
					   val.add(a);
					}
					if (channel.isClosed()) {
/*						System.out.println(check);
						System.out.println("exit-status: " + channel.getExitStatus());
*/						break;
					}
					try {
						Thread.sleep(1000);
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
				channel.disconnect();
				//jschsession.disconnect();
				
							
				
				com.jcraft.jsch.Channel channel_2 = jschsession.openChannel("exec");
				((ChannelExec) channel_2).setCommand(command2);
				channel_2.setInputStream(null);
				((ChannelExec) channel_2).setErrStream(System.err);

				InputStream in_2 = channel_2.getInputStream();
				channel_2.connect();
				byte[] tmp_2 = new byte[1024];
				while (check) {
					while (in_2.available() > 0) {
						int m_2 = in_2.read(tmp_2, 0, 1024);
						if (m_2 < 0)
							break;
						 String bb = (new String(tmp_2, 0, m_2));
						 float bb_1 = Float.parseFloat(bb);
						 size = bb_1/1024;
						 System.out.println(size);
 
					}
					if (channel_2.isClosed()) {
/*						System.out.println(check);
						System.out.println("exit-status: " + channel.getExitStatus());
*/						break;
					}
					try {
						Thread.sleep(1000);
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
				channel_2.disconnect();
				jschsession.disconnect();
				
		
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			p = (String) id.get(i);
			t = (String) types.get(i);	
			date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
			time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			
			FileWriter fw = new FileWriter("C:\\eclipse-jee-mars-R-win32-x86_64\\StoredDataNew.txt",true);  
			BufferedWriter bw  = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);	
			
		      
			  
			   pw.println(p + "\t" + t + "\t" + size + "\t" + a + "\t" + date + "\t" + time);
			   pw.close();
		       bw.close();
			   fw.close();
				
			
			
			
			
			
		}
		
		Float y = Collections.max(val);
		Float z = Collections.min(val);
		int index = val.indexOf(z);
		session.setAttribute("max", y);
		session.setAttribute("min", z);
		session.setAttribute("index", index);
		session.setAttribute("IstanceId",id);
		response.sendRedirect("MaxMinAutoScale");
		
		System.out.println(y);
		System.out.println(z);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
