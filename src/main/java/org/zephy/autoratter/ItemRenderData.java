package org.zephy.autoratter;

//#if MC>=12100
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class ItemRenderData {
    public final DrawContext drawContext;
    public final ItemStack itemStack;
    public final int x, y, z;

    public ItemRenderData(DrawContext drawContext, ItemStack itemStack, int x, int y, int z) {
        this.drawContext = drawContext;
        this.itemStack = itemStack;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
//#endif
