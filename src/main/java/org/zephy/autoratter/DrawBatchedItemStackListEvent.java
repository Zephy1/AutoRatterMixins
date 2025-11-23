package org.zephy.autoratter;

//#if MC>=12100
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DrawBatchedItemStackListEvent {
    private static final Map<String, DrawBatchedItemStackListEvent.Draw> registeredListeners = new ConcurrentHashMap<>();
    private static final List<ItemRenderData> batchedItemRenderDataList = new ArrayList<>();

    public static void register(String eventID, DrawBatchedItemStackListEvent.Draw listener) {
        if (registeredListeners.containsKey(eventID)) {
            return;
        }

        registeredListeners.put(eventID, listener);
        DRAW_ITEM_RENDER_DATA_LIST.register(listener);
    }

    public static final Event<DrawBatchedItemStackListEvent.Draw> DRAW_ITEM_RENDER_DATA_LIST = EventFactory.createArrayBacked(DrawBatchedItemStackListEvent.Draw.class,
        listeners -> (itemRenderDataList, zOffset) -> {
            for (DrawBatchedItemStackListEvent.Draw listener : listeners) {
                listener.onDrawBatchedItems(itemRenderDataList, zOffset);
            }
        }
    );

    public static void addItemRenderData(ItemRenderData itemRenderData) {
        batchedItemRenderDataList.add(itemRenderData);
    }
    public static void finishFrame(int screenType) {
        if (batchedItemRenderDataList.isEmpty()) return;

        int zOffset = screenType == 0 ? 510 : 0;

        DRAW_ITEM_RENDER_DATA_LIST.invoker().onDrawBatchedItems(batchedItemRenderDataList, zOffset);
        batchedItemRenderDataList.clear();
    }

    @FunctionalInterface
    public interface Draw {
        void onDrawBatchedItems(List<ItemRenderData> itemRenderDataList, int zOffset);
    }
}
//#endif
