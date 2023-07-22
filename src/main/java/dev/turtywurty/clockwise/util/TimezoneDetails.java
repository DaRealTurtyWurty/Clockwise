package dev.turtywurty.clockwise.util;

public class TimezoneDetails {
    private final String name;
    private final int offset;
    private final boolean daylightSavings;

    public TimezoneDetails(String zoneName, int offset, boolean daylightSaving) {
        this.name = zoneName;
        this.offset = offset;
        this.daylightSavings = daylightSaving;
    }

    public String getName() {
        return this.name;
    }

    public int getOffset() {
        return this.offset;
    }

    public boolean isDaylightSavings() {
        return this.daylightSavings;
    }
}
