package org.zephy.autoratter.mixins;

//#if MC>=12100
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
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
            //$$target = "Lnet/minecraft/client/gui/DrawContext;draw()V",
            //$$ordinal = 0,
            //#else
            target = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            //#endif
            shift = At.Shift.BEFORE
        )
    )
    private void flushHudItems(
        RenderTickCounter tickCounter,
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
            //$$target = "Lnet/minecraft/client/gui/DrawContext;draw()V",
            //$$ordinal = 1,
            //#else
            target = "Lnet/minecraft/client/gui/render/GuiRenderer;render(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V",
            //#endif
            shift = At.Shift.BEFORE
        )
    )
    private void flushScreenItems(
        RenderTickCounter tickCounter,
        boolean tick,
        CallbackInfo callback
    ) {
        DrawBatchedItemStackListEvent.finishFrame(1);
    }
}
//#endif
