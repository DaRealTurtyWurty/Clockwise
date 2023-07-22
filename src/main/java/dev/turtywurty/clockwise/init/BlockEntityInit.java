package dev.turtywurty.clockwise.init;

import dev.turtywurty.clockwise.Clockwise;
import dev.turtywurty.clockwise.blockentity.DigitalClockBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Clockwise.MOD_ID);

    public static final RegistryObject<BlockEntityType<DigitalClockBlockEntity>> DIGITAL_CLOCK =
            BLOCK_ENTITIES.register("digital_clock",
            () -> BlockEntityType.Builder.of(DigitalClockBlockEntity::new, BlockInit.DIGITAL_CLOCK.get())
                    .build(null));
}
