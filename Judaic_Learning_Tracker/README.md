# Learning Progress Tracker

The Learning Progress Tracker is a command-line application designed to help users track their learning progress in different subjects. It provides various options to manage and update information about the subjects the user is studying.

## How to Use

1. Run the `DerechClass` class, which contains the main method, to start the program.
2. The program will display a menu with different options.
3. Enter the corresponding letter to select an option and press Enter.
4. Follow the instructions provided by the program to interact and navigate through the options.

## Program Features

### Home Page

The Home Page displays the user's name and provides the following options:

- View Limud Progress: Displays the user's name and all the limud (subject) information. Press Enter to go back to the home page.
- Update Limud Progress: Displays the user's current limud information and prompts for the name of the ספר (book) to update the progress. It also prompts for the page/daf number the user has reached. It updates the progress and displays the updated completion percentage.
- Delete a Limud: Displays the user's current limud information and prompts for the name of the ספר (book) to delete. It deletes the selected limud from the list and confirms the deletion.
- End Program: Exits the program.

### Limud Class

The Limud class represents a specific subject of study and contains the following attributes:

- Type: The type of limud, such as תנך, משנה, גמרא, מחשבה, or other.
- Category: The category of the limud, such as סדר (for משנה and גמרא) or ספר (for תנך and מחשבה).
- Book: The name of the specific book or מסכתא being studied.
- Quantity: The total quantity or number of pages/dapim to cover in the limud.
- Amount Complete: The current progress or number of pages/dapim completed.

The Limud class provides getters and setters for each attribute and overrides the `toString()` method to display the limud information based on its type.

**Please note that this program is a command-line application and relies on user input through the console.**
