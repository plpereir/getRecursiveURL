package br.com.getAllURLRecursive.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/searchProcess")
public class SearchProcess extends HttpServlet{
	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			    throws ServletException, IOException {
		  response.getWriter().write("Hello World");
	  }
}
