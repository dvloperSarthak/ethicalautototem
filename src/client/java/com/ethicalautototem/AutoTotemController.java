package com.ethicalautototem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public final class AutoTotemController {
    private static final int OFFHAND_SLOT_ID = 45;
    private static final int REEQUIP_DELAY_TICKS = 16; // 0.8 sec at 20 TPS

    private static int reEquipAtTick = -1;

    private AutoTotemController() {
    }

    public static void onLocalTotemPop() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }

        int now = client.player.age;
        reEquipAtTick = now + REEQUIP_DELAY_TICKS;

        if (!(client.currentScreen instanceof InventoryScreen)) {
            client.setScreen(new InventoryScreen(client.player));
        }
    }

    public static void onEndTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) {
            return;
        }

        if (reEquipAtTick < 0 || client.player.age < reEquipAtTick) {
            return;
        }

        if (!(client.currentScreen instanceof InventoryScreen)) {
            client.setScreen(new InventoryScreen(client.player));
            return;
        }

        if (moveTotemToOffhand(client)) {
            reEquipAtTick = -1;
        }
    }

    private static boolean moveTotemToOffhand(MinecraftClient client) {
        ScreenHandler handler = client.player.playerScreenHandler;
        if (handler.getSlot(OFFHAND_SLOT_ID).getStack().isOf(Items.TOTEM_OF_UNDYING)) {
            return true;
        }

        int totemSlotId = findTotemSlot(handler);
        if (totemSlotId < 0) {
            return false;
        }

        int syncId = handler.syncId;
        client.interactionManager.clickSlot(syncId, totemSlotId, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(syncId, OFFHAND_SLOT_ID, 0, SlotActionType.PICKUP, client.player);

        ItemStack cursor = handler.getCursorStack();
        if (!cursor.isEmpty()) {
            client.interactionManager.clickSlot(syncId, totemSlotId, 0, SlotActionType.PICKUP, client.player);
        }

        return handler.getSlot(OFFHAND_SLOT_ID).getStack().isOf(Items.TOTEM_OF_UNDYING);
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
