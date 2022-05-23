package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final List<Action> items = new ArrayList<>();
    private Action exitAction;
    private static final Scanner scanner = new Scanner(System.in);

    public void add(Action action) {
        items.add(action);
    }

    public void setExitAction(String name) {
        setExitAction(name, null);
    }

    public void setExitAction(String name, Runnable action) {
        this.exitAction = new Action(name, action);
    }

    public void show() {
        System.out.println();
        int count = 1;
        for (Action action : items) {
            System.out.printf("%d. %s%n", count++, action);
        }
        if (exitAction != null) {
            System.out.printf("0. %s%n", exitAction);
        }
    }

    public void choose() {
        while (true) {
            show();
            Action action;
            try {
                int index = scanner.nextInt();
                action = getItem(index);
            } catch (Exception ignored) {
                scanner.nextLine();
                continue;
            }
            if (action.isExit()) {
                break;
            } else {
                action.run();
                if (action.isRunOnce()) {
                    break;
                }
            }
        }
    }

    private Action getItem(int index) {
        if (index == 0) {
            return exitAction;
        } else if (index < 0 || index > items.size()) {
            throw new IllegalArgumentException("Wrong input");
        }
        return items.get(index - 1);
    }
}
