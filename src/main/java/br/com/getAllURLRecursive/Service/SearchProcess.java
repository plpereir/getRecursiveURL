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
public class SearchProcess extends HttpServlet {
	private static final String HTML_A_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	private Pattern pattern;
	private Set<String> visitedUrls = new HashSet<String>();
	private CoreProcess cp = new CoreProcess();
	public SearchProcess() {
		pattern = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
	}


	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		boolean stop = false;

		response.getWriter().write("<h5 class='title text-success'>Collecting all links from the initial URL: </h5><p>"+request.getParameter("search")+"</p>");
		response.getWriter().write("<ul class='list-group'>");

		String strLink = request.getParameter("search");
		if (strLink.contains("http")){}else{strLink = "http://"+strLink;}
			
		while( (stop == false)) {
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
							response.getWriter().write(cp.loadResponse(group));// + "<br>");
							if (!visitedUrls.contains(group)
									&& visitedUrls.size() < 200) {
								strLink = group;
								stop = false;
							} else {
								response.getWriter().write(cp.loadResponse("invalid or timeout connection url"));
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
				}
				
			}
		}
		response.getWriter().write("</ul>");
	}
}
