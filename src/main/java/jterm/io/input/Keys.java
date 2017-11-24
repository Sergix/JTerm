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
    int value;
    final Runnable r;

    Keys(Runnable r) {
        this.r = r;
    }

    public void executeAction() {
        if (r != null) r.run();
    }

    Keys(int value, Runnable r) {
        this.r = r;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static void initWindows() {
        UP.value = 57416;
        DOWN.value = 57424;
        RIGHT.value = 57421;
        LEFT.value = 57419;
        BACKSPACE.value = 8;
        NWLN.value = 13;
    }

    public static void initUnix() {
        UP.value = -183;
        DOWN.value = -184;
        RIGHT.value = -185;
        LEFT.value = -186;
        BACKSPACE.value = 127;
        NWLN.value = 10;
    }

    public static void initGUI() {
        UP.value = -38;
        DOWN.value = -40;
        RIGHT.value = -39;
        LEFT.value = -37;
        BACKSPACE.value = 8;
        NWLN.value = 10;
    }

    public static Keys getKeyByValue(int c) {
        Keys[] keys = Keys.values();
        for (Keys key : keys) {
            if (c == (key.value))
                return key;
        }
        return CHAR;
    }
}
