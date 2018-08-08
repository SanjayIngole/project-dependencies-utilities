package com.salesforce.tests.dependency;

import java.io.IOException;

import org.junit.Test;

/**
 * Place holder for your unit tests
 */
public class YourUnitTest extends BaseTest {

	/**
	 * Test Invalid Scenario.
	 * 
	 * @throws IOException
	 */
	@Test
    public void testInvalidProgramName() throws IOException {
        String[] input = {
                "DEPEND LOOOOOOOONNNNNGGGGGG AAAAAAAAAAAAAAAAAAAAAAA  \n",
                "END\n"
        };

        String expectedOutput = "DEPEND Program name : LOOOOOOOONNNNNGGGGGG is greater than 10 char.\n" +
                "END\n";

        runTest(expectedOutput, input);
    }
	/**
	 * Test Invalid Scenario.
	 * 
	 * @throws IOException
	 */
	@Test
    public void testInvalidCommand() throws IOException {
        String[] input = {
                "XYCBX A B\n",
                "END\n"
        };

        String expectedOutput = "INVALID COMMAND\n" +
                "END\n";

        runTest(expectedOutput, input);
    }
	
	/**
	 * Sample Input to add Dependencies, some invalid values, uninstall, install and list programs.
	 * 
	 * @throws IOException
	 */
	 @Test
	    public void testSampleInput() throws IOException {
	        String[] input = {
	                "DEPEND TELNET TCPIP NETCARD\n",
	                "DEPEND TCPIP NETCARD\n",
	                "DEPEND NETCARD TCPIP\n",
	                "INSTALL NETCARD\n",
	                "INSTALL TELNET\n",
	                "INSTALL ABCD\n",
	                "REMOVE NETCARD\n",
	                "INSTALL BROWSER\n",
	                "LIST\n",
	                "REMOVE TELNET\n",
	                "REMOVE NETCARD\n",
	                "REMOVE INVALID\n",
	                "REMOVE NETCARD\n",
	                "INSTALL NETCARD\n",
	                "REMOVE TCPIP\n",
	                "LIST\n",
	                "END\n"
	        };

	        String expectedOutput = "DEPEND TELNET TCPIP NETCARD\n" +
	                "DEPEND TCPIP NETCARD\n" +
	                "DEPEND NETCARD TCPIP\n" +
	                "TCPIP depends on NETCARD, ignoring command\n" +
	                "INSTALL NETCARD\n" +
	                "Installing NETCARD\n" +
	                "INSTALL TELNET\n" +
	                "Installing TCPIP\n" +
	                "Installing TELNET\n" +
	                "INSTALL ABCD\n" +
	                "Installing ABCD\n" +
	                "REMOVE NETCARD\n" +
	                "NETCARD is still needed\n" +
	                "INSTALL BROWSER\n" +
	                "Installing BROWSER\n" +
	                "LIST\n" +
	                "NETCARD\n" +
	                "TCPIP\n" +
	                "TELNET\n" +
	                "ABCD\n" +
	                "BROWSER\n" +
	                "REMOVE TELNET\n" +
	                "Removing TELNET\n" +
	                "Removing TCPIP\n" +
	                "REMOVE NETCARD\n" +
	                "Removing NETCARD\n" +
	                "REMOVE INVALID\n" +
	                "INVALID is not installed\n" +
	                "REMOVE NETCARD\n" +
	                "NETCARD is not installed\n" +
	                "INSTALL NETCARD\n" +
	                "Installing NETCARD\n" +
	                "REMOVE TCPIP\n" +
	                "TCPIP is not installed\n" +
	                "LIST\n" +
	                "NETCARD\n" +
	                "ABCD\n" +
	                "BROWSER\n" +
	                "END\n";

	        runTest(expectedOutput, input);
	    }
	 
    @Test
    //INSTALL: output "Installing"
    public void testInstall() throws IOException {
        String[] input = {
                "DEPEND A B\n",
                "INSTALL B\n",
                "END\n"
        };

        String expectedOutput = "DEPEND A B\n" +
                "INSTALL B\n" +
                "Installing B\n" +
                "END\n";

        runTest(expectedOutput, input);
    }

}
