package org.zephy.autoratter;

//#if MC==10809
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class DrawBatchedItemStackListEvent extends Event {
    private static final List<ItemRenderData> batchedItemRenderDataList = new ArrayList<>();

    private final List<ItemRenderData> itemRenderDataList;

    public DrawBatchedItemStackListEvent(List<ItemRenderData> itemRenderDataList) {
        this.itemRenderDataList = itemRenderDataList;
    }

    public List<ItemRenderData> getItemRenderDataList() {
        return itemRenderDataList;
    }

    public static void addItemRenderData(ItemRenderData itemRenderData) {
        batchedItemRenderDataList.add(itemRenderData);
    }

    public static void finishFrame() {
        if (batchedItemRenderDataList.isEmpty()) return;

        MinecraftForge.EVENT_BUS.post(new DrawBatchedItemStackListEvent(
            new ArrayList<>(batchedItemRenderDataList)
        ));
        batchedItemRenderDataList.clear();
    }
}
//#endif
