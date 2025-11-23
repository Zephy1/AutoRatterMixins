package org.zephy.autoratter.mixins;

//#if MC>=12100
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zephy.autoratter.DrawBatchedItemStackListEvent;
import org.zephy.autoratter.ItemRenderData;

//#if MC<=12105
//$$import org.joml.Matrix4f;
//$$import org.joml.Vector3f;
//$$import net.minecraft.client.util.math.MatrixStack;
//$$import org.spongepowered.asm.mixin.Final;
//$$import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(DrawContext.class)
abstract class DrawContextMixin {
    //#if MC<=12105
    //$$@Shadow
    //$$@Final
    //$$private MatrixStack matrices;
    //#endif

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

        DrawBatchedItemStackListEvent.addItemRenderData(
            new ItemRenderData(
                (DrawContext)(Object)this,
                stack,
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
