package org.zephy.autoratter;

//#if MC>=12100
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DrawItemStackOverlayEvent {
    private static final Map<String, Draw> registeredListeners = new ConcurrentHashMap<>();

    public static void register(String eventID, Draw listener) {
        if (registeredListeners.containsKey(eventID)) {
            return;
        }

        registeredListeners.put(eventID, listener);
        DRAW_ITEM_STACK.register(listener);
    }

    public static final Event<Draw> DRAW_ITEM_STACK = EventFactory.createArrayBacked(Draw.class,
        listeners -> (drawContext, textRenderer, itemStack, x, y, z) -> {
            for (Draw listener : listeners) {
                listener.onDrawItemStackOverlay(drawContext, textRenderer, itemStack, x, y, z);
            }
        }
    );

    @FunctionalInterface
    public interface Draw {
        void onDrawItemStackOverlay(DrawContext context, TextRenderer textRenderer, ItemStack stack, int x, int y, int z);
    }
}
//#endif
