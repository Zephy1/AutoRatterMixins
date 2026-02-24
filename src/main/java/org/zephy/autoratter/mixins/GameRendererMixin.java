package org.zephy.autoratter.mixins;

//#if MC>=12100
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zephy.autoratter.DrawBatchedItemStackListEvent;

@Mixin(GameRenderer.class)
abstract class GameRendererMixin {
    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            //#if MC<=12105
            //$$target = "Lnet/minecraft/client/gui/GuiGraphics;draw()V",
            //$$ordinal = 0,
            //#else
            target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V",
            //#endif
            shift = At.Shift.BEFORE
        )
    )
    private void flushHudItems(
        DeltaTracker tickCounter,
        boolean tick,
        CallbackInfo callback
    ) {
        DrawBatchedItemStackListEvent.finishFrame(0);
    }

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            //#if MC<=12105
            //$$target = "Lnet/minecraft/client/gui/GuiGraphics;draw()V",
            //$$ordinal = 1,
            //#else
            target = "Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V",
            //#endif
            shift = At.Shift.BEFORE
        )
    )
    private void flushScreenItems(
        DeltaTracker tickCounter,
        boolean tick,
        CallbackInfo callback
    ) {
        DrawBatchedItemStackListEvent.finishFrame(1);
    }
}
//#endif
