package org.zephy.autoratter.mixins;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = FontRenderer.class, priority = 1111)
public abstract class ColoredHexCodesMixin {
    @Shadow(remap = false)
    protected abstract void setColor(float r, float g, float b, float a2);

    @Shadow
    private int textColor;
    @Shadow
    private float alpha;

    @Unique
    private int autoratter$colorSR;
    @Unique
    private int autoratter$colorState;

    @Unique
    private static boolean autoratter$isSpecial = false;

    @Inject(
        method = "renderStringAtPos",
        at = @At("HEAD")
    )
    private void resetStateWhenRendering(String text, boolean shadow, CallbackInfo ci) {
        autoratter$colorSR = 0;
        autoratter$colorState = -1;
    }

    @Inject(
        method = "isFormatSpecial",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private static void protectFormatCodesSpecial(char formatChar, CallbackInfoReturnable<Boolean> cir) {
        if (formatChar == '/') {
            autoratter$isSpecial = false;
            cir.setReturnValue(true);
        } else if (autoratter$isSpecial) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "isFormatColor",
        at = @At(value = "HEAD"),
        cancellable = true
    )
    private static void protectFormatCodesSimple(char formatChar, CallbackInfoReturnable<Boolean> cir) {
        if (formatChar == '#') {
            autoratter$isSpecial = true;
            cir.setReturnValue(true);
        } else if (autoratter$isSpecial) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
        method = "getFormatFromString",
        at = @At(value = "TAIL")
    )
    private static void resetState(String text, CallbackInfoReturnable<String> cir) {
        autoratter$isSpecial = false;
    }

    @Inject(
        method = "getFormatFromString",
        at = @At(value = "HEAD")
    )
    private static void resetStateAtHead(String text, CallbackInfoReturnable<String> cir) {
        autoratter$isSpecial = false;
    }

    @Inject(
        method = "renderStringAtPos",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/FontRenderer;setColor(FFFF)V",
            ordinal = 0,
            shift = At.Shift.AFTER,
            remap = false
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onChooseColor(
        String text,
        boolean shadow,
        CallbackInfo ci,
        int i,
        char c0,
        int i1
    ) {
        char c = text.charAt(i + 1);
        int hexCode = "0123456789abcdef".indexOf(c);
        if (c == '#') {
            if (autoratter$colorState != -1) {
                throw new IllegalStateException("Encountered §# while inside push sequence.");
            }
            autoratter$colorState = 0;
            autoratter$colorSR = 0;
        } else if (c == '/') {
            if (autoratter$colorState != 8 && autoratter$colorState != 6) {
                throw new IllegalStateException("Encountered §/ without encountering enough pushes: " + autoratter$colorState);
            }

            textColor = autoratter$colorSR;
            int shadowDivisor = shadow ? 4 : 1;
            setColor(
                (autoratter$colorSR >> 16 & 0xFF) / 255f / shadowDivisor,
                (autoratter$colorSR >> 8 & 0xFF) / 255f / shadowDivisor,
                (autoratter$colorSR & 0xFF) / 255f / shadowDivisor,
                (autoratter$colorState == 8 ? (autoratter$colorSR >> 24 & 0xFF) / 255f : this.alpha)
            );
            autoratter$colorState = -1;
        } else if (0 <= hexCode && autoratter$colorState != -1) {
            autoratter$colorState++;
            if (autoratter$colorState > 8)  {
                throw new IllegalStateException("Encountered too many pushes inside of §#§/ sequence");
            }
            autoratter$colorSR = (autoratter$colorSR << 4) | hexCode;
        }
    }
}
