package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.controller.SelectionController;
import com.github.shynixn.astraledit.api.bukkit.business.entity.Selection;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationImpl;
import com.github.shynixn.astraledit.api.bukkit.business.controller.OperationType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FlipCommand implements PlayerCommand {
    private final Plugin plugin;
    private final SelectionController controller;

    /**
     * The Command flips the current selection depending on the Plugin and SelectionController.
     *
     * @param plugin the Plugin
     * @param controller the current SelectionController
     */
    public FlipCommand(Plugin plugin, SelectionController controller) {
        this.plugin = plugin;
        this.controller = controller;
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
            final Selection selection = this.controller.getSelection(player);
            if (selection == null) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                selection.flip();
                this.controller.addOperation(player, new OperationImpl(OperationType.FLIP));
            }
        });

        return true;
    }

}
