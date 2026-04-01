package me.aleksilassila.litematica.printer.implementation.actions;

import me.aleksilassila.litematica.printer.LitematicaMixinMod;
import me.aleksilassila.litematica.printer.actions.InteractAction;
import me.aleksilassila.litematica.printer.actions.PrepareAction;
import me.aleksilassila.litematica.printer.implementation.PrinterPlacementContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;

public class InteractActionImpl extends InteractAction {
    public InteractActionImpl(PrinterPlacementContext context) {
        super(context);
    }

    @Override
    protected void interact(Minecraft client, LocalPlayer player, InteractionHand hand, BlockHitResult hitResult) {
        if (client.gameMode == null) return;

        PrepareAction lookAction = LitematicaMixinMod.printer != null
                ? LitematicaMixinMod.printer.actionHandler.lookAction : null;

        if (lookAction != null && (lookAction.modifyYaw || lookAction.modifyPitch)) {
            float savedYaw = player.getYRot();
            float savedPitch = player.getXRot();
            if (lookAction.modifyYaw) player.setYRot(lookAction.yaw);
            if (lookAction.modifyPitch) player.setXRot(lookAction.pitch);
            client.gameMode.useItemOn(player, hand, hitResult);
            player.setYRot(savedYaw);
            player.setXRot(savedPitch);
        } else {
            client.gameMode.useItemOn(player, hand, hitResult);
        }
    }
}
