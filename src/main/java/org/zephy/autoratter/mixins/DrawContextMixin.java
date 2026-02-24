package org.zephy.autoratter.mixins;

//#if MC>=12100
//#if MC<=12105
//$$import net.minecraft.client.gui.GuiGraphics;
//$$import net.minecraft.world.level.Level;
//$$import org.jetbrains.annotations.Nullable;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$import org.spongepowered.asm.mixin.injection.At;
//$$import org.spongepowered.asm.mixin.injection.Inject;
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$import org.zephy.autoratter.DrawBatchedItemStackListEvent;
//$$import org.zephy.autoratter.ItemRenderData;
//$$import org.joml.Matrix4f;
//$$import org.joml.Vector3f;
//$$import com.mojang.blaze3d.vertex.PoseStack;
//$$import org.spongepowered.asm.mixin.Final;
//$$import org.spongepowered.asm.mixin.Shadow;
//$$import net.minecraft.world.entity.LivingEntity;
//$$import net.minecraft.world.item.ItemStack;
//$$
//$$@Mixin(GuiGraphics.class)
//$$abstract class DrawContextMixin {
//$$    @Shadow
//$$    @Final
//$$    private PoseStack pose;
//$$
//$$    @Inject(
//$$        method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
//$$        at = @At("RETURN")
//$$    )
//$$    public void drawItem(
//$$        @Nullable LivingEntity entity,
//$$        @Nullable Level world,
//$$        ItemStack stack,
//$$        int x,
//$$        int y,
//$$        int seed,
//$$        int z,
//$$        CallbackInfo callback
//$$    ) {
//$$        if (stack.isEmpty()) return;
//$$
//$$        Matrix4f matrices = this.pose.last().pose();
//$$        Vector3f transformedPos = new Vector3f(x, y, 0);
//$$        matrices.transformPosition(transformedPos);
//$$        int screenX = Math.round(transformedPos.x);
//$$        int screenY = Math.round(transformedPos.y);
//$$
//$$        DrawBatchedItemStackListEvent.addItemRenderData(
//$$            new ItemRenderData(
//$$                (GuiGraphics)(Object)this,
//$$                stack,
//$$                screenX,
//$$                screenY,
//$$                z
//$$            )
//$$        );
//$$    }
//$$}
//#endif
//#endif
