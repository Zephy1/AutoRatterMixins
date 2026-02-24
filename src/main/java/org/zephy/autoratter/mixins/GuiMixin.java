package org.zephy.autoratter.mixins;

//#if MC>=12106
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zephy.autoratter.DrawSingleItemStackEvent;
import org.zephy.autoratter.ItemRenderData;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(
        method = "renderItemHotbar",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;renderSlot(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/client/DeltaTracker;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;I)V",
            ordinal = 0
        )
    )
    public void renderHotbarItem(
        CallbackInfo ci,
        @Local(argsOnly = true) GuiGraphics drawContext,
        @Local(ordinal = 4, name = "m") int index,
        @Local(ordinal = 5, name = "n") int x,
        @Local(ordinal = 6, name = "o") int y,
        @Local() Player player
    ) {
        ItemStack itemStack = player.getInventory().getNonEquipmentItems().get(index);
        if (itemStack.isEmpty()) return;

        DrawSingleItemStackEvent.drawSingleItemStack(
            new ItemRenderData(
                drawContext,
                itemStack,
                x,
                y,
                0
            )
        );
    }
}
//#endif
