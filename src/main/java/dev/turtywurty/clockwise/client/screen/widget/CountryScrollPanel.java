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

import java.util.ArrayList;
import java.util.List;

public class CountryScrollPanel extends CustomScrollPanel {
    private final List<String> allCountries = new ArrayList<>();
    private final List<String> filteredCountries = new ArrayList<>();

    private final DigitalClockTimezoneScreen screen;

    private String selected = "";

    public CountryScrollPanel(DigitalClockTimezoneScreen screen, int width, int height, int top, int left) {
        super(Minecraft.getInstance(), width, height, top, left);

        this.screen = screen;

        TimezoneLoader.getTimezones().keySet().stream()
                .distinct()
                .sorted()
                .forEach(this.allCountries::add);

        this.filteredCountries.addAll(this.allCountries);
    }

    @Override
    protected int getContentHeight() {
        return this.filteredCountries.size() * 16;
    }

    @Override
    protected void drawPanel(GuiGraphics guiGraphics, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        int scrollAmount = getScrollAmount();
        for (int index = 0; index < this.filteredCountries.size(); index++) {
            String country = this.filteredCountries.get(index);
            // check if the entry is in the visible area
            if (relativeY + index * 16 - scrollAmount + 16 > 0 && relativeY + index * 16 - scrollAmount < this.top + this.height) {
                drawEntry(guiGraphics, country, relativeY, index, mouseX, mouseY);
            }
        }
    }

    private void drawEntry(GuiGraphics graphics, String country, int relativeY, int index, int mouseX, int mouseY) {
        int x = this.left + 4;
        int y = relativeY + 4 + index * 16;

        if (y + 12 > this.top + this.height)
            return;

        boolean isOver = isMouseOverEntry(mouseX, mouseY, y - 2, y + 10, this.left + 2, this.left + this.width - 2 - this.barWidth) || country.equalsIgnoreCase(this.selected);
        graphics.fill(this.left + 2, y - 2, this.left + this.width - 2 - this.barWidth, y + 10, isOver ? 0x80AAAAAA : 0x80555555);
        graphics.drawString(this.client.font, truncate(country, 30), x, y, 0xFFFFFF, true);
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        this.filteredCountries.forEach(country -> pNarrationElementOutput.add(NarratedElementType.USAGE, Component.literal(country)));
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
        if (this.filteredCountries.size() * 16 < this.height) {
            this.scrollDistance = 0;
            return false;
        }

        return super.mouseScrolled(mouseX, mouseY, scroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        String country = getEntryAtPosition((int) mouseX, (int) mouseY);
        if (country != null) {
            this.selected = country;
            this.screen.getNextButton().active = true;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void updateEntries(String filter) {
        if (filter.isEmpty()) {
            this.filteredCountries.clear();
            this.filteredCountries.addAll(this.allCountries);
            return;
        }

        this.scrollDistance = 0;
        this.filteredCountries.clear();
        for (String country : this.allCountries) {
            if (country.toLowerCase().contains(filter.toLowerCase())) {
                this.filteredCountries.add(country);
            }
        }
    }

    public String getSelected() {
        return this.selected;
    }

    private String getEntryAtPosition(int mouseX, int mouseY) {
        int relativeY = this.top + border - (int) this.scrollDistance;
        int scrollAmount = getScrollAmount();
        for (int index = 0; index < this.filteredCountries.size(); index++) {
            String country = this.filteredCountries.get(index);
            // check if the entry is in the visible area
            if (relativeY + index * 16 - scrollAmount + 16 > 0 && relativeY + index * 16 - scrollAmount < this.top + this.height + 16) {
                int y = relativeY + 4 + index * 16;

                if (y + 12 > this.top + this.height)
                    continue;

                if (isMouseOverEntry(mouseX, mouseY, y - 2, y + 10, this.left + 2, this.left + this.width - 2 - this.barWidth)) {
                    return country;
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