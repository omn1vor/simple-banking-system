package banking;

public class Card {
    String number;
    String pin;
    long balance;

    public Card(String number, String pin, long balance) {
        this.number = number;
        this.pin = pin;
        this.balance = balance;
    }

    public Card(String number, String pin) {
        this(number, pin, 0);
    }

    String getPin() {
        return pin;
    }

    public String getNumber() {
        return number;
    }

}
