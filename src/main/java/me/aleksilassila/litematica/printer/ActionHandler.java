package me.aleksilassila.litematica.printer;

import me.aleksilassila.litematica.printer.actions.Action;
import me.aleksilassila.litematica.printer.actions.InteractAction;
import me.aleksilassila.litematica.printer.actions.PrepareAction;
import me.aleksilassila.litematica.printer.config.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ActionHandler {
    private final Minecraft client;
    private final LocalPlayer player;
    private final Queue<Action> actionQueue = new LinkedList<>();
    public PrepareAction lookAction = null;

    public ActionHandler(Minecraft client, LocalPlayer player) {
        this.client = client;
        this.player = player;
    }

    private int tick = 0;
    // Counts down after each block placement (InteractAction). The next InteractAction
    // will not fire until this reaches zero, enforcing a minimum gap between placements.
    private int blockPlacementCooldown = 0;

    public void onGameTick() {
        if (blockPlacementCooldown > 0) {
            blockPlacementCooldown--;
        }

        int tickRate = Configs.PRINTING_INTERVAL.getIntegerValue();
        tick = tick % tickRate == tickRate - 1 ? 0 : tick + 1;

        if (tick % tickRate != 0) {
            return;
        }

        // If the next queued action is a block placement and the cooldown hasn't expired,
        // leave it in the queue and wait — prevents flooding UseItemOn packets.
        Action peeked = actionQueue.peek();
        if (peeked instanceof InteractAction && blockPlacementCooldown > 0) {
            return;
        }

        Action nextAction = actionQueue.poll();

        if (nextAction != null) {
            Printer.printDebug("Sending action {}", nextAction);
            nextAction.send(client, player);
            if (nextAction instanceof InteractAction) {
                blockPlacementCooldown = Configs.PLACEMENT_COOLDOWN_TICKS.getIntegerValue();
            }
        } else {
            lookAction = null;
        }
    }

    public boolean acceptsActions() {
        return actionQueue.isEmpty();
    }

    public void addActions(Action... actions) {
        if (!acceptsActions()) {
            return;
        }

        for (Action action : actions) {
            if (action instanceof PrepareAction) {
                lookAction = (PrepareAction) action;
            }
        }

        actionQueue.addAll(List.of(actions));
    }
}
