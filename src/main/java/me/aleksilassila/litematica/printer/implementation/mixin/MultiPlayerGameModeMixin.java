package me.aleksilassila.litematica.printer.implementation.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin {
    @ModifyVariable(method = "useItemOn", at = @At("HEAD"), argsOnly = true)
    private LocalPlayer modifyPlayerForUseItemOn(LocalPlayer originalPlayer) {
        // Rotation is now applied directly in InteractActionImpl before calling useItemOn,
        // so the real player is always passed here — keeping prediction system intact.
        return originalPlayer;
    }
}
