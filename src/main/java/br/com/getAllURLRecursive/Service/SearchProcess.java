package br.com.getAllURLRecursive.Service;

import br.com.getAllURLRecursive.Cloudant.API;
import br.com.getAllURLRecursive.Cloudant.Document;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.cloudant.client.api.CloudantClient;
/**
 * Servlet implementation class SearchProcess
 */
@WebServlet("/searchProcess")
public class SearchProcess extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	private Pattern pattern;
	private Set<String> visitedUrls = new HashSet<String>();
	private CoreProcess cp = new CoreProcess();
	private API api = new API();
	private List<Document> documents = new ArrayList();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
	public SearchProcess() {
		pattern = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean stop = false;
		
		CloudantClient cc = API.cloudantClient();
		try {
			API.deleteAllDocs();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.getWriter().write("<br><h5 class='title text-success'>Collecting all links from the initial URL: </h5><p>"+request.getParameter("search")+"</p>");
		response.getWriter().write("<ul class='list-group'>");

		String strLink = request.getParameter("search");
		if (strLink.contains("http")){}else{strLink = "http://"+strLink;}
		long t= System.currentTimeMillis();
		long end = t+15000;
		
		int count=0;
		
		while( (stop == false) && ((System.currentTimeMillis() < end))) {
			String content = null;
			URLConnection connection = null;
			try {
				connection = new URL(strLink).openConnection();
				Scanner scanner = new Scanner(connection.getInputStream());
				scanner.useDelimiter("\\Z");
				if (scanner.hasNext()) {
					content = scanner.next();
					visitedUrls.add(strLink);
					Matcher matcher = pattern.matcher(content);

					while (matcher.find()) {
						String group = matcher.group();
						if (group.toLowerCase().contains("http")
								|| group.toLowerCase().contains("https")) {
							group = group.substring(group.indexOf("=") + 1);
							group = group.replaceAll("'", "");
							group = group.replaceAll("\"", "");
							if (count<10)
							{
								Document document = new Document();
								document.setSearch(strLink);
								document.setUrl(group);
								document.set_rev(null);
								document.setId(null);
								
								api.newDocument(cc,document.getSearch(),document.getUrl());
							}
							count += 1;
							
							/**
							documents.add(document);**/
							
							response.getWriter().write(cp.loadResponse(group));// + "<br>");
							if (!visitedUrls.contains(group)
									&& visitedUrls.size() < 200) {
								strLink = group;
								end = end+15000;
								stop = false;
							} else {
								stop = true;
							}
						}
					}
				}
			} catch (Exception ex) {

				stop = cp.IsValidURL(strLink);
				if (stop)
				{
					response.getWriter().write(cp.loadResponse("invalid or timeout connection url"));
				} else
				{
					strLink = strLink.replace("http:", "https:");
				}
			}
		}
		response.getWriter().write("</ul>");
		/**
		if (!documents.isEmpty())
		{
			api.loadAllURLs(documents);
		}**/

	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
