Done by: Yasser Alejandro Palacios
ID: 13030067

All work is captured on Package Student:
- It includes 4 interfaces and 10 classes.
- 1 Interface and 2 classes are the PriorityQueue's provided and are being leveraged
in this solution.
- 3  new interfaces and 7 classes were designed and developed to implement the solution.
This was mainly done to ensure that the solution follows the SOLID software design
principles.

I have also included a doc folder in the repository with the JavaDoc documentation.

I attempted creating JUnit tests for the classes created. However, I did ran into issues
with the level of visibility of constructors and other methods of the classes that I
needed acccess to in order to run my tests that are in other packages (example: GameState class 
has a private constructor and the Node class has a package constructor). I didn't want to change 
this visibility for the purpose of testing.
However, it limited my ability to use those classes or to be able to create a Mock versions
of the EscapeState and ExplorationState classes. In particular, a sticking point was the
package visibility of the Node class constructor. I didn't want to recreate similar
classes to the Node and GameState to avoid  any confussion on my submission. I am capabable
of rebuilding similar classes, but wanted to avoid confussion.