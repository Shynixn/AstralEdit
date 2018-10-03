package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.entity.Selection;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.Operation;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationType;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FlipCommand implements PlayerCommand {
    private final Plugin plugin;
    private final SelectionManager manager;

    /**
     * The Command flips the current selection depending on the Plugin and SelectionManager.
     *
     * @param plugin the Plugin
     * @param manager the current SelectionManager
     */
    public FlipCommand(Plugin plugin, SelectionManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    /**
     * Executes the given command if the arguments match.
     *
     * @param player executing the command.
     * @param args   arguments.
     * @return Boolean - Whether the arguments and permissions are valid and the command successfully gets executed.
     */
    @Override
    public boolean onPlayerExecuteCommand(Player player, String[] args) {
        if (args.length != 1 || !args[0].equalsIgnoreCase("flip") || !Permission.FLIP.hasPermission(player)) {
            return false;
        }

        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            final Selection selection = this.manager.getSelection(player);
            if (selection == null) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                selection.flip();
                this.manager.addOperation(player, new Operation(OperationType.FLIP));
            }
        });

        return true;
    }

}
