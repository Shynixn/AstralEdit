package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.controller.SelectionController;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationImpl;
import com.github.shynixn.astraledit.api.bukkit.business.controller.OperationType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

import static com.github.shynixn.astraledit.bukkit.logic.lib.Utils.tryParseDouble;


public class RotateCommand implements PlayerCommand {

    private static final String commandName = "rotate";

    private final Plugin plugin;

    private final SelectionController controller;

    /**
     * Creates an instance of {@link RotateCommand} which depends on
     * a {@link SelectionController} and a {@link Plugin}
     *
     * @param controller Selection Manager
     * @param plugin
     */
    public RotateCommand(SelectionController controller, Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.controller = Objects.requireNonNull(controller);
    }

    /**
     * Rotates the selection for the given angle
     *
     * @param player executing the command.
     * @param args   arguments.
     * @return True if this command was executed. False if the arguments do not match
     */
    @Override
    public boolean onPlayerExecuteCommand(Player player, String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("rotate") && tryParseDouble(args[1]) && Permission.ROTATE.hasPermission(player)) {
            this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
                if (!this.controller.hasSelection(player)) {
                    player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
                } else {
                    final OperationImpl operation = new OperationImpl(OperationType.ROTATE);
                    operation.setOperationData(this.controller.getSelection(player).getRotation());
                    this.controller.getSelection(player).rotate(Double.parseDouble(args[1]));
                    this.controller.addOperation(player, operation);
                }
            });
            return true;
        }
        return false;
    }
}