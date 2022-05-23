package banking;

public class Action {
    private final String name;
    private final Runnable action;
    private final boolean runOnce;

    public Action(String name, Runnable action, boolean runOnce) {
        this.name = name;
        this.action = action;
        this.runOnce = runOnce;
    }

    public Action(String name, Runnable action) {
        this(name, action, false);
    }

    @Override
    public String toString() {
        return name;
    }

    public void run() {
        if (action != null) {
            action.run();
        }
    }

    public boolean isExit() {
        return action == null;
    }

    public boolean isRunOnce() {
        return runOnce;
    }
}
