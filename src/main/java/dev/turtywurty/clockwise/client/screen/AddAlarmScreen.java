package dev.turtywurty.clockwise.client.screen;

import dev.turtywurty.clockwise.Clockwise;
import dev.turtywurty.clockwise.client.screen.widget.DateTimeSelector;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class AddAlarmScreen extends Screen {
    private static final Component TITLE =
            Component.translatable("gui." + Clockwise.MOD_ID + ".digital_clock.alarm");

    private final DigitalClockAlarmScreen parent;
    private final int imageWidth, imageHeight;

    private int leftPos, topPos;

    private EditBox nameField;
    private DateTimeSelector dateTimeSelector;
//    private Dropdown repeatDropdown;
//    private DropdownEditBox soundSelection;

    public AddAlarmScreen(DigitalClockAlarmScreen parent) {
        super(TITLE);

        this.parent = parent;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

//        this.nameField = addRenderableWidget(new EditBox(
//                this.font,
//                this.leftPos + 40,
//                this.topPos + 25,
//                this.imageWidth - 45,
//                20,
//                Component.empty()));
//
//        this.dateTimeSelector = addRenderableWidget(new DateTimeSelector(
//                this.leftPos + 40,
//                this.topPos + 65
//        ));
//
//        this.repeatDropdown = addRenderableWidget(new Dropdown(
//                this.leftPos + 40,
//                this.topPos + 90,
//                this.imageWidth - 45,
//                20
//        ));
//
//        this.soundSelection = addRenderableWidget(new DropdownEditBox(
//                this.leftPos + 40,
//                this.topPos + 115,
//                this.imageWidth - 45,
//                20
//        ));
    }
}
