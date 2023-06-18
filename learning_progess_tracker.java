import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class DerechClass
{
    public static void main (String[] args) throws IOException
    {
        DerechClass testClass = new DerechClass();
        testClass.run();
    }


    //Instance variables
    String userName;
    int userAge;
    Set<Limud> limudSet = new HashSet<>();

    String CLS = "\u001b[2J";
    String HOME = "\u001b[H";

    public void run() throws IOException
    {
        //ADD A SOME CHAR THAT CLEAR'S THE SCREEN
        System.out.print(CLS); // clear the screen
        System.out.println(HOME);

        this.setProfileInfo();
        this.setAllLimudInfo();
        this.homePage();
    }

    //Functional Methods
    private void homePage() throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean keepRunning = true;
        while(keepRunning)
        {
            //ADD A SOME CHAR THAT CLEAR'S THE SCREEN
            System.out.print(CLS); // clear the screen
            System.out.println(HOME);

            System.out.println(userName);

            System.out.println("Please enter a different option");
            String[] answerOptions = {"A. View limud progress ", "B. Update limud progress", "C. Delete a Limud", "D. Add a limud", "E. End program"};
                for (String option : answerOptions)
                {
                    System.out.println(option);
                }
            String answer = reader.readLine();
                switch (answer)
                {
                    case "A":
                        //ADD A SOME CHAR THAT CLEAR'S THE SCREEN
                        System.out.print(CLS); // clear the screen
                        System.out.println(HOME);

                        System.out.println(this.getAllLimudInfo());
                        System.out.println("Press return to go back");
                        reader.readLine();
                        break;
                    case "B":
                        //ADD A SOME CHAR THAT CLEAR'S THE SCREEN
                        System.out.print(CLS); // clear the screen
                        System.out.println(HOME);

                        this.setLimudProgress();
                        System.out.println("Press return to go back");
                        reader.readLine();
                        break;
                    case "C":
                        //ADD A SOME CHAR THAT CLEAR'S THE SCREEN
                        System.out.print(CLS); // clear the screen
                        System.out.println(HOME);

                        this.deleteLimud();
                        System.out.println("Press return to go back");
                        reader.readLine();
                        break;
                    case "D":
                        //ADD A SOME CHAR THAT CLEAR'S THE SCREEN
                        System.out.print(CLS); // clear the screen
                        System.out.println(HOME);

                        this.setAllLimudInfo();
                        break;
                    case "E":
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("You failed to enter a valid option, so we're shutting you down, stinks to be you");
                        keepRunning = false;
                        break;
                }
        }
    }


    private String getAllLimudInfo()
    {
        String allLimudString = userName + " " + '\n';
        for (Limud l: limudSet) {
            allLimudString += l.toString() + '\n';
        }
        return allLimudString;
    }

    private void setProfileInfo() throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your first and last name: ");
        this.userName = reader.readLine();
        System.out.print("Enter your age: ");
        this.userAge = Integer.parseInt(reader.readLine());

    }

    private void setAllLimudInfo() throws IOException
    {
        //ADD A SOME CHAR THAT CLEAR'S THE SCREEN
        System.out.print(CLS); // clear the screen
        System.out.println(HOME);
        boolean keepGoing = true;
        while (keepGoing)
        {
            Limud currentLimud = this.createNewLimud();
            limudSet.add(currentLimud);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Do you want to add another לימוד?");
            System.out.println("A: Yes B: No (type the letter)");
            String answer = reader.readLine();
            switch (answer)
            {
                case "A":
                    keepGoing = true;
                    break;
                case "B":
                    keepGoing = false;
                    break;
                default:
                    System.out.println("Because you answered poorly, you lost your chance to enter another limud");
                    keepGoing = false;
                    break;
            }
        }
    }

    private Limud createNewLimud() throws IOException
    {
        Limud currentLimud = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        boolean invalidAnswer = true;
        while(invalidAnswer)
        {
            System.out.println("What type of לימוד do you want to learn?");
            String[] answerOptions = {"A. גמרא", "B. משנה", "C. מחשבה", "D. תנך", "E. other", "(type a letter)"};
            for (String option : answerOptions) {
                System.out.println(option);
            }

            String answer = reader.readLine();
            switch (answer)
            {
                case "A":
                    currentLimud = setGemaraLimud();
                    invalidAnswer = false;
                    break;
                case "B":
                    currentLimud = setMishnahLimud();
                    invalidAnswer = false;
                    break;
                case "C":
                    currentLimud = setMachshavaLimud();
                    invalidAnswer = false;
                    break;
                case "D":
                    currentLimud = setTanachLimud();
                    invalidAnswer = false;
                    break;
                case "E":
                    currentLimud = setOtherLimud();
                    invalidAnswer = false;
                    break;
                default:
                    System.out.println("You did not provide a valid answer, please try again");
            }
        }
        return currentLimud;
    }

        private Limud setOtherLimud() throws IOException
        {
            Limud otherLimud = new Limud();
            otherLimud.setType("other");
            BufferedReader reader = new BufferedReader((new InputStreamReader(System.in)));
            System.out.println("Which ספר do you want to learn?");
            otherLimud.setBook(reader.readLine());

            System.out.println("How many דפים do you plan to cover in that ספר?");
            int dapimAnswer = Integer.parseInt(reader.readLine());
            otherLimud.setQuantity(dapimAnswer);

            return otherLimud;
        }

        private Limud setTanachLimud() throws IOException
        {
            Limud tanachLimud = new Limud();
            tanachLimud.setType("תנך");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Which ספר do you want to learn?");
            String[] answerOptions = {"A. תורה", "B. נביאים", "C. כתובים", "(type the ספר)"};
            for (String option : answerOptions) {
                System.out.println(option);
            }

            String seferAnswer = reader.readLine();
            tanachLimud.setCategory(seferAnswer);

            System.out.println("Which ספר do you want to learn in " + seferAnswer + " ?");
            String masechtaAnswer = reader.readLine();
            tanachLimud.setBook(masechtaAnswer);

            System.out.println("How many פרקים do you plan to cover in that ספר?");
            int dapimAnswer = Integer.parseInt(reader.readLine());
            tanachLimud.setQuantity(dapimAnswer);

            return tanachLimud;
        }

        private Limud setMachshavaLimud() throws IOException
        {
            Limud machshavaLimud = new Limud();
            machshavaLimud.setType("מחשבה");

            System.out.println("Which ספר do you want to learn?");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String answer = reader.readLine();
            machshavaLimud.setBook(answer);

            System.out.println("How many דפים do you plan to cover in that ספר?");
            int dapimAnswer = Integer.parseInt(reader.readLine());
            machshavaLimud.setQuantity(dapimAnswer);
            return machshavaLimud;
        }

        private Limud setMishnahLimud() throws IOException
        {
            Limud mishnahLimud = new Limud();
            mishnahLimud.setType("משנה");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Which סדר do you want to learn?");
            String[] answerOptions = {"A. זרעים", "B. מועד", "C. נשים", "D. נזיקין", "E. קדשים", " F. טהרות", "(type the סדר)"};
            for (String option : answerOptions) {
                System.out.println(option);
            }
            String sederAnswer = reader.readLine();
            mishnahLimud.setCategory(sederAnswer);

            System.out.println("Which מסכתא do you want to learn?");
            String masechtaAnswer = reader.readLine();
            mishnahLimud.setBook(masechtaAnswer);

            System.out.println("How many פרקים do you plan to cover in that מסכתא?");
            int perakimAnswer = Integer.parseInt(reader.readLine());
            mishnahLimud.setQuantity(perakimAnswer);
            return mishnahLimud;
        }

        private Limud setGemaraLimud() throws IOException
        {
            Limud gemaraLimud = new Limud();
            gemaraLimud.setType("גמרא");

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Which סדר do you want to learn?");
            String[] answerOptions = {"A. זרעים", "B. מועד", "C. נשים", "D. נזיקין", "E. קדשים", " F. טהרות", "(type the סדר)"};
            for (String option : answerOptions) {
                System.out.println(option);
            }
            String sederAnswer = reader.readLine();
            gemaraLimud.setCategory(sederAnswer);

            System.out.println("Which מסכתא do you want to learn?");
            String masechtaAnswer = reader.readLine();
            gemaraLimud.setBook(masechtaAnswer);

            System.out.println("How many דפים are in that מסכתא?");
            int dapimAnswer = Integer.parseInt(reader.readLine());
            gemaraLimud.setQuantity(dapimAnswer);
            return gemaraLimud;
        }

    private void setLimudProgress () throws IOException
    {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("These are your לימודים:");
            System.out.println(this.getAllLimudInfo());
            System.out.println("Which לימוד do you want to update? (type the name of the ספר)");
            String seferAnswer = reader.readLine();
            System.out.println("Where page/ daf are you now up to now?");
            int updatedAmountComplete = Integer.parseInt(reader.readLine());
            for (Limud l: limudSet)
            {
                if (l.getBook().equals(seferAnswer)) {
                    l.setAmountComplete(updatedAmountComplete);
                    System.out.println("You have finished " + l.getAmountComplete() + '%');
                    break;
                }
            }
        }

    private void deleteLimud() throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Which לימוד do you want to delete? (type the name of the ספר)");
        String seferAnswer = reader.readLine();
        for (Limud l: limudSet)
            {
                if (l.getBook().equals(seferAnswer))
                {
                    this.limudSet.remove(l);
                    System.out.println("You have deleted " + seferAnswer);
                    break;
                }
            }

    }

    private void setSchedule()
    {

    }
    private void getSchedule()
    {

    }

}
