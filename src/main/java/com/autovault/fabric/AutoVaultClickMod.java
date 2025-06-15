package com.autovault.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.client.MinecraftClient;

public class AutoVaultClickMod implements ClientModInitializer {
    private boolean clicked = false;
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Screen s = client.currentScreen;
            if (!clicked && s != null
                && s.getClass().getSimpleName().contains("Vault")
                && s.getClass().getSimpleName().contains("RewardScreen")) {
                try {
                    var screen = (HandledScreen<?>) s;
                    screen.getScreenHandler().slots.forEach(slot -> {
                        ItemStack stack = slot.getStack();
                        if (!stack.isEmpty()
                            && stack.getTranslationKey().contains("heavy_core")) {
                            client.interactionManager.clickSlot(
                                screen.getScreenHandler().syncId,
                                slot.id, 0, SlotActionType.PICKUP, client.player);
                            clicked = true;
                        }
                    });
                } catch (Exception ignored) {}
            }
            if (clicked && (s == null
                || !s.getClass().getSimpleName().contains("RewardScreen"))) {
                clicked = false;
            }
        });
    }
}
