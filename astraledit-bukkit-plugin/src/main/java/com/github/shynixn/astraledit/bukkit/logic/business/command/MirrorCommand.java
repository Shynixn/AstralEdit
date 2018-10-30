package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.controller.SelectionController;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationImpl;
import com.github.shynixn.astraledit.api.bukkit.business.controller.OperationType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class MirrorCommand implements PlayerCommand {

    private static final String commandName = "mirror";

    private final Plugin plugin;

    private final SelectionController controller;

    public MirrorCommand(Plugin plugin, SelectionController SelectionController) {
        this.plugin = Objects.requireNonNull(plugin);
        this.controller = Objects.requireNonNull(SelectionController);
    }

    /**
     * Mirrors the given selection
     *
     * @param player executing the command.
     * @param args   arguments.
     */
    @Override
    public boolean onPlayerExecuteCommand(Player player, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase(commandName) && Permission.MIRROR.hasPermission(player)) {
            this.plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                if (!this.controller.hasSelection(player)) {
                    player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
                } else {
                    this.controller.getSelection(player).mirror();
                    this.controller.addOperation(player, new OperationImpl(OperationType.MIRROR));
                }
            });
            return true;
        } else {
            return false;
        }
    }
}
