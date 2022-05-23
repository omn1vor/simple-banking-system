package banking;

import java.util.Arrays;

public class Bank {
    private final BankDAO dao;

    public Bank(DB db) {
        dao = new BankDAO(db);
    }

    public Card createAccount() {
        Card card = new Card(generateNumber(), generatePIN());
        dao.addCard(card);
        return card;
    }

    public Card logIn(String number, String pin) {
        checkNumberFormat(number);
        if (!pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("Wrong pin format!");
        }
        return dao.get(number, pin)
                .orElseThrow(() -> new IllegalArgumentException("Wrong card number or PIN!"));
    }

    public long getBalance(Card card) {
        return dao.balance(card);
    }

    public void deposit(Card card, long sum) {
        dao.deposit(card, sum);
    }

    public void transfer(Card card, String number, long sum) {
        if (dao.balance(card) < sum) {
            throw new IllegalArgumentException("Not enough money!");
        }
        dao.transfer(card, number, sum);
    }

    public void checkNumber(String number) {
        checkNumberFormat(number);
        int[] arr = Arrays.stream(number.split(""))
                .mapToInt(Integer::parseInt)
                .toArray();
        if (Util.getCheckSum(arr) != arr[arr.length - 1]) {
            throw new IllegalArgumentException("Probably you made a mistake in the card number. Please try again!");
        }
        if (!dao.exists(number)) {
            throw new IllegalArgumentException("Such a card does not exist.");
        }
    }

    public void closeAccount(Card card) {
        dao.delete(card);
    }

    private String generateNumber() {
        int[] digits = new int[16];
        digits[0] = 4;
        long newNumber = dao.count() + 1;
        Util.numberIntoArray(newNumber, digits, 6, 14);
        digits[15] = Util.getCheckSum(digits);

        return Util.arrayToString(digits);
    }

    private String generatePIN() {
        int pinNumber = (int) (Math.random() * 10000);
        int[] digits = new int[4];
        Util.numberIntoArray(pinNumber, digits, 0, 3);
        return Util.arrayToString(digits);
    }

    private void checkNumberFormat(String number) {
        if (!number.matches("\\d{16}")) {
            throw new IllegalArgumentException("Wrong card number format!");
        }
    }
}
