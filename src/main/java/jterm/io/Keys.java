package jterm.io;

public enum Keys {
    UP, DOWN, LEFT, RIGHT, TAB(9), BACKSPACE, NWLN, CHAR, CTRL_C(3), CTRL_Z(26), NONE(-1);

    Keys() {

    }

    Keys(int value) {
        this.value = value;
    }

    int value;

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

    public static Keys getKeyByValue(int c) {
        Keys[] keys = Keys.values();
        for (Keys key : keys) {
            if (c == (key.value))
                return key;
        }
        return CHAR;
    }
}
