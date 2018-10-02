package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.entity.Selection;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.Operation;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationType;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionHolder;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceCommand implements PlayerCommand {
    private final Plugin plugin;
    private final SelectionManager manager;

    /**
     * Creates a new instance of the PlaceCommand which depends on a Plugin and a SelectionManager.
     *
     * @param plugin the Plugin
     * @param manager the SelectionManager
     */
    public PlaceCommand(Plugin plugin, SelectionManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    /**
     * Executes the PlaceCommand, if the arguments match and the Player has a valid ({@link Permission#PLACE}) Permission.
     *
     * @param player executing the command.
     * @param args   arguments.
     * @return True if this command was executed. False if the arguments do not match.
     */
    @Override
    public boolean onPlayerExecuteCommand(Player player, String[] args) {
        if (args.length != 1 || !args[0].equalsIgnoreCase("place") || !Permission.PLACE.hasPermission(player)) {
            return false;
        }

        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            final Selection selection = this.manager.getSelection(player);
            if (selection == null) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Placing render ...");
                final Operation operation = new Operation(OperationType.PLACE);
                operation.setOperationData(((SelectionHolder) selection).getTemporaryStorage());
                selection.placeBlocks(() -> {
                    this.manager.addOperation(player, operation);
                    player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Finished placing render.");
                });
            }
        });

        return true;
    }

}
