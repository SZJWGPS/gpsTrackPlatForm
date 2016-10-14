package com.hengdian.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddOrDelSpotPointsServlet
 */
@WebServlet("/addOrDelSpotPointsServlet")
public class AddOrDelSpotPointsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public AddOrDelSpotPointsServlet() {
        super();        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("=====adddel=========");
		new Thread(new Runnable() {			
			@Override
			public void run() {
				
				
			}
		}).start();
	}

}
