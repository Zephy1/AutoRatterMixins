package org.zephy.autoratter.mixins;

//#if MC>=12106
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zephy.autoratter.DrawSingleItemStackEvent;
import org.zephy.autoratter.ItemRenderData;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Inject(
        method = "renderSlot",
        at = @At("HEAD")
    )
    private void drawSlotItemUnderlay(
        GuiGraphics drawContext,
        Slot slot,
        //#if MC>=12111
        int i,
        int j,
        //#endif
        CallbackInfo ci
    ) {
        ItemStack stack = slot.getItem();
        if (stack.isEmpty()) return;

        DrawSingleItemStackEvent.drawSingleItemStack(
            new ItemRenderData(
                drawContext,
                stack,
                slot.x,
                slot.y,
                0
            )
        );
    }

    @Inject(
        method = "renderCarriedItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/GuiGraphics;nextStratum()V",
            shift = At.Shift.AFTER
        )
    )
    private void drawCursorItemUnderlay(
        GuiGraphics drawContext,
        int x,
        int y,
        CallbackInfo ci,
        @Local ItemStack itemStack
    ) {
        if (itemStack.isEmpty()) return;

        DrawSingleItemStackEvent.drawSingleItemStack(
            new ItemRenderData(
                drawContext,
                itemStack,
                x - 8,
                y - 8,
                0
            )
        );
    }
}
//#endif

