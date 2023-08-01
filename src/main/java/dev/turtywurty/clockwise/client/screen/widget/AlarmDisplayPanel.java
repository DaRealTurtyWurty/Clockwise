package dev.turtywurty.clockwise.client.screen.widget;

import com.mojang.blaze3d.vertex.Tesselator;
import dev.turtywurty.clockwise.client.screen.DigitalClockAlarmScreen;
import dev.turtywurty.clockwise.util.data.Alarm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AlarmDisplayPanel extends CustomScrollPanel {
    private final DigitalClockAlarmScreen screen;
    private final List<Alarm> alarms = new ArrayList<>();

    private Alarm selected;

    public AlarmDisplayPanel(DigitalClockAlarmScreen screen, List<Alarm> alarms, int width, int height, int top, int left) {
        super(Minecraft.getInstance(), width, height, top, left);

        this.screen = screen;
        this.alarms.addAll(alarms.subList(0, Math.min(alarms.size(), 5)));
    }

    @Override
    protected int getContentHeight() {
        return this.alarms.size() * 20;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Alarm alarm = getEntryAtPosition((int) mouseX, (int) mouseY);
        if (alarm != null) {
            setSelected(alarm);
            return true;
        }

        setSelected(null);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        for (int index = 0; index < this.alarms.size(); index++) {
            Alarm alarm = this.alarms.get(index);
            // check if the entry is in the visible area
            if (this.top + this.border + index * 20 + 16 > 0 && this.top + this.border + index * 20 < this.top + this.border + this.height) {
                drawEntry(guiGraphics, alarm, index, mouseX, mouseY);
            }
        }
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        this.alarms.forEach(alarm -> pNarrationElementOutput.add(NarratedElementType.USAGE, Component.literal(alarm.getName())));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        return false;
    }

    private void drawEntry(GuiGraphics graphics, Alarm alarm, int index, int mouseX, int mouseY) {
        int x = this.left + 4;
        int y = this.top + this.border + 4 + index * 20;

        if (y + 12 > this.top + this.border + this.height)
            return;

        boolean isOver = isMouseOverEntry(mouseX, mouseY, y - 2, y + 10, this.left + 2, this.left + this.width - 2) || alarm.equals(this.selected);
        graphics.fill(this.left + 2, y - 4, this.left + this.width - 2, y + 12, isOver ? 0x80AAAAAA : 0x80555555);
        graphics.drawString(this.client.font, truncate(alarm.getName(), 30), x, y, 0xFFFFFF, true);
    }

    public void setSelected(@Nullable Alarm selected) {
        this.selected = selected;

        if (selected != null) {
            this.screen.setSelectedAlarm(selected);
        }

        this.screen.updateButtonValidity();
    }

    public @Nullable Alarm getSelected() {
        return this.selected;
    }

    private Alarm getEntryAtPosition(int mouseX, int mouseY) {
        for (int index = 0; index < this.alarms.size(); index++) {
            Alarm alarm = this.alarms.get(index);

            int y = this.top + this.border + 4 + index * 20;
            // check if the entry is in the visible area
            if (y + 20 > 0 && y < this.top + this.border + this.height + 20) {
                if (y + 12 > this.top + this.border + this.height)
                    continue;

                if (isMouseOverEntry(mouseX, mouseY, y - 4, y + 12, this.left + 2, this.left + this.width - 2)) {
                    return alarm;
                }
            }
        }

        return null;
    }

    private static boolean isMouseOverEntry(int mouseX, int mouseY, int entryTop, int entryBottom, int entryLeft, int entryRight) {
        return mouseX >= entryLeft && mouseX <= entryRight && mouseY >= entryTop && mouseY <= entryBottom;
    }

    private static String truncate(String str, int length) {
        if (str.length() > length) {
            return str.substring(0, length - 3).trim() + "...";
        }

        return str;
    }
}