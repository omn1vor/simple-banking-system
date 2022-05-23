package banking;

import java.util.Scanner;

public class CLI {
    private final Bank bank;
    private final Scanner scanner = new Scanner(System.in);

    public CLI(Bank bank) {
        this.bank = bank;
        Menu menu = getMainMenu();
        menu.choose();
    }

    private Menu getMainMenu() {
        Menu menu = new Menu();
        menu.add(new Action("Create an account", this::createAccount));
        menu.add(new Action("Log into account", this::logIn));
        menu.setExitAction("Exit");

        return menu;
    }

    private Menu getAccountMenu(Card card) {
        Menu menu = new Menu();
        menu.add(new Action("Balance", () -> showBalance(card)));
        menu.add(new Action("Add income", () -> addIncome(card)));
        menu.add(new Action("Do transfer", () -> transfer(card)));
        menu.add(new Action("Close account", () -> closeAccount(card), true));
        menu.add(new Action("Log out", null));
        menu.setExitAction("Exit", () -> System.exit(0));

        return menu;
    }

    private void createAccount() {
        Card card = bank.createAccount();
        System.out.println("Your card has been created");
        System.out.printf("Your card number:%n%s%n", card.getNumber());
        System.out.printf("Your card PIN:%n%s%n", card.getPin());
    }

    private void logIn() {
        System.out.println();
        System.out.println("Enter your card number:");
        String number = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();

        Card card;
        try {
            card = bank.logIn(number, pin);
        } catch (Exception e) {
            System.out.println();
            System.out.println(e.getMessage());
            return;
        }
        System.out.printf("%nYou have successfully logged in!%n");
        Menu menu = getAccountMenu(card);
        menu.choose();
    }

    private void showBalance(Card card) {
        System.out.printf("%nBalance: %d%n", bank.getBalance(card));
    }

    private void addIncome(Card card) {
        System.out.println();
        System.out.println("Enter income:");
        long sum = Long.parseLong(scanner.nextLine());
        if (sum <= 0) {
            System.out.println("Wrong sum!");
            return;
        }
        bank.deposit(card, sum);
        System.out.println("Income was added!");
    }

    private void transfer(Card card) {
        System.out.println();
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String number = scanner.nextLine();

        try {
            bank.checkNumber(number);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        long sum = Long.parseLong(scanner.nextLine());

        try {
            bank.transfer(card, number, sum);
            System.out.println("Success!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void closeAccount(Card card) {
        bank.closeAccount(card);
        System.out.println();
        System.out.println("The account has been closed!");
    }
}
