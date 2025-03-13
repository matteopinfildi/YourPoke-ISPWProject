package org.example.ispwproject.utils.enumeration;

public enum UI {
    NONE(-1),
    GUI(1),
    CLI(2);

    private final int value;

    UI(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UI fromInt(int i) {
        for (UI u : UI.values()) {
            if (u.getValue() == i) {
                return u;
            }
        }
        return NONE;
    }
}
