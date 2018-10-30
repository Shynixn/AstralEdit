package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.controller.SelectionController;
import com.github.shynixn.astraledit.api.bukkit.business.entity.Selection;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationImpl;
import com.github.shynixn.astraledit.api.bukkit.business.controller.OperationType;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionHolder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceCommand implements PlayerCommand {
    private final Plugin plugin;
    private final SelectionController controller;

    /**
     * Creates a new instance of the PlaceCommand which depends on a Plugin and a SelectionController.
     *
     * @param plugin the Plugin
     * @param controller the SelectionController
     */
    public PlaceCommand(Plugin plugin, SelectionController controller) {
        this.plugin = plugin;
        this.controller = controller;
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
            final Selection selection = this.controller.getSelection(player);
            if (selection == null) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Placing render ...");
                final OperationImpl operation = new OperationImpl(OperationType.PLACE);
                operation.setOperationData(((SelectionHolder) selection).getTemporaryStorage());
                selection.placeBlocks(() -> {
                    this.controller.addOperation(player, operation);
                    player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Finished placing render.");
                });
            }
        });

        return true;
    }
}
