package br.com.getAllURLRecursive.Service.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.getAllURLRecursive.Service.CoreProcess;
 
public class SearchProcessTest {
    private static CoreProcess coreProcess;
    
    @BeforeClass
    public static void initCoreProcess() {
    	coreProcess = new CoreProcess();
    }
 
    @Before
    public void beforeEachTest() {
        System.out.println("This is executed before each Test");
    }
 
    @After
    public void afterEachTest() {
        System.out.println("This is exceuted after each Test");
    }
 
    @Test
    public void coreProcessUrl() {
    	assertTrue("is valid url", coreProcess.IsValidURL("https://google.com.br"));
    }
    
    @Test
    public void coreProcessReturn() {
    	assertEquals("<li class='list-group-item'>oi</li>", coreProcess.loadResponse("oi"));
    }

}
