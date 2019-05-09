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
	
	private static final String HTML_A_HREF_TAG_PATTERN =
	        "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	private Pattern pattern;
	private Set<String> visitedUrls = new HashSet<String>();

	public SearchProcess() {
	    pattern = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
	}

	private String fetchContentFromURL(String strLink) {
	    String content = null;
	    URLConnection connection = null;
	    try {
	        connection = new URL(strLink).openConnection();
	        Scanner scanner = new Scanner(connection.getInputStream());
	        scanner.useDelimiter("\\Z");
	        if (scanner.hasNext()) {
	            content = scanner.next();
	            visitedUrls.add(strLink);
	           content += fetchURL(content) +"<br/>";
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return content;
	}

	private String fetchURL(String content) {
		String tmp = null;
	    Matcher matcher = pattern.matcher(content);
	    while (matcher.find()) {
	        String group = matcher.group();
	        if (group.toLowerCase().contains("http") || group.toLowerCase().contains("https")) {
	            group = group.substring(group.indexOf("=") + 1);
	            group = group.replaceAll("'", "");
	            group = group.replaceAll("\"", "");
	            tmp +=  "lINK " + group +"<br/>";
	            //System.out.println("lINK " + group);
	            if (!visitedUrls.contains(group) && visitedUrls.size() < 200) {
	                fetchContentFromURL(group);
	            }
	        }
	    }
	    
	    //System.out.println("DONE");
	    return tmp;
	}

	
	
	  @Override
	  protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			    throws ServletException, IOException {
		  
		  response.getWriter().write(new SearchProcess().fetchContentFromURL(request.getParameter("search")));
	  }
}
