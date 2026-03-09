package org.zephy.autoratter.mixins;

//#if MC>=12100
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.zephy.autoratter.DarkHexColorReadabilityOptions;
import org.zephy.autoratter.DrawSingleItemStackEvent;
import org.zephy.autoratter.ItemRenderData;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //#if MC>=12105
    @Shadow
    public abstract void fill(int i, int j, int k, int l, int m);
    @Shadow
    public abstract int guiWidth();
    @Shadow
    public abstract int guiHeight();
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

    //#if MC>=12105
    private static final Pattern HEX_PATTERN = Pattern.compile("#([0-9a-fA-F]{6})");

    @Inject(
        method = "renderTooltip",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/TooltipRenderUtil;renderTooltipBackground(Lnet/minecraft/client/gui/GuiGraphics;IIIILnet/minecraft/resources/Identifier;)V",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void preRenderTooltip(
        Font font,
        List<ClientTooltipComponent> list,
        int x,
        int y,
        ClientTooltipPositioner clientTooltipPositioner,
        Identifier identifier,
        CallbackInfo ci
    ) {
        if (!DarkHexColorReadabilityOptions.GetIsEnabled()) return;
        int width = 0;
        int height = list.size() == 1 ? -2 : 0;

        for (ClientTooltipComponent component : list) {
            int w = component.getWidth(font);
            if (w > width) width = w;
            height += component.getHeight(font);
        }

        Vector2ic pos = clientTooltipPositioner.positionTooltip(
            this.guiWidth(),
            this.guiHeight(),
            x,
            y,
            width,
            height
        );

        int px = pos.x();
        int py = pos.y();
        int lineY = py;

        for (int i = 0; i < list.size(); i++) {
            ClientTooltipComponent component = list.get(i);

            if (component instanceof ClientTextTooltipAccessor accessor) {
                FormattedCharSequence sequence = accessor.getText();
                StringBuilder textBuilder = new StringBuilder();
                sequence.accept((index, style, codePoint) -> {
                    textBuilder.appendCodePoint(codePoint);
                    return true;
                });
                String text = textBuilder.toString();
                Matcher matcher = HEX_PATTERN.matcher(text);

                while (matcher.find()) {
                    String hexColor = matcher.group(1);
                    int colorInt = Integer.parseInt(hexColor, 16);
                    if (isDarkColor(colorInt)) {
                        int padding = 1;
                        this.fill(
                            px - (2 * padding),
                            lineY - padding,
                            px + width + (2 * padding),
                            lineY + component.getHeight(font) - padding,
                            DarkHexColorReadabilityOptions.GetBackgroundColorInt()
                        );
                    }
                }
                lineY += component.getHeight(font) + (i == 0 ? 2 : 0);
            }
        }
    }

    private boolean isDarkColor(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        double luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255.0;
        return luminance < DarkHexColorReadabilityOptions.GetLuminanceThreshold();
    }
    //#endif
}
//#endif
