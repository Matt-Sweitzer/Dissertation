# <center>Breadboard</center>

<center>Author: Matthew D. Sweitzer<br>
Email: <a href="mailto:email@mattsweitzer.com">email@mattsweitzer.com</a><br>
Homepage: <a href="https://www.matthewsweitzer.com">mattsweitzer.com</a></center>

<br>
Study code for my dissertation titled "Selection Homophily in Dynamic Political Communication Networks: An Interpersonal Perspective".

<hr>

This code requires installation of Breadboard, a software package for human subjects networking experiments. More information is available at http://breadboard.yale.edu/ and at https://github.com/human-nature-lab/breadboard/wiki

To use, download the directory "SelectionHomophily", maintaining the subdirectory structure. Then, compress the root folder into a .zip file and use the import option on BreadBoard to load the study.

Importantly, you MUST have root access to the /breadboard-v#.#.#/ directory on your machine for importing to work.

In the most recent update to the code, tie decisions are assigned using recursive functions. This allows multiple participants to make tie decisions at the same time, with the step closing when the "queue" is empty. This is accomplished in steps `newNetworkTieDecision#.groovy` as well as `TieDecisionFns.groovy`. The old method of assigning tie choices one-by-one is perhaps safer, albeit MUCH slower. The old method is contained in steps `NetworkTieDecision#.groovy`. To revert to the old method, simply change the step pointed to in the corresponding `networkDiscussion#.done` closures in steps `NetworkDiscussion#.groovy`.

<hr>

### Known issues:

On startup or engine refresh, the console prints errors as both `basepay` and `waittime` are missing values absent input in the "Experiment Instances" dialog. These errors can be ignored as they are parameterized on instance startup.

Client HTML will need to be manually copied into the "customize" window -- Breadboard (as of ver. 2.3.1) disregards this file when importing.

Parameters will rarely read into breadboard in the same order they appear in `parameters.csv`. One can either attempt to import multiple times until they appear correctly, or learn to live with the disorder.

`selectedfortie` is a stored attribute of both vertices and edges, and it is used to change the color, size, and stroke of each when a tie decision is presented to participants in order to emphasize what relationship is being considered. In the latest update to the code, `selectedfortie` is a private property of vertices and edges, meaning that only the player making the tie decision can see the style changes. However, at this time, breadboard does not support making attributes of one vertex private to another vertex. In other words, participants would be able to see style changes when they are the target of a tie decision, but they will not be able to see who is making that decision. As such, style changes pertaining to the target vertices are kept the same as non-targets; however, this could be changed in the style.css file in the lines where `selectedfortie` are set to either 2 or 4. It is unclear whether support for alter-private values will come in some future version of the software.
