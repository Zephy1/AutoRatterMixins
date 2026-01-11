package org.zephy.autoratter;

//#if MC==10809

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.List;

public class DrawBatchedItemStackListEventHandler {
    private final Callback callback;

    public DrawBatchedItemStackListEventHandler(Callback callback) {
        this.callback = callback;
    }

    @SubscribeEvent
    public void onDrawBatchedItems(DrawBatchedItemStackListEvent event) {
        callback.onDrawBatchedItems(event.getItemRenderDataList());
    }

    @FunctionalInterface
    public interface Callback {
        void onDrawBatchedItems(List<ItemRenderData> itemRenderDataList);
    }
}
//#endif
