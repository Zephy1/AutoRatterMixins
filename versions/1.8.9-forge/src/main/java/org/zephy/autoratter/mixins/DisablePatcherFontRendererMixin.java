package org.zephy.autoratter.mixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "club.sk1er.patcher.hooks.FontRendererHook", remap = false, priority = 1111)
public final class DisablePatcherFontRendererMixin {
    @Inject(
        method = "renderStringAtPos(Ljava/lang/String;Z)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    public void autoratter$disablePatcherFontRenderer(@NotNull final String text, final boolean shadow, @NotNull final CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
