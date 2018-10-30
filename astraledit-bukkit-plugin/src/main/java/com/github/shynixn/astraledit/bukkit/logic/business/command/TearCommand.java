package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.controller.SelectionController;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationImpl;
import com.github.shynixn.astraledit.api.bukkit.business.controller.OperationType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TearCommand implements PlayerCommand {
    private final Plugin plugin;
    private final SelectionController controller;

    /**
     * Creates an instance of TearCommand which depends on
     * a SelectionController and a Plugin
     *
     * @param controller SelectionController
     * @param plugin Plugin
     */
    public TearCommand(SelectionController controller, Plugin plugin) {
        this.controller = controller;
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
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
                if (!this.controller.hasSelection(player)) {
                    player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
                } else if (this.controller.getSelection(player).isJoined()) {
                    this.controller.getSelection(player).tearApart();
                    this.controller.addOperation(player, new OperationImpl(OperationType.UNCOMBINE));
                }
            });
            return true;
        }
    }
}
