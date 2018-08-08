package com.salesforce.tests.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

import static com.salesforce.tests.dependency.PROGRAM_STATE.*;

/**
 * The entry point for the Test program
 * 
 * This class provides methods to :
 * Maintain a list of program dependencies and installed programs,
 * List the Installed programs,
 * Add a program (and its parent programs),
 * Remove a program(and its child programs),
 * Clean Up the programs (remove a program if its not installed explicitly and all its child programs are unistalled).
 * 
 * @author Sanjay Ingole
 * 
 */
public class Main {

	/**
	 * Constructor to initialize the class.
	 */
	public Main() {
		installed = new LinkedHashMap<String, String>();
		dependenciesMap = new HashMap<String, List<String>>();
	}

	/**
	 * HashMap to maintain the list of installed programs.
	 * 
	 * Programs can be directly installed or through a dependency.
	 * 
	 * example : 
	 * TCPIP, INSTALLED_THRU_DEPENDENCY
	 * or 
	 * BROWSER, INSTALLED
	 */
	LinkedHashMap<String, String> installed;

	/**
	 * HashMap to maintain the list of programs and thier dependencies.
	 */
	HashMap<String, List<String>> dependenciesMap;

	/**
	 * Method to add the dependencies for a program.
	 * 
	 * Method also validates the length of target program name
	 * and check if the provided dependencies are not already listed as dependencies to the target program.  
	 * 
	 * @param programs
	 */
	public Boolean addDependencies(String programs) {

		StringTokenizer t = new StringTokenizer(programs, " ");
		int index = 0;

		List<String> dependencies = new ArrayList<String>();
		String programName = null;

		while (t.hasMoreTokens()) {
			String name = t.nextToken();

			/**
			 * Validate if the program name is greater than 10 char. 
			 */
			if(name.trim() == null || name.trim().length() > 10){
				System.out.println(" Program name : "+ name.trim() +" is greater than 10 char.");
				return false;
			}
			
			/**
			 * First token is the actual Program to be added.
			 */
			System.out.print(" " + name.trim());
			
			if (index == 0) {
				programName = name;
			} else {
				/** 
				 * Valid if the program is not already added as a parent of entered dependency. In which case return an error.
				 */
				if(validateDependency(name.trim(), programName)){
					System.out.println("\n"+ name.trim() +" depends on " + programName +", ignoring command");
					return false;
				} else {
					dependencies.add(name.trim());
				}
			}
			index++;
		}

		if (programName != null) {
			this.dependenciesMap.put(programName.trim(), dependencies);
		}
		
		System.out.println("");
		return true;
	}
	
	/**
	 * Method to validate if there exists a dependency between provided program and target parent.
	 * 
	 * @return true if dependency exists.
	 */
	public Boolean validateDependency(String programName, String parent) {
		
		Stack<String> hierarchy = new Stack<String >();
		HashSet<String> visited = new HashSet<String>();
	
		hierarchy = getHierarchy(programName, hierarchy, visited);
		
		boolean childExplored = false;
		
		while(!hierarchy.isEmpty()){
			String name = hierarchy.peek();
			if(name.equals(programName)){
				childExplored = true;				
			} else if(name.equals(parent) && childExplored){
				return true;
			}
			hierarchy.pop();
		}	
		
		return false;
	}
	
	/**
	 * Method to return if the program is installed.
	 * 
	 * @return
	 */
	private Boolean isInstalled(String program){
		if(program == null) 
			return false;
		return this.installed.get(program).startsWith(INSTALLED.name());
	}
	
	/**
	 * Method to install a Program.
	 * 
	 * This method recursively install the target program and all its dependent program (if not already installed)
	 * 
	 * @param programName
	 * @param installAs Signify whether this is a explicit install or through dependency.
	 */
	public void installProgram(String programName, String installAs) {

		if (installed.get(programName) != null && isInstalled(programName)) {
			System.out.println(programName + " is already installed");
			return;
		}

		// get the Dependencies for this program from the stack.
		List<String> dependencies = this.dependenciesMap.get(programName);

		if(dependencies != null) {
			for (String dependency : dependencies) {

				if (installed.get(dependency) != null && isInstalled(dependency)) {
					continue;
				}
	
				installProgram(dependency, INSTALLED_THRU_DPNDNCY.name());
			}
		}
		System.out.println("Installing " + programName);
		this.installed.put(programName, installAs);
	}

