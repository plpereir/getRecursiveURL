package br.com.getAllURLRecursive.Service;

public class CoreProcess {
	
	public boolean IsValidURL(String strLink)
	{
		if (strLink.contains("https:"))
		{
		  	return true;	
		}else
		{
			return false;	
		}
	}
	
	public String loadResponse(String line)
	{
		return "<li class='list-group-item'>"+line+"</li>";
	}
	
}
