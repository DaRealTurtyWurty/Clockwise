package dev.turtywurty.clockwise.handler;

import dev.turtywurty.clockwise.Clockwise;
import dev.turtywurty.clockwise.blockentity.DigitalClockBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Clockwise.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {
    @SubscribeEvent
    public static void itemUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if(level.isClientSide() || event.getHand() != InteractionHand.MAIN_HAND || !event.getEntity().isCrouching()) return;

        ItemStack stack = event.getItemStack().copy();
        if(!(stack.getItem() instanceof DyeItem dye)) return;

        BlockEntity be = level.getBlockEntity(event.getHitVec().getBlockPos());
        if(be instanceof DigitalClockBlockEntity blockEntity) {
            DyeColor color = dye.getDyeColor();
            if(blockEntity.getColor() == color) return;

            blockEntity.setColor(color);
            if(!event.getEntity().isCreative()) {
                stack.shrink(1);
            }

            event.getEntity().setItemInHand(event.getHand(), stack);
        }
    }
}
