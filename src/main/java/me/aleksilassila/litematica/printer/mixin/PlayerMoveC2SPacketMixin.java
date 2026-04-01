package me.aleksilassila.litematica.printer.mixin;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// NOTE: Faking rotation in movement packets causes Minecraft 1.21.11's server-side
// input simulation to use the wrong direction, creating a position-correction flood
// that both locks movement and triggers a kick for sending too many packets.
// These hooks are intentionally left as pass-through.
@Mixin(ServerboundMovePlayerPacket.class)
public class PlayerMoveC2SPacketMixin {
    @ModifyVariable(method = "<init>(DDDFFZZZZ)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifyLookYaw(float yaw) {
        return yaw;
    }

    @ModifyVariable(method = "<init>(DDDFFZZZZ)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private static float modifyLookPitch(float pitch) {
        return pitch;
    }
}
