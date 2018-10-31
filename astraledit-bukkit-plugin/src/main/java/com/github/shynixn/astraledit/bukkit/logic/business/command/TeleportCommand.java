package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.controller.SelectionController;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import org.bukkit.entity.Player;

public class TeleportCommand implements PlayerCommand {
    private static final String commandName = "teleport";
    private final SelectionController controller;

    /**
     * Creates an instance of TearCommand which depends on
     * a SelectionController
     *
     * @param controller SelectionController
     */
    public TeleportCommand(SelectionController controller){
        this.controller = controller;
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
        if (args.length == 1 && args[0].equalsIgnoreCase(commandName) && Permission.TELEPORT_PLAYER.hasPermission(player)){
            this.teleportPlayerToRenderCommand(player);
            return true;
        }
        return false;
    }

    /**
     * Teleports the given player to the selection
     *
     * @param player player
     */
    private void teleportPlayerToRenderCommand(Player player) {
        if (!this.controller.hasSelection(player)) {
            player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
        } else {
            player.teleport(this.controller.getSelection(player).getLocation());
        }
    }
}
