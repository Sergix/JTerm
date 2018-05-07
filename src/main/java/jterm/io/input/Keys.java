package jterm.io.input;

public enum Keys {

    UP(InputHandler::processUp),
    DOWN(InputHandler::processDown),
    LEFT(InputHandler::processLeft),
    RIGHT(InputHandler::processRight),
    TAB(9, InputHandler::tabEvent),
    BACKSPACE(InputHandler::backspaceEvent),
    NWLN(InputHandler::newLineEvent),
    CHAR(InputHandler::charEvent),
    CTRL_C(3, InputHandler::ctrlCEvent),
    CTRL_Z(26, InputHandler::ctrlZEvent),
    NONE(-1, null);
    String value;
    final Runnable r;

    Keys(Runnable r) {
        this.r = r;
    }

    public void executeAction() {
        if (r != null) r.run();
    }

    Keys(int value, Runnable r) {
        this.r = r;
        this.value = String.valueOf(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static void initGUI() {
        UP.value = "-38";
        DOWN.value = "-40";
        RIGHT.value = "-39";
        LEFT.value = "-37";
        BACKSPACE.value = "8";
        NWLN.value = "10";
    }

    public static Keys getKeyByValue(String input) {
        Keys[] keys = Keys.values();
        for (Keys key : keys) {
            if (input.equals(key.value))
                return key;
        }
        return CHAR;
    }
}