	/**
	 * Method to list the all the installed program.
	 * 
	 * @return
	 */
	public void listInstalledPrograms() {

		for (Map.Entry<String, String> entry : installed.entrySet()) {
			if(isInstalled(entry.getKey())){
				System.out.println(entry.getKey());
			}		    
		}
	}
	
	/**
	 * Method to find the dependencies of a program by iterating through the dependencies list recursively 
	 * and populating a stack (with parent at the bottom of the stack and child at the top)
	 * 
	 * We use a temporary HashSet "visited" for the items already visited in order to avoid redundant iteration.
	 * 
	 * @return
	 */
	public Stack<String> getHierarchy(String programName, Stack<String> stack, HashSet<String> visited) {

		/**
		 * Do not iterate further if the node is already visited.
		 */
		if(visited.contains(programName)) return stack;
		
		List<String> programs = this.dependenciesMap.get(programName);

		if(programs != null) {
			for (int i = 0; i < programs.size(); i++) {
				getHierarchy(programs.get(i), stack, visited);
			}
		}
		
		stack.push(programName);
		visited.add(programName);
		
		return stack;
	}

	/**
	 * Method to Remove an Installed Program.
	 * 
	 * Check For Dependent/Child Programs.
	 * Before removing the program check if any dependent programs is currently installed, 
	 * if yes, do not uninstall the program. no dependent program currently installed.
	 * 
	 * Parent Clean Up:
	 * When uninstalling a program, also delete the parent node if there is no more dependency on them..
	 * 
	 * @param program
	 */
	public Boolean removeProgram(String programName, String removeAs) {
		
		if (installed.get(programName) == null || !isInstalled(programName)) {
			System.out.println(programName + " is not installed");
			return true;
		}
		
		/**
		 * Iterate thorugh all the installed program and check if any dependent one is still installed.
		 */
		Iterator<String> iter =	installed.keySet().iterator();		
		while(iter.hasNext()){
			String name = (String) iter.next();
			if(!name.trim().equals(programName) && validateDependency(name.trim(), programName) && isInstalled(name)){
				return false; 
			}
		}
		
		/**
		 * Only remove this program if it has no child or if its a parent installed thorugh dependency.
		 */
		if(this.installed.get(programName).equals(removeAs)) {
			System.out.println("Removing " + programName);
			installed.put(programName, UNINSTALLED.name());
		}
		
		/**
		 * Check if the parents nodes have any other dependency than the taget node. 
		 * If not then delete the parent node as well.
		 */
		List<String> parents = dependenciesMap.get(programName);
		if(parents != null) {
			for(String parent : parents) {
				removeProgram(parent, INSTALLED_THRU_DPNDNCY.name());
			}
		}
		return true;
	}

	public static void main(String[] args) {

		// read input from stdin
		Scanner scan = new Scanner(System.in);

		ArrayList<String> input = new ArrayList<String>();

		Main main = new Main();

		while (true) {
			String line = scan.nextLine();

			// no action for empty input
			if (line == null || line.length() == 0) {
				continue;
			} else {

			}

			// the END command to stop the program
			if ("END".startsWith(line)) {
				System.out.println("END");
				break;
			}

			// Please provide your implementation here

			if (line != null && !line.isEmpty()) {

				if (line.trim().startsWith(PROGRAM_STATE.DEPEND.name())) {
					String programs = line.substring(6, line.length()).trim();
					System.out.print("DEPEND");
					main.addDependencies(programs);

				} else if (line.trim().startsWith(PROGRAM_STATE.INSTALL.name())) {
					String program = line.substring(7, line.length()).trim();
					System.out.println("INSTALL " + program);
					main.installProgram(program, INSTALLED.name());

				} else if (line.trim().startsWith(PROGRAM_STATE.REMOVE.name())) {
					String program = line.substring(6, line.length()).trim();
					System.out.println("REMOVE " + program);
					if(!main.removeProgram(program, INSTALLED.name())){
						System.out.println(program + " is still needed");
					};

				} else if (line.trim().startsWith(PROGRAM_STATE.LIST.name())) {
					System.out.println("LIST");
					main.listInstalledPrograms();
				} else {
					System.out.println("INVALID COMMAND");
				}
			}
		}

	}
}
