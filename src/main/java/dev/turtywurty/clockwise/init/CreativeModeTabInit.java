package dev.turtywurty.clockwise.init;

import dev.turtywurty.clockwise.Clockwise;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabInit {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Clockwise.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_TABS.register(Clockwise.MOD_ID,
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Clockwise.MOD_ID))
                    .icon(ItemInit.DIGITAL_CLOCK_ITEM.get()::getDefaultInstance)
                    .displayItems(
                            (pParameters, pOutput) -> ItemInit.ITEMS.getEntries().forEach(item -> pOutput.accept(item.get())))
                    .build());
}
