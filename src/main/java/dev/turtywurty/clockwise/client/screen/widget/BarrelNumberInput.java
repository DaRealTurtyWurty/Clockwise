package dev.turtywurty.clockwise.client.screen.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BarrelNumberInput extends EditBox {
    private int min = -100, max = 100, current = 0;

    public BarrelNumberInput(int pX, int pY, int pWidth, int pHeight) {
        super(Minecraft.getInstance().font, pX, pY, pWidth, pHeight, Component.empty());
        setResponder(this::handleChange);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        if(this.current != max) {
            pGuiGraphics.drawString(Minecraft.getInstance().font, "+", getX() + getWidth() / 2, getY() - 5, 0x404040, false);
        }

        if(this.current != min) {
            pGuiGraphics.drawString(Minecraft.getInstance().font, "-", getX() + getWidth() / 2, getY() + getHeight() + 5, 0x404040, false);
        }
    }

    public void setMin(int min) {
        if (min > this.max)
            throw new IllegalArgumentException("Min cannot be greater than max!");

        this.min = min;
    }

    public void setMax(int max) {
        if (max < this.min)
            throw new IllegalArgumentException("Max cannot be less than min!");

        this.max = max;
    }

    public void setMinMax(int min, int max) {
        if (min > max)
            throw new IllegalArgumentException("Min cannot be greater than max!");

        this.min = min;
        this.max = max;
    }

    public int getCurrent() {
        return this.current;
    }

    private void handleChange(String text) {
        if(text.equals(String.valueOf(this.current)))
            return;

        if(text.isEmpty()) {
            this.current = 0;
            setValue("0");
        } else {
            try {
                this.current = Mth.clamp(Integer.parseInt(text), this.min, this.max);
                setValue(String.valueOf(this.current));
            } catch (NumberFormatException ignored) {
                setValue(text.replace("[^%d]", ""));
            }
        }
    }
}
