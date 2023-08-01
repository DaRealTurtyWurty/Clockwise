package dev.turtywurty.clockwise.client.screen.widget;

import com.mojang.blaze3d.vertex.Tesselator;
import dev.turtywurty.clockwise.client.screen.DigitalClockTimezoneScreen;
import dev.turtywurty.clockwise.util.TimezoneLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimezoneScrollPanel extends CustomScrollPanel {
    private final List<Timezone> allTimezones = new ArrayList<>();
    private final List<Timezone> filteredTimezones = new ArrayList<>();

    private final DigitalClockTimezoneScreen screen;

    public TimezoneScrollPanel(DigitalClockTimezoneScreen screen, String country, int width, int height, int top, int left) {
        super(Minecraft.getInstance(), width, height, top, left);

        this.screen = screen;

        TimezoneLoader.getTimezones().get(country).forEach(details -> {
            String[] split = details.getName().replace("_", " ").split("/");
            String name = split[split.length - 1];

            this.allTimezones.add(new Timezone(name, ZoneId.of(details.getName())));
        });

        this.allTimezones.sort(Comparator.comparing(Timezone::name));
        this.filteredTimezones.addAll(this.allTimezones);
    }

    @Override
    protected int getContentHeight() {
        return this.filteredTimezones.size() * 16;
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        int scrollAmount = getScrollAmount();
        for (int index = 0; index < this.filteredTimezones.size(); index++) {
            Timezone timezone = this.filteredTimezones.get(index);
            // check if the entry is in the visible area
            if (relativeY + index * 16 - scrollAmount + 16 > 0 && relativeY + index * 16 - scrollAmount < this.top + this.height + 16) {
                drawEntry(guiGraphics, timezone, relativeY, index, mouseX, mouseY);
            }
        }
    }

    private void drawEntry(GuiGraphics graphics, Timezone timezone, int relativeY, int index, int mouseX, int mouseY) {
        int x = this.left + 4;
        int y = relativeY + 4 + index * 16;

        if(y + 12 > this.top + this.height)
            return;

        boolean isOver = isMouseOverEntry(mouseX, mouseY, y - 2, y + 10, this.left + 2, this.left + this.width - 2 - this.barWidth);
        graphics.fill(this.left + 2, y - 2, this.left + this.width - 2 - this.barWidth, y + 10, isOver ? 0x80AAAAAA : 0x80555555);
        graphics.drawString(this.client.font, truncate(timezone.name(), 30), x, y, 0xFFFFFF, true);
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        this.filteredTimezones.forEach(timezone -> pNarrationElementOutput.add(NarratedElementType.USAGE, Component.literal(timezone.name())));
    }

    @Override
    public int getBarHeight() {
        return Math.max(10, Math.min((int) ((float) this.height * ((float) this.height / (float) getContentHeight())), this.height));
    }

    @Override
    protected int getScrollAmount() {
        return Math.max(0, Math.min(16, getContentHeight() - (this.height - 4)));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if(this.filteredTimezones.size() * 16 < this.height) {
            this.scrollDistance = 0;
            return false;
        }

        return super.mouseScrolled(mouseX, mouseY, scroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Timezone timezone = getEntryAtPosition((int) mouseX, (int) mouseY);
        if(timezone != null) {
            this.screen.setTimezone(timezone);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void updateEntries(String filter) {
        if(filter.isEmpty()) {
            this.filteredTimezones.clear();
            this.filteredTimezones.addAll(Timezone.sortTimezonesByOffset(new ArrayList<>(this.allTimezones)));
            return;
        }

        this.scrollDistance = 0;
        this.filteredTimezones.clear();
        for (Timezone timezone : this.allTimezones) {
            if (timezone.name().toLowerCase().contains(filter.toLowerCase())) {
                this.filteredTimezones.add(timezone);
            }
        }
    }

    private Timezone getEntryAtPosition(int mouseX, int mouseY) {
        int relativeY = this.top + border - (int) this.scrollDistance;
        int scrollAmount = getScrollAmount();
        for (int index = 0; index < this.filteredTimezones.size(); index++) {
            Timezone timezone = this.filteredTimezones.get(index);
            // check if the entry is in the visible area
            if (relativeY + index * 16 - scrollAmount + 16 > 0 && relativeY + index * 16 - scrollAmount < this.top + this.height + 16) {
                int y = relativeY + 4 + index * 16;

                if(y + 12 > this.top + this.height)
                    continue;

                if(isMouseOverEntry(mouseX, mouseY, y - 2, y + 10, this.left + 2, this.left + this.width - 2 - this.barWidth)) {
                    return timezone;
                }
            }
        }
        return null;
    }

    private static boolean isMouseOverEntry(int mouseX, int mouseY, int entryTop, int entryBottom, int entryLeft, int entryRight) {
        return mouseX >= entryLeft && mouseX <= entryRight && mouseY >= entryTop && mouseY <= entryBottom;
    }

    private static String truncate(String str, int length) {
        if(str.length() > length) {
            return str.substring(0, length - 3).trim() + "...";
        }

        return str;
    }

    public record Timezone(String name, ZoneId zoneId) {
        public static List<Timezone> sortTimezonesByOffset(List<Timezone> timezoneList) {
            Comparator<Timezone> offsetComparator = (timezone1, timezone2) -> {
                int offset1 = timezone1.zoneId().getRules().getOffset(Instant.now()).getTotalSeconds();
                int offset2 = timezone2.zoneId().getRules().getOffset(Instant.now()).getTotalSeconds();
                return Integer.compare(offset1, offset2);
            };

            List<Timezone> sortedTimezones = new ArrayList<>(timezoneList);
            return sortedTimezones.stream().sorted(offsetComparator).toList();
        }
    }
}
