package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class ClearCommand implements PlayerCommand {

    private static final String commandName = "clear";

    private final Plugin plugin;

    private final SelectionManager manager;

    public ClearCommand(Plugin plugin, SelectionManager manager) {
        this.plugin = Objects.requireNonNull(plugin);
        this.manager = Objects.requireNonNull(manager);
    }

    /**
     * Clears the rendered object
     */
    @Override
    public boolean onPlayerExecuteCommand(Player player, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase(commandName) && Permission.CLEAR.hasPermission(player)) {
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
                if (!this.manager.hasSelection(player)) {
                    player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
                } else {
                    player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Destroying render ...");
                    this.manager.clearSelection(player);
                    player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Finished destroying render.");
                }
            });
            return true;
        }
        return false;
    }
}
