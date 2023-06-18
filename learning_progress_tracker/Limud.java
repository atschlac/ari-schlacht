import java.util.NoSuchElementException;

public class Limud
{
    String type;
    String category;
    String book;
    double quantity;
    double amountComplete;

    Limud ()
    {

    }

    //SETTERS
    protected void setType(String type) {
        this.type = type;
    }

    protected void setCategory(String category) {
        this.category = category;
    }

    protected void setBook(String book) {
        this.book = book;
    }
    protected void setAmountComplete(int amountComplete) {
        this.amountComplete = amountComplete;
    }

    protected void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //GETTERS
    protected String getType() {
        return type;
    }

    protected String getCategory() {
        return category;
    }

    protected String getBook() {
        return book;
    }

    protected double getQuantity() {
        return quantity;
    }

    protected double getAmountComplete() {
        return (amountComplete / quantity) * 100;
    }


    @Override
    public String toString() {
        switch(type)
        {
            case "תנך":
                return "Sefer " + category + ", " + book + ", Amount Completed = " + this.getAmountComplete() + '%';
            case "משנה":
                return "Seder " + category + ", " + book + ", Amount Completed = " + this.getAmountComplete() + '%';
            case "גמרא":
                return "Seder " + category + ", " + book + ", Amount Completed = " + this.getAmountComplete() + '%';
            case "משחבה":
                return "Sefer " + book + ", Amount Completed = " + this.getAmountComplete() + '%';
            case "other":
                return "Sefer " + book + ", Amount Completed = " + this.getAmountComplete() + '%';
            default:
                throw new NoSuchElementException("There is no toString for this limud");


        }


    }
}
