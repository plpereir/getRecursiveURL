package br.com.getAllURLRecursive.Service;

public class CoreProcess {
	
	public boolean IsValidURL(String strLink)
	{
		if (strLink.contains("https:"))
		{
		  	return true;	
		}else
		{
			strLink = strLink.replace("http:", "https:");
			return false;	
		}
	}
	
	public String loadResponse(String line)
	{
		return "<li class='list-group-item'>"+line+"</li>";
	}
	
}
