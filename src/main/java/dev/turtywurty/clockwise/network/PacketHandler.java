package dev.turtywurty.clockwise.network;

import dev.turtywurty.clockwise.Clockwise;
import dev.turtywurty.clockwise.network.serverbound.SSetTimezonePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Clockwise.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        int index = 0;

        INSTANCE.messageBuilder(SSetTimezonePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SSetTimezonePacket::encode)
                .decoder(SSetTimezonePacket::new)
                .consumerMainThread(SSetTimezonePacket::handle)
                .add();

        Clockwise.LOGGER.info("Registered {} packets for mod '{}'!", index, Clockwise.MOD_ID);
    }
}
