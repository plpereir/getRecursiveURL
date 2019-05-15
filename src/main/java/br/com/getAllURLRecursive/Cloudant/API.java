package br.com.getAllURLRecursive.Cloudant;
import br.com.getAllURLRecursive.Cloudant.Document;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;


public class API {
	
	private static CloudantClient cloudantClient() throws IOException
	{
//		Properties prop=new Properties(); 
//		FileInputStream ip= new FileInputStream("config.properties");
//		prop.load(ip);
		CloudantClient client = ClientBuilder.url(new URL("https://c0f2ad69-cb46-4003-96ce-6a5ba4d0eab8-bluemix.cloudant.com"/*prop.getProperty("bluemixcloudantserver").toString()*/))
                .username("athesereenstaterinsithis")
                .password("25bf821dc6ae37ac089c439301a202278f55324b")
                .build();
		return client;
	}
	
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
	
	private static String getAllDocs(CloudantClient client) throws IOException, JSONException
	{
		String tmp = "{ \"records\":[";
		List<String> allDocs = client.database("listurl", false).getAllDocsRequestBuilder().build().getResponse().getDocIds();
		for (String doc : allDocs) {
			System.out.println("All my docs Id : "+doc);
			tmp = tmp + getDocument(doc,client)+",";
		}

		return (tmp+"]}").replace("},]}", "}]}");
	}
	
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
	
	public static void deleteAllDocs()
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
	
	public void loadAllURLs(List<Document> docs)
	{
		try {
			CloudantClient cc = cloudantClient();
			for (Document doc : docs) {
				newDocument(cc,doc.getSearch(), doc.getUrl());
				try {
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	
	

	public static void main(String[] args) throws IOException, JSONException {
	// TODO Auto-generated method stub
	//	newDocument(cloudantClient(),"testando 0040","testando 58484");
	//	getDocument( "05cbaea46e2c66de3301bdf9250f20df",cloudantClient());
	//	System.out.println(getAllDocs(cloudantClient()));
	//	System.out.println(getAllDocs(cloudantClient()));
	//	System.out.println(removeDocument(cloudantClient(), "0bdad52829dafde4191692b81ea45a65"));
		deleteAllDocs();
	}
}
