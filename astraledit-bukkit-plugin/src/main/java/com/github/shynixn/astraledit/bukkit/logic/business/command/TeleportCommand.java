package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;

public class TeleportCommand implements PlayerCommand {
    private static final String commandName = "teleport";
    private final SelectionManager selectionManager;

    /**
     * Creates an instance of TearCommand which depends on
     * a SelectionManager
     *
     * @param selectionManager SelectionManager
     */
    public TeleportCommand(SelectionManager selectionManager){
        this.selectionManager = selectionManager;
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
        if (!this.selectionManager.hasSelection(player)) {
            player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
        } else {
            player.teleport(this.selectionManager.getSelection(player).getLocation());
        }
    }
}
