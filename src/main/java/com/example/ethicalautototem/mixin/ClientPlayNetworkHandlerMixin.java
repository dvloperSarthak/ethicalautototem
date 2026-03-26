package com.example.ethicalautototem.mixin;

import com.example.ethicalautototem.EthicalAutoTotemClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onEntityStatus", at = @At("TAIL"))
    private void ethicalautototem$trackTotemPop(EntityStatusS2CPacket packet, CallbackInfo ci) {
        // 35 = totem activation animation/event.
        if (packet.getStatus() == 35) {
            EthicalAutoTotemClient.onTotemPop();
        }
    }
}
