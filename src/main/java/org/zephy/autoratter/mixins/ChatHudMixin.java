package org.zephy.autoratter.mixins;

//#if MC>=12100
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChatComponent.class)
public class ChatHudMixin {
    @ModifyExpressionValue(
        method = {
            "addMessageToQueue(Lnet/minecraft/client/GuiMessage;)V",
            "addMessageToDisplayQueue"
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
