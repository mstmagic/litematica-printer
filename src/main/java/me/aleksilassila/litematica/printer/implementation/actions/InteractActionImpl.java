package me.aleksilassila.litematica.printer.implementation.actions;

import me.aleksilassila.litematica.printer.LitematicaMixinMod;
import me.aleksilassila.litematica.printer.actions.InteractAction;
import me.aleksilassila.litematica.printer.actions.PrepareAction;
import me.aleksilassila.litematica.printer.implementation.PrinterPlacementContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

        sendPlacementMessage(player);
    }

    private void sendPlacementMessage(LocalPlayer player) {
        BlockPos pos = context.targetBlockPos != null
                ? context.targetBlockPos
                : context.getClickedPos().relative(context.getClickedFace());

        String itemName = !context.getItemInHand().isEmpty()
                ? context.getItemInHand().getHoverName().getString()
                : "block";

        player.displayClientMessage(
                Component.literal("[Printer] ").withStyle(ChatFormatting.GREEN)
                        .append(Component.literal(itemName).withStyle(ChatFormatting.YELLOW))
                        .append(Component.literal(" at ").withStyle(ChatFormatting.WHITE))
                        .append(Component.literal(pos.getX() + ", " + pos.getY() + ", " + pos.getZ()).withStyle(ChatFormatting.AQUA)),
                false
        );
    }
}
