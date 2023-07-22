package dev.turtywurty.clockwise.init;

import dev.turtywurty.clockwise.Clockwise;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Clockwise.MOD_ID);

    public static final RegistryObject<BlockItem> DIGITAL_CLOCK_ITEM = ITEMS.register("digital_clock",
            () -> new BlockItem(BlockInit.DIGITAL_CLOCK.get(), new Item.Properties()));
}
