package com.ethicalautototem.mixin;

import com.ethicalautototem.AutoTotemController;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onEntityStatus", at = @At("TAIL"))
    private void ethicalautototem$onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        if (packet.getStatus() != 35) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) {
            return;
        }

        Entity entity = packet.getEntity(client.world);
        if (entity != null && entity.getId() == client.player.getId()) {
            AutoTotemController.onLocalTotemPop();
        }
    }
}
