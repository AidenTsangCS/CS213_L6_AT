
**NG 7/27/2026**
* To provide you with thorough feedback, your instructor reads your code line by line and runs it, and quite often does it multiple times. Jumping from file to file is too time-consuming. **Unless noted otherwise, place your class definitions above the main(), all in one file.** While the industry standard involves multi-source file programs, we will use a single file format for these labs unless instructed otherwise.
* never hesitate to ask if you find something confusing or have questions
*  **feedback.md is for instructor use only.  Please DO NOT change the feedback.md**; make a copy if needed; do no add anything
* if any, items with (-X) - no deductions this time, serve as a warning; please ensure these errors are corrected, as repeating them in future assignments will result in X points being deducted
* in feedback, #N means line number, e.g., 
* it is time for each class to have its own file  -5 
* modularization: each menu option should have its own method with the possible exception of the quit and default cases; if a case in a switch has more than two statements, including break, make it a method -5
* #66-69 and like: excessive use of System.out.println(); method calls are computational expense (takes time and space); unnecessary function calls increase execution time and require additional resources -5
```text
System.out.println("\n\nCode is like humor.\n\t"+ 
                    "When you have to explain "+ 
                    "it, it’s bad.\n\t\t\t"+ 
                    "–Cory House");
```
* #72 and like: multiple return statements in a non-recursive method; a code block (like a method) should have one way in and one way out ( normally referred to as Core Principle of Single Entry, Single Exit (SESE)) whenever possible to promote readability, clarity, maintainability,  and control; use a variable to store the results and return the variable; multiple return statements might result in an unreachable return statement; -5
* 158 and like readability: do not put opening and closing {} on the same line; each executable statement should be on its own line; define each variable on a separate line; do not put closing } and the next statement on the same line;  closing } should be on its own line -2
```text
int health=DEFAULT_HEALTH;
int strength = DEFAULT_STRENGTH;
public Creature() {
  setCreature("unknown", "unknown", 1, 1);
}
```
* uses on or of the following:  non-allowed libraries, break (Ok in switch), return or exit in loops and ifs to get out of loops and ifs, go to or continue, empty return (e.g., return; return null;), infinite loops (while(true) or similar); void function with return statement; there is no situation in this realm where same effect cannot be achieved by a logical statement; in other words, there is no situation in this realm that cannot be described by using a combination of relational, comparison, and logical operators; you can always  write a loop condition to exit naturally when needed; see the syllabus and your HW # Bad Programming Practices; moving forward use of any of the above will result  in a grade of a zero of the assignment and additional attempts will not be granted-  stopped grading

***

