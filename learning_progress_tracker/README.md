# Learning Progress Tracker

**Note: This application is currently in the very early stages and is very basic. Significantly more functionality and a graphical user interface (GUI) will be added in the future.**

The Learning Progress Tracker is a command-line application designed to help users track their learning progress in different judaic subjects, but can easily be expanded to support all subjects. It provides various options to manage and update information about the subjects the user is studying.

## Getting Started

Please note that the Learning Progress Organizer is currently in the early stages of development and only provides basic functionality in a text-based format. To use the software:

1. Clone the repository or download the source code.
2. Compile and run the application using a Java development environment.
3. Follow the instructions provided within the application to set up your learning goals, schedule, and progress tracking.

Please keep an eye out for future updates as we continue to enhance the software's functionality and introduce a graphical user interface (GUI) for a more user-friendly experience.

## Program Features

### Home Page

The Home Page displays the user's name and provides the following options:

- `View Limud Progress`: Displays the user's name and all the limud (subject) information. Press Enter to go back to the home page.
- `Update Limud Progress`: Displays the user's current limud information and prompts for the name of the ספר (book) to update the progress. It also prompts for the page/daf number the user has reached. It updates the progress and displays the updated completion percentage.
- `Delete a Limud`: Displays the user's current limud information and prompts for the name of the ספר (book) to delete. It deletes the selected limud from the list and confirms the deletion.
- `Add a Limud`: Allows the user to enter another limud to be tracked and displayed by the application
- `End Program`: Exits the program.

### Limud Class

The Limud class represents a specific subject of study and contains the following attributes:

- `Type`: The type of limud, such as תנך, משנה, גמרא, מחשבה, or other.
- `Category`: The category of the limud, such as סדר (for משנה and גמרא) or ספר (for תנך and מחשבה).
- `Book`: The name of the specific book or מסכתא being studied.
- `Quantity`: The total quantity or number of pages/dapim to cover in the limud.
- `Amount Complete`: The current progress or number of pages/dapim completed.

The Limud class provides getters and setters for each attribute and overrides the `toString()` method to display the limud information based on its type.


## Feedback and Contributions

We appreciate your interest in the Learning Progress Organizer. As this project is in its early stages, we welcome feedback, suggestions, and contributions. If you have any ideas to improve the software or would like to contribute to its development, please submit a pull request or reach out to us via the provided contact information.

**Please note that this program is a command-line application and relies on user input through the console.**
