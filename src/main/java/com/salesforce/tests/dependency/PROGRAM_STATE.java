package com.salesforce.tests.dependency;

public enum PROGRAM_STATE {
	DEPEND, INSTALL, REMOVE, LIST, END, UNINSTALLED,
	/**
	 * Status for manually Installed Program.
	 */
	INSTALLED,  
	/**
	 * Status for Parent Program installed through a dependency.
	 */
	INSTALLED_THRU_DPNDNCY;
}
