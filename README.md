git-to-clearcase
================

A simple Java command line application that reproduces GIT commits on a Clearcase UCM view.

# Usage

git-to-clearcase is very simple to run. It suposes that you have
 - git command line executable installed somewhere
 - cleartool command line executable installed elsewhere

then just need two more pieces of information:
 - path of your git repository
 - path of the source code directory within a ClearCase UCM view

Then, just call the script

`java -jar git-to-clearcase-1.2 --git c:\cmd\git\git.exe --repo c:\git\proj-repository --ct c:\cmd\clearcase\bin\cleartool.exe --view c:\ccviews\project\source´

Sure, there are further command line arguments. Call `java -jar git-to-clearcase-1.2 --help´ to learn more.

# If everything fails

Experience has shown that cleartool calls may fail arbitrarely. If repository and view get out of synch, just fix the affected directory by calling.

`java -jar git-to-clearcase-1.2 --git c:\cmd\git\git.exe --repo c:\git\proj-repository --ct c:\cmd\clearcase\bin\cleartool.exe --view c:\ccviews\project\source --compare subdirectory´

# Notes
 - this requits does not support CCRC views

