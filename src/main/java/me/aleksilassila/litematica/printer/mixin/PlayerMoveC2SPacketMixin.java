package me.aleksilassila.litematica.printer.mixin;

import me.aleksilassila.litematica.printer.LitematicaMixinMod;
import me.aleksilassila.litematica.printer.actions.PrepareAction;
import me.aleksilassila.litematica.printer.config.Configs;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerboundMovePlayerPacket.class)
public class PlayerMoveC2SPacketMixin {
    @ModifyVariable(method = "<init>(DDDFFZZZZ)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static float modifyLookYaw(float yaw) {
        if (LitematicaMixinMod.printer == null || !Configs.ROTATE.getBooleanValue()) {
            return yaw;
        }
        PrepareAction action = LitematicaMixinMod.printer.actionHandler.lookAction;
        if (action == null || !action.modifyYaw) {
            return yaw;
        }
        return action.yaw;
    }

    @ModifyVariable(method = "<init>(DDDFFZZZZ)V", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private static float modifyLookPitch(float pitch) {
        if (LitematicaMixinMod.printer == null || !Configs.ROTATE.getBooleanValue()) {
            return pitch;
        }
        PrepareAction action = LitematicaMixinMod.printer.actionHandler.lookAction;
        if (action == null || !action.modifyPitch) {
            return pitch;
        }
        return action.pitch;
    }
}
