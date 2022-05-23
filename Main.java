package banking;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2 || !"-fileName".equals(args[0])) {
            System.out.println("expecting a parameter: -fileName DB_filename");
            return;
        }

        Bank bank = new Bank(new DB(args[1]));
        new CLI(bank);
    }
}