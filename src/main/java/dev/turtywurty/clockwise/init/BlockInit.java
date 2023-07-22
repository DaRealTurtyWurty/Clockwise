package dev.turtywurty.clockwise.init;

import dev.turtywurty.clockwise.Clockwise;
import dev.turtywurty.clockwise.block.DigitalClockBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Clockwise.MOD_ID);

    public static final RegistryObject<DigitalClockBlock> DIGITAL_CLOCK = BLOCKS.register("digital_clock",
            () -> new DigitalClockBlock(
                    BlockBehaviour.Properties.of()
                            .strength(7.0f, 15.0f)
                            .noOcclusion()
                            .sound(SoundType.LANTERN)
            ));
}
