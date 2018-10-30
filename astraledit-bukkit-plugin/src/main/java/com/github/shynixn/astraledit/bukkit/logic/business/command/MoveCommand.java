package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationImpl;
import com.github.shynixn.astraledit.api.bukkit.business.controller.OperationType;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static com.github.shynixn.astraledit.bukkit.logic.lib.Utils.tryParseDouble;

public class MoveCommand implements PlayerCommand {
    private final SelectionManager selectionManager;
    private final Plugin plugin;

    /**
     * Creates a new instance of MoveCommand with SelectionManager as dependency.
     *
     * @param selectionManager selectionManager.
     */
    public MoveCommand(SelectionManager selectionManager, Plugin plugin) {
        this.selectionManager = selectionManager;
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
        if (args.length == 1 && args[0].equalsIgnoreCase("move") && Permission.MOVE_PLAYER.hasPermission(player)) {
            this.moveRenderCommand(player, player.getLocation().clone().add(0, -2, 0));
            return true;
        } else if (args.length == 4 && args[0].equalsIgnoreCase("move") && tryParseDouble(args[1]) && tryParseDouble(args[2]) && tryParseDouble(args[3]) && Permission.MOVE_COORDINATE.hasPermission(player)) {
            this.moveRenderCommand(player, new Location(player.getWorld(), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
            return true;
        }

        return false;
    }

    /**
     * Moves the rendered Object to the given coordinates.
     *
     * @param player   player
     * @param location location.
     */
    private void moveRenderCommand(Player player, Location location) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
            if (!this.selectionManager.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                final OperationImpl operation = new OperationImpl(OperationType.MOVE);
                operation.setOperationData(this.selectionManager.getSelection(player).getLocation().clone());
                this.selectionManager.getSelection(player).move(location);
                this.selectionManager.addOperation(player, operation);
            }
        });
    }
}
