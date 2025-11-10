package org.zephy.autoratter.mixins;

//#if MC>=12100
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zephy.autoratter.DrawItemStackOverlayEvent;

@Mixin(DrawContext.class)
abstract class DrawContextMixin {
	@Unique
    MinecraftClient mc = MinecraftClient.getInstance();

	@Inject(
		method = "drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
		at = @At("RETURN")
	)
	public void drawStackOverlay(TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String stackCountText, CallbackInfo callback) {
		if (!stack.isEmpty()) {
			var currentScreen = mc.currentScreen;
			var z = 0;

			if (currentScreen == null) {
				z = 700;
			} else {
				if ((
					currentScreen instanceof GenericContainerScreen ||
					currentScreen instanceof CreativeInventoryScreen ||
					currentScreen instanceof InventoryScreen ||
					currentScreen instanceof GameMenuScreen
				)) {
					if (currentScreen instanceof HandledScreenAccessor accessor) {
						x += accessor.getX();
						y += accessor.getY();
					}
				} else if ((
					currentScreen instanceof ChatScreen
				)) {
					z = 700;
				}
			}

            DrawItemStackOverlayEvent
                .DRAW_ITEM_STACK
                .invoker()
                .onDrawItemStackOverlay((DrawContext)(Object)this, textRenderer, stack, x, y, z);
		}
	}
}
//#endif
