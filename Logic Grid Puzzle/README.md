# Logic Grid Puzzle Challenge

Welcome to the **Logic Grid Puzzle Challenge**! This challenge is designed to test your logical reasoning and deduction skills. By solving a logic grid puzzle, you will uncover a hidden phrase or keyword to complete the challenge.

## Folder Structure
The project is organized as follows:

```
/Logic Grid Puzzle
├── src/main/java/sample  # Contains the source code of the application
├── logicPuzzle.exe       # Executable version of the application
├── logicPuzzle.jar       # Java-based executable version of the application
└── readme.md             # This README file
```

## Challenge Explanation
### Objective
Participants must solve a logic grid puzzle by matching names to objects, places, or other categories. The solution to the puzzle will reveal a hidden phrase or keyword.

### Challenge Setup
#### Logic Puzzle
The puzzle provides a series of clues that participants must logically deduce to complete the grid. Once solved, the completed grid reveals the phrase or keyword.

#### Example Scenario
- **Scenario**: There are three IT employees: Alice, Bob, and Charlie. Each of them uses a different operating system (Windows, Linux, MacOS) and sits in a different room (101, 102, 103).

**Clues:**
1. Alice does not use Windows, and she does not sit in Room 102.
2. Bob uses Linux but does not sit in Room 101.
3. The person who uses MacOS does not sit in Room 102.
4. The person who sits in Room 101 uses Windows.
5. Charlie does not use MacOS.

**Solution:**
- Alice uses MacOS and sits in Room 103.
- Bob uses Linux and sits in Room 102.
- Charlie uses Windows and sits in Room 101.

### Setup
#### Grid
Participants will use a table with rows and columns to track possible and impossible options based on the clues provided.

#### Categories
- **Employees**: Alice, Bob, Charlie
- **Operating Systems**: Windows, Linux, MacOS
- **Room Numbers**: 101, 102, 103

#### Logic Grid Example
Here’s how the grid might look:

|              | Room 101 | Room 102 | Room 103 | Windows | Linux | MacOS |
|--------------|-----------|----------|----------|---------|-------|-------|
| **Alice**    | X         | X        | ✓        | X       |       | ✓     |
| **Bob**      | X         | ✓        |          |         | ✓     |       |
| **Charlie**  | ✓         |          |          | ✓       | X     | X     |

**Note:** This exact scenario will not appear in the application, but the logic and approach will be similar.

## Features
- **Interactive Puzzle Solver**: Provides a logic grid and clues for participants to solve.
- **Challenge Completion**: Solving the puzzle correctly reveals a hidden phrase or keyword.

## Quick Guide
1. Launch the application by running `logicPuzzle.exe` or `logicPuzzle.jar`.
2. Review the provided clues in the application.
3. Use logical deduction to complete the grid and determine the correct matches.
4. Submit your solution.
5. If correct, the hidden phrase or keyword will be revealed!

## Development
The source code for the application is located in the `src/main/java/sample` folder. Developers can review and modify the code as needed to enhance functionality or adapt it to specific use cases.

## Requirements
To run the application:
- **Java Runtime Environment (JRE)** for `logicPuzzle.jar`
- **Windows OS** for `logicPuzzle.exe`

---

Good luck solving the puzzle and revealing the hidden phrase or keyword!

