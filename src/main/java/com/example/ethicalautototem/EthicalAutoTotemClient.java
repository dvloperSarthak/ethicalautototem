package com.example.ethicalautototem;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

/**
 * Client-side totem restock helper.
 */
public final class EthicalAutoTotemClient implements ClientModInitializer {
    private static final long RESTOCK_DELAY_MS = 800L;

    private static volatile long poppedAt = -1L;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(EthicalAutoTotemClient::onEndTick);
    }

    public static void onTotemPop() {
        poppedAt = System.currentTimeMillis();
    }

    private static void onEndTick(MinecraftClient client) {
        if (poppedAt < 0L || client.player == null || client.world == null || client.interactionManager == null) {
            return;
        }

        if (System.currentTimeMillis() - poppedAt < RESTOCK_DELAY_MS) {
            return;
        }

        poppedAt = -1L;

        if (client.player.isDead()) {
            return;
        }

        ScreenHandler screenHandler = client.player.playerScreenHandler;
        int totemSlot = findTotemSlot(screenHandler);
        if (totemSlot < 0) {
            return;
        }

        // Open inventory so the movement is visible as requested.
        client.setScreen(new InventoryScreen(client.player));

        int syncId = screenHandler.syncId;
        int offhandSlot = 45;

        client.interactionManager.clickSlot(syncId, totemSlot, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(syncId, offhandSlot, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(syncId, totemSlot, 0, SlotActionType.PICKUP, client.player);
    }

    private static int findTotemSlot(ScreenHandler handler) {
        for (int i = 0; i < handler.slots.size(); i++) {
            ItemStack stack = handler.getSlot(i).getStack();
            if (stack.isOf(Items.TOTEM_OF_UNDYING)) {
                return i;
            }
        }
        return -1;
    }
}
