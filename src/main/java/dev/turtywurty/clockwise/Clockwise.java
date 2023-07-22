package dev.turtywurty.clockwise;

import dev.turtywurty.clockwise.init.BlockEntityInit;
import dev.turtywurty.clockwise.init.BlockInit;
import dev.turtywurty.clockwise.init.CreativeModeTabInit;
import dev.turtywurty.clockwise.init.ItemInit;
import dev.turtywurty.clockwise.network.PacketHandler;
import dev.turtywurty.clockwise.util.TimezoneLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Clockwise.MOD_ID)
public class Clockwise {
    public static final String MOD_ID = "clockwise";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Clockwise() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        CreativeModeTabInit.CREATIVE_TABS.register(bus);

        TimezoneLoader.init();
        bus.addListener(this::commonSetup);

        LOGGER.info("Clockwise has been loaded!");
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::init);
    }
}
