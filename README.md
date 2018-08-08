# project-dependencies-utilities
A sample utility to install a program along with its dependencies. It employs algorithm to perform topological sort to install, delete, list the programs.

 * This class provides methods to :
 * Maintain a list of program dependencies and installed programs,
 * List the Installed programs,
 * Add a program (and its parent programs),
 * Remove a program(and its child programs),
 * Clean Up the programs (remove a program if its not installed explicitly and all its child programs are unistalled).
