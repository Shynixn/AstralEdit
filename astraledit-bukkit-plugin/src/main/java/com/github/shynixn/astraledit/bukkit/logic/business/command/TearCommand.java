package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.Operation;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationType;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TearCommand implements PlayerCommand {

    private Plugin plugin;
    private SelectionManager manager;

    /**
     * Creates an instance of TearCommand which depends on
     * a SelectionManager and a Plugin
     *
     * @param manager SelectionManager
     * @param plugin Plugin
     */
    public TearCommand(SelectionManager manager, Plugin plugin) {
        this.manager = manager;
        this.plugin = plugin;
    }

    /**
     * Executes the given command if the arguments match.
     *
     * @param player executing the command.
     * @param args   arguments.
     * @return True if this command was executed. False if the arguments do not match.
     */
    @Override
    public boolean onPlayerExecuteCommand(Player player, String[] args) {
        if (args.length != 1 || !args[0].equalsIgnoreCase("tear") || !Permission.TEAR.hasPermission(player)) {
            return false;
        } else {
            plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
                if (!manager.hasSelection(player)) {
                    player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
                } else if (manager.getSelection(player).isJoined()) {
                    manager.getSelection(player).tearApart();
                    manager.addOperation(player, new Operation(OperationType.UNCOMBINE));
                }
            });
            return true;
        }

    }
}
