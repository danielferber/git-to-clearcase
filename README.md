## About

[Please refer to the Wiki for detailed explanation](http://github.com/danielferber/git-to-clearcase/wiki/Home).

**git-to-clearcase** is a simple Java command line application that reproduces GIT commits on a Clearcase UCM view, keeping the second synchronized with the former.

It was designed to allow current trends as agile development, incremental design and refactoring on IT departments that still rely on old fashioned Clearcase for Source Control Management.

While the teams works with modern tools and IDEs on a Git repository, the **git-to-clearcase** runs on background, keeping Clearcase in sync, without bothering any developer anymore.

## Usage

**git-to-clearcase** is easy to run. Suppose that you have:
 - git command line executable installed somewhere
 - cleartool command line executable installed elsewhere

You need two more pieces of information:
 - path of your git repository
 - path of the source code directory within a ClearCase UCM view
It would be appreciated if both repository and view were dedicated exclusively to git-to-clearcase.

Then, just call git-to-clearcase:
`java -jar git-to-clearcase-1.2 --git c:\cmd\git\git.exe --repo c:\git\project-repository --ct c:\cmd\clearcase\bin\cleartool.exe --view c:\ccviews\project\source`

For the first time, I also would recommend adding the parameters `--configure --update --clean --reset`, just to ensure that both repository and view are in a consistent state.

There are further command line arguments. Call java -jar git-to-clearcase-1.2 --help to learn more or [read the additional documentation at the wiki](http://github.com/danielferber/git-to-clearcase/wiki/Home).

### If everything fails

Experience has shown that cleartool calls may fail arbitrarily. If repository and view get out of sync, just fix the affected directory by calling:

`java -jar git-to-clearcase-1.2 --git c:\cmd\git\git.exe --repo c:\git\proj-repository --ct c:\cmd\clearcase\bin\cleartool.exe --view c:\ccviews\project\source --update --reset --clean --configure --compare subdirectory`

