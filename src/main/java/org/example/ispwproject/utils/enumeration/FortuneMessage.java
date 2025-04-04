package org.example.ispwproject.utils.enumeration;

import java.util.concurrent.ThreadLocalRandom;

public enum FortuneMessage {
    OPPORTUNITY("Every day is a new opportunity."),
    PERSEVERANCE("Perseverance is the key to success."),
    BELIEVE_IN_YOURSELF("Never stop believing in yourself."),
    HAPPINESS("Happiness depends on you."),
    STEP_BY_STEP("Every step you take brings you closer to your goal.");

    private final String message;

    FortuneMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static FortuneMessage getRandomFortune() {
        FortuneMessage[] values = FortuneMessage.values();
        return values[ThreadLocalRandom.current().nextInt(values.length)];
    }
}
