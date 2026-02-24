package org.zephy.autoratter;

//#if MC>=12100
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class ItemRenderData {
    public final GuiGraphics drawContext;
    public final ItemStack itemStack;
    public final int x, y, z;

    public ItemRenderData(GuiGraphics drawContext, ItemStack itemStack, int x, int y, int z) {
        this.drawContext = drawContext;
        this.itemStack = itemStack;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
//#endif
