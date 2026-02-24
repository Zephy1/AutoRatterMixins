package org.zephy.autoratter;

//#if MC>=12106
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DrawSingleItemStackEvent {
    private static final Map<String, DrawSingleItemStackEvent.Draw> registeredListeners = new ConcurrentHashMap<>();

    public static void register(String eventID, DrawSingleItemStackEvent.Draw listener) {
        if (registeredListeners.containsKey(eventID)) {
            return;
        }

        registeredListeners.put(eventID, listener);
        DRAW_ITEM_RENDER_DATA.register(listener);
    }

    public static final Event<DrawSingleItemStackEvent.Draw> DRAW_ITEM_RENDER_DATA = EventFactory.createArrayBacked(DrawSingleItemStackEvent.Draw.class,
        listeners -> (itemRenderData) -> {
            for (DrawSingleItemStackEvent.Draw listener : listeners) {
                listener.onDrawSingleItem(itemRenderData);
            }
        }
    );

    public static void drawSingleItemStack(ItemRenderData itemRenderData) {
        DRAW_ITEM_RENDER_DATA.invoker().onDrawSingleItem(itemRenderData);
    }

    @FunctionalInterface
    public interface Draw {
        void onDrawSingleItem(ItemRenderData itemRenderData);
    }
}
//#endif
