package br.com.getAllURLRecursive.Cloudant;
import br.com.getAllURLRecursive.Cloudant.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;

/**
 * 
 * @author Pedro Luiz da Silva Pereira
 * Data: 15 may 2019
 * Proposal: this class has the proposal to working with No SQL Cloudant Database, 
 * the main functions: new document, read document, read all documents, delete all documents.
 */

public class API {
	
	/**
	 * 
	 * This method return the Client Cloudant Database Connection
	 */
	
	public static  CloudantClient cloudantClient() throws IOException
	{
		CloudantClient client = ClientBuilder.url(new URL("https://c0f2ad69-cb46-4003-96ce-6a5ba4d0eab8-bluemix.cloudant.com"/*prop.getProperty("bluemixcloudantserver").toString()*/))
                .username("athesereenstaterinsithis")
                .password("25bf821dc6ae37ac089c439301a202278f55324b")
                .build();
		return client;
	}
	
	/**
	 * 
	 * This method return the document by id document, in json format.
	 */
	
	private static String getDocument(String idDoc,CloudantClient client) throws IOException, JSONException
	{
		String tmp = "";
		
		InputStream document = client.database("listurl", false).find(idDoc);
		BufferedReader br = new BufferedReader(new InputStreamReader(document));
		String line = "";
       while ((line = br.readLine()) != null) {
           if (line.equalsIgnoreCase("quit")) {
               break;
           }
           tmp = tmp + line;
       }
		System.out.println(tmp);
		return tmp;
	}
	
	/**
	 * 
	 * This method return remove the document from id Document and connection Cloudant Database
	 * if success return true, else return false.
	 */
	private static Boolean removeDocument(CloudantClient client, String idDoc)
	{
		try{
			JSONObject my_obj = new JSONObject(getDocument(idDoc,client));
			Document findDoc = new Document();
			findDoc.setId(my_obj.getString("_id"));
			findDoc.set_rev(my_obj.getString("_rev"));
			findDoc.setSearch(my_obj.getString("search"));
			findDoc.setUrl(my_obj.getString("url"));
			client.database("listurl",false).remove(idDoc, findDoc.get_rev());
			return true;
		}catch(Exception ex)
		{
			System.out.println(ex.toString());
			return false;
		}
	}
	
	/**
	 * this method return all documents from Cloudant Database and generate JSON.
	 */
	
	public static String getAllDocs(CloudantClient client) throws IOException, JSONException, InterruptedException
	{

		String tmp = "{ \"records\":[";
		List<String> allDocs = client.database("listurl", false).getAllDocsRequestBuilder().build().getResponse().getDocIds();
		for (String doc : allDocs) {
			System.out.println("All my docs Id : "+doc);
			tmp = tmp + getDocument(doc,client)+",";
		}

		return (tmp+"]}").replace("},]}", "}]}");
	}
	
	/**
	 * this method is used after find the new link to save at Cloudant Database
	 */
	
	public void newDocument(CloudantClient client, String search, String url)
	{
		try{
			Document newDoc = new Document();
			newDoc.setId(null);
			newDoc.set_rev(null);
			newDoc.setSearch(search);
			newDoc.setUrl(url);
			client.database("listurl", false).post(newDoc); 
			
			//return true;
		}catch (Exception ex)
		{
			//return false;
		}
	}
	/**
	 * this method delete all documents form Cloudant Database
	 */
	
	public static  void deleteAllDocs() throws InterruptedException
	{
		CloudantClient client;
		try {
			client = cloudantClient();
			List<String> allDocs = client.database("listurl", false).getAllDocsRequestBuilder().build().getResponse().getDocIds();
			for (String doc : allDocs) {
				try {
					System.out.println("deleted do id: "+doc);
					JSONObject my_obj = new JSONObject(getDocument(doc,client));
					Document findDoc = new Document();
					findDoc.setId(my_obj.getString("_id"));

					removeDocument(client, findDoc.getId());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Deleted all Documents!");

	}
	
	/**
	 * this method is use if origin list of documents to save at Cloudant Database
	 */
	
	public void loadAllURLs(List<Document> docs) throws IOException, InterruptedException
	{
			CloudantClient cc = cloudantClient();
			for (Document doc : docs) {
				newDocument(cc,doc.getSearch(), doc.getUrl());
			}
	}
	
}
