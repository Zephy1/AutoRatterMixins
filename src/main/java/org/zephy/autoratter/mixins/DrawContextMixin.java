package org.zephy.autoratter.mixins;

//#if MC>=12100
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
//import net.minecraft.client.gui.render.state.ItemGuiElementRenderState;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
//import net.minecraft.client.render.item.KeyedItemRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.zephy.autoratter.DrawItemStackOverlayEvent;

import java.lang.Math;
import java.util.ArrayDeque;

@Mixin(DrawContext.class)
abstract class DrawContextMixin {
    //#if MC<=12105
    //$$@Shadow
    //$$@Final
    //$$private MatrixStack matrices;
    //#endif

    @Unique
    MinecraftClient mc = MinecraftClient.getInstance();

//	@Inject(
//		method = "drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",
//		at = @At("RETURN")
//	)
//	public void drawStackOverlay(TextRenderer textRenderer, ItemStack stack, int x, int y, @Nullable String stackCountText, CallbackInfo callback) {
//		if (!stack.isEmpty()) {
//			var currentScreen = mc.currentScreen;
//			var z = 0;
//
//			if (currentScreen == null) {
//				z = 700;
//			} else {
//				if ((
//					currentScreen instanceof GenericContainerScreen ||
//					currentScreen instanceof CreativeInventoryScreen ||
//					currentScreen instanceof InventoryScreen ||
//					currentScreen instanceof GameMenuScreen
//				)) {
//					if (currentScreen instanceof HandledScreenAccessor accessor) {
//						x += accessor.getX();
//						y += accessor.getY();
//					}
//				} else if ((
//					currentScreen instanceof ChatScreen
//				)) {
//					z = 700;
//				}
//			}
//
//            DrawItemStackOverlayEvent
//                .DRAW_ITEM_STACK
//                .invoker()
//                .onDrawItemStackOverlay((DrawContext)(Object)this, textRenderer, stack, x, y, z);
//		}
//	}






    @Inject(
        //#if MC<=12105
        //$$method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V",
        //#else
        method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;III)V",
        //#endif
        at = @At("RETURN")
    )
    public void drawItem(
        @Nullable LivingEntity entity,
        @Nullable World world,
        ItemStack stack,
        int x,
        int y,
        int seed,
        //#if MC<=12105
        //$$int z,
        //#endif
        CallbackInfo callback
    ) {
        if (stack.isEmpty()) return;

        //#if MC<=12105
        //$$Matrix4f matrices = this.matrices.peek().getPositionMatrix();
        //$$Vector3f transformedPos = new Vector3f(x, y, 0);
        //$$matrices.transformPosition(transformedPos);
        //$$int screenX = Math.round(transformedPos.x);
        //$$int screenY = Math.round(transformedPos.y);
        //#else
        int screenX = x;
        int screenY = y;
        //#endif




//            var currentScreen = mc.currentScreen;
            var newZ = 0;
//
//            if (currentScreen == null) {
//                newZ = 700;
//            } else {
//                if ((
//                    currentScreen instanceof GenericContainerScreen ||
//                        currentScreen instanceof CreativeInventoryScreen ||
//                        currentScreen instanceof InventoryScreen ||
//                        currentScreen instanceof GameMenuScreen
//                )) {
//                    if (currentScreen instanceof HandledScreenAccessor accessor) {
//                        x += accessor.getX();
//                        y += accessor.getY();
//                    }
//                } else if ((
//                        currentScreen instanceof ChatScreen
//                )) {
//                    newZ = 700;
//                }
//            }

            DrawItemStackOverlayEvent
                .DRAW_ITEM_STACK
                .invoker()
                .onDrawItemStackOverlay((DrawContext)(Object)this, stack, screenX, screenY, newZ);
    }
}
//#endif
