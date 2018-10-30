package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.controller.SelectionController;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationImpl;
import com.github.shynixn.astraledit.api.bukkit.business.controller.OperationType;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionHolder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ConvertToBlocksCommand implements PlayerCommand {
    private static final String commandName = "convertToBlocks";
    private final SelectionController controller;
    private final Plugin plugin;

    /**
     * Creates a new instance of ConvertToBlocksCommand with controller and Plugin as dependency.
     *
     * @param controller controller.
     * @param plugin plugin
     */
    public ConvertToBlocksCommand(SelectionController controller, Plugin plugin){
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
        if (args.length == 1 && args[0].equalsIgnoreCase(commandName) && Permission.CONVERT_TO_BLOCKS.hasPermission(player)) {
            this.convertToBlocksCommand(player);
            return true;
        }

        return false;
    }

    /**
     * Converts the blocks to a rendered object
     *
     * @param player player
     */
    private void convertToBlocksCommand(Player player) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (!this.controller.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Converting render ...");
                final OperationImpl operation = new OperationImpl(OperationType.PLACE);
                operation.setOperationData(((SelectionHolder) this.controller.getSelection(player)).getTemporaryStorage());
                this.controller.getSelection(player).placeBlocks(() -> {
                    this.controller.clearSelection(player);
                    this.controller.addOperation(player, operation);
                    player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Finished converting render.");
                });
            }
        });
    }
}
