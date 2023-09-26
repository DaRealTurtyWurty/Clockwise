package dev.turtywurty.clockwise.client.screen.widget;

import net.minecraft.client.gui.components.Renderable;

import java.util.List;

public class DateTimeSelector {
    private final BarrelNumberInput yearInput;
//    private final Dropdown monthInput;
//    private final Dropdown dayInput;
//    private final BarrelNumberInput hourInput, minuteInput, secondInput;

    private final int xPos, yPos;

    public DateTimeSelector(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;

        this.yearInput = new BarrelNumberInput(xPos, yPos, 20, 20);
    }

    public List<Renderable> getWidgets() {
//        return List.of(this.yearInput, this.monthInput, this.dayInput, this.hourInput, this.minuteInput, this.secondInput);
        return List.of();
    }
}
