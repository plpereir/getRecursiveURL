package br.com.getAllURLRecursive.Service;

/**
 * 
 * @author Pedro Luiz da Silva Pereira
 * this class is responsible to valid url and support tag html return ajax.
 *
 */

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
