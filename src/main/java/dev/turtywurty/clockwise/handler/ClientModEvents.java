package dev.turtywurty.clockwise.handler;

import dev.turtywurty.clockwise.Clockwise;
import dev.turtywurty.clockwise.client.renderer.DigitalClockRenderer;
import dev.turtywurty.clockwise.init.BlockEntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Clockwise.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.DIGITAL_CLOCK.get(), DigitalClockRenderer::new);
    }
}
