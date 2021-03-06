package br.com.getAllURLRecursive.Service;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Pedro Luiz da Silva Pereira
 * this class use to client java application to get recursive urls from initial url.
 */

public class Content {
private static final String HTML_A_HREF_TAG_PATTERN =
        "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
private Pattern pattern;
private Set<String> visitedUrls = new HashSet<String>();

public Content() {
    pattern = Pattern.compile(HTML_A_HREF_TAG_PATTERN);
}

private void fetchContentFromURL(String origin, String strLink) {
    String content = null;
    URLConnection connection = null;
    try {
        connection = new URL(strLink).openConnection();
        Scanner scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\Z");
        if (scanner.hasNext()) {
            content = scanner.next();
            visitedUrls.add(strLink);
            fetchURL(origin, content);
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

private void fetchURL(String origin, String content) {
    Matcher matcher = pattern.matcher(content);
    
    while (matcher.find()) {
        String group = matcher.group();
        if (group.toLowerCase().contains("http") || group.toLowerCase().contains("https")) {
            group = group.substring(group.indexOf("=") + 1);
            group = group.replaceAll("'", "");
            group = group.replaceAll("\"", "");
            System.out.println("origin: "+origin+" url "+ group);
            if (!visitedUrls.contains(group) && visitedUrls.size() < 200) {
            	String newOrigin = group;
                fetchContentFromURL(newOrigin,group);
            }
        }
    }
    System.out.println("DONE: "+origin);
}

/**
 * @param args
 */
public static void main(String[] args) {
    new Content().fetchContentFromURL("https://globoesporte.globo.com/","https://globoesporte.globo.com/");
}

}
