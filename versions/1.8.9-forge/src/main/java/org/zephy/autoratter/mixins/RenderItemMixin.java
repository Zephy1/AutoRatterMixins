package org.zephy.autoratter.mixins;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zephy.autoratter.DrawBatchedItemStackListEvent;
import org.zephy.autoratter.ItemRenderData;

@Mixin(value = RenderItem.class)
public abstract class RenderItemMixin {
    @Inject(
        method = "renderItemIntoGUI",
        at = @At("RETURN")
    )
    public void drawItem(
        ItemStack itemStack,
        int x,
        int y,
        CallbackInfo callback
    ) {
        DrawBatchedItemStackListEvent.addItemRenderData(
            new ItemRenderData(
                itemStack,
                x,
                y
            )
        );
    }
}
