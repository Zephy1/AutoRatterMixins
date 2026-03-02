package org.zephy.autoratter.mixins;

//#if MC>=12100
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.zephy.autoratter.DrawSingleItemStackEvent;
import org.zephy.autoratter.ItemRenderData;

//#if MC<=12105
//$$import org.joml.Matrix4f;
//$$import org.joml.Vector3f;
//$$import com.mojang.blaze3d.vertex.PoseStack;
//$$import org.spongepowered.asm.mixin.Final;
//$$import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(GuiGraphics.class)
abstract class DrawContextMixin {
    //#if MC<=12105
    //$$@Shadow
    //$$@Final
    //$$private PoseStack pose;
    //#endif

    @Inject(
        //#if MC<=12105
        //$$method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;IIII)V",
        //#else
        method = "renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
        //#endif
        at = @At("HEAD")
    )
    public void drawItem(
        @Nullable LivingEntity entity,
        @Nullable Level world,
        ItemStack itemStack,
        int x,
        int y,
        int seed,
        //#if MC<=12105
        //$$int z,
        //#endif
        CallbackInfo callback
    ) {
        if (itemStack.isEmpty()) return;

        //#if MC<=12105
        //$$Matrix4f matrices = this.pose.last().pose();
        //$$Vector3f transformedPos = new Vector3f(x, y, 0);
        //$$matrices.transformPosition(transformedPos);
        //$$int screenX = Math.round(transformedPos.x);
        //$$int screenY = Math.round(transformedPos.y);
        //#else
        int screenX = x;
        int screenY = y;
        //#endif

        DrawSingleItemStackEvent.drawSingleItemStack(
            new ItemRenderData(
                (GuiGraphics)(Object)this,
                itemStack,
                screenX,
                screenY,
                //#if MC<=12105
                //$$z
                //#else
                0
                //#endif
            )
        );
    }
}
//#endif
