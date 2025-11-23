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
        at = @At("RETURN")
    )
    public void finishDrawingFrame(
        RenderTickCounter tickCounter,
        boolean tick,
        CallbackInfo callback
    ) {
        DrawBatchedItemStackListEvent.finishFrame();
    }
}
//#endif
