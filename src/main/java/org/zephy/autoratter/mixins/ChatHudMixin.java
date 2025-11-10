package org.zephy.autoratter.mixins;

//#if MC>=12100
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.hud.ChatHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    @ModifyExpressionValue(
        method = {
            "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V",
            "addVisibleMessage"
        },
        at = @At(
            value = "CONSTANT",
            args = "intValue=100"
        )
    )
    private int increaseMessageLimit(int hundred) {
        return 65535;
    }
}
//#endif
