package org.zephy.autoratter;

//#if MC>=12100
import net.minecraft.item.ItemStack;

public class ItemRenderData {
    public final ItemStack itemStack;
    public final int x, y;

    public ItemRenderData(ItemStack itemStack, int x, int y) {
        this.itemStack = itemStack;
        this.x = x;
        this.y = y;
    }
}
//#endif
