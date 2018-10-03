package com.github.shynixn.astraledit.bukkit.logic.business;

import com.github.shynixn.astraledit.api.bukkit.AstralEditApi;
import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.entity.Selection;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.command.*;
import com.github.shynixn.astraledit.bukkit.logic.lib.SimpleCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;

public class SelectionCommandExecutor extends SimpleCommandExecutor.Registered {
    private final SelectionManager manager;
    private final List<PlayerCommand> commands = new ArrayList<>();

    /**
     * Initializes the commandExecutor
     *
     * @param manager manager
     */
    SelectionCommandExecutor(SelectionManager manager) {
        super("awe", JavaPlugin.getPlugin(AstralEditPlugin.class));
        this.manager = manager;

        this.commands.add(new RenderCommand(plugin));
        this.commands.add(new AutoRotateCommand(manager));
        this.commands.add(new ClearCommand(plugin, manager));
        this.commands.add(new MirrorCommand(plugin, manager));
        this.commands.add(new PlaceCommand(plugin, manager));
        this.commands.add(new TearCommand(manager, plugin));
        this.commands.add(new FlipCommand(plugin, manager));
        this.commands.add(new HideCommand(manager, plugin));
        this.commands.add(new ShowCommand(manager, plugin));
    }

    /**
     * Can be overwritten to listen to player executed commands
     *
     * @param player player
     * @param args   args
     */
    @Override
    public void onPlayerExecuteCommand(Player player, String[] args) {
        for (final PlayerCommand command : this.commands) {
            final boolean executed = command.onPlayerExecuteCommand(player, args);

            if (executed) {
                return;
            }
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("join") && Permission.JOIN.hasPermission(player))
            this.combineCommand(player);
        else if (args.length == 1 && args[0].equalsIgnoreCase("move") && Permission.MOVE_PLAYER.hasPermission(player))
            this.moveRenderToPlayer(player);
        else if (args.length == 4 && args[0].equalsIgnoreCase("move") && tryParseDouble(args[1]) && tryParseDouble(args[2]) && tryParseDouble(args[3]) && Permission.MOVE_COORDINATE.hasPermission(player))
            this.moveRenderCommand(player, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        else if (args.length == 1 && args[0].equalsIgnoreCase("upsidedown") && Permission.UPSIDEDOWN.hasPermission(player))
            this.upSideDownCommand(player);
        else if (args.length == 1 && args[0].equalsIgnoreCase("undo") && Permission.UNDO.hasPermission(player))
            this.undoCommand(player);
        else if (args.length == 4 && args[0].equalsIgnoreCase("angles") && tryParseDouble(args[1]) && tryParseDouble(args[2]) && tryParseDouble(args[3]) && Permission.ANGLES.hasPermission(player))
            this.setAnglesCommand(player, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
        else if (args.length == 2 && args[0].equalsIgnoreCase("rotate") && tryParseDouble(args[1]) && Permission.ROTATE.hasPermission(player))
            this.rotateRenderCommand(player, Double.parseDouble(args[1]));
        else if (args.length == 1 && args[0].equalsIgnoreCase("convertToBlocks") && Permission.CONVERT_TO_BLOCKS.hasPermission(player))
            this.convertToBlocksCommand(player);
        else if (args.length == 1 && args[0].equalsIgnoreCase("convertToRender") && Permission.CONVERT_TO_RENDER.hasPermission(player))
            this.convertToRenderCommand(player);
        else if (args.length == 1 && args[0].equalsIgnoreCase("teleport") && Permission.TELEPORT_PLAYER.hasPermission(player))
            this.teleportPlayerToRenderCommand(player);
        else if (args.length == 1 && args[0].equalsIgnoreCase("auto-follow") && Permission.AUTO_FOLLOW.hasPermission(player))
            this.toggleAutoFollowCommand(player);
        else if (args.length == 1 && args[0].equalsIgnoreCase("3")) {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "                      AstralEdit                    ");
            player.sendMessage("");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe auto-follow - Toggles auto follow of the selection");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe auto-rotate - Toggles auto rotate of the selection");
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "                           ┌3/3┐                            ");
            player.sendMessage("");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("2")) {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "                      AstralEdit                    ");
            player.sendMessage("");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe undo - Undos operation");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe mirror - Mirrors the render");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe flip - Flips the render");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe upsideDown - Upside downs the render");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe rotate <amount> - Rotates around the angle");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe angles <x> <y> <z> - Set selection angles");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe hide - Hides your render from other players");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe show - Shows your render to other players");
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "                           ┌2/3┐                            ");
            player.sendMessage("");
        } else {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "                      AstralEdit                    ");
            player.sendMessage("");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe render - Renders WE selection");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe join - Joins the blocks");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe tear - Tears the blocks apart");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe place - Places render");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe clear - Clears render");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe move - Move selection to player");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe move <x> <y> <z> - Move selection");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe convertToBlocks - Converts it to blocks");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe convertToRender - Converts WE selection");
            player.sendMessage(AstralEditPlugin.PREFIX + "/awe teleport - Teleports player to it");
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "                           ┌1/3┐                            ");
            player.sendMessage("");
        }
    }

    /**
     * Toggles autoFollow
     *
     * @param player player
     */
    private void toggleAutoFollowCommand(Player player) {
        if (!this.manager.hasSelection(player)) {
            player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
        } else {
            final Selection selection = this.manager.getSelection(player);
            selection.setAutoFollowEnabled(!selection.isAutoFollowEnabled());
            if (selection.isAutoFollowEnabled()) {
                player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Enabled auto-follow.");
            } else {
                player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Disabled auto-follow.");
            }
        }
    }
  
    //SYNC

    /**
     * Teleports the given player to the selection
     *
     * @param player player
     */
    private void teleportPlayerToRenderCommand(Player player) {
        if (!this.manager.hasSelection(player)) {
            player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
        } else {
            player.teleport(this.manager.getSelection(player).getLocation());
        }
    }

    //ASYNC

    /**
     * Converts a rendered object to blocks
     *
     * @param player player
     */
    private void convertToRenderCommand(final Player player) {
        this.runAsyncTask(() -> {
            try {
                player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Removing blocks and rendering selection asynchronously...");
                this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
                    AstralEditApi.INSTANCE.renderAndDestroy(player);
                    player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Finished converting selection.");
                });
            } catch (final Exception e) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "Failed converting WE selection!");
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "Check if you selected an area with Worldedit.");
            }
        });
    }

    /**
     * Converts the blocks to a rendered object
     *
     * @param player player
     */
    private void convertToBlocksCommand(Player player) {
        this.runAsyncTask(() -> {
            if (!this.manager.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Converting render ...");
                final Operation operation = new Operation(OperationType.PLACE);
                operation.setOperationData(((SelectionHolder) this.manager.getSelection(player)).getTemporaryStorage());
                this.manager.getSelection(player).placeBlocks(() -> {
                    this.manager.clearSelection(player);
                    this.manager.addOperation(player, operation);
                    player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Finished converting render.");
                });
            }
        });
    }

    /**
     * Rotates the blocks for the given angle
     *
     * @param player player
     * @param x      x
     * @param y      y
     * @param z      z
     */
    private void setAnglesCommand(Player player, double x, double y, double z) {
        this.runAsyncTask(() -> {
            if (!this.manager.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                final Operation operation = new Operation(OperationType.ANGLES);
                operation.setOperationData(this.manager.getSelection(player).getBlockAngle());
                this.manager.getSelection(player).setBlockAngle(new EulerAngle(x, y, z));
                this.manager.addOperation(player, operation);
            }
        });
    }

    /**
     * Rotates the selection for the given angle
     *
     * @param player player
     * @param amount amount
     */
    private void rotateRenderCommand(Player player, double amount) {
        this.runAsyncTask(() -> {
            if (!this.manager.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                final Operation operation = new Operation(OperationType.ROTATE);
                operation.setOperationData(this.manager.getSelection(player).getRotation());
                this.manager.getSelection(player).rotate(amount);
                this.manager.addOperation(player, operation);
            }
        });
    }

    /**
     * Undos the last operation
     *
     * @param player player
     */
    private void undoCommand(Player player) {
        this.runAsyncTask(() -> {
            player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Undoing operation ...");
            if (!this.manager.undoOperation(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You cannot undo the last operation.");
            } else {
                player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Finished undoing the last operation.");
            }
        });
    }

    /**
     * UpsideDowns the given selection
     *
     * @param player player
     */
    private void upSideDownCommand(Player player) {
        this.runAsyncTask(() -> {
            if (!this.manager.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                this.manager.getSelection(player).upSideDown();
                this.manager.addOperation(player, new Operation(OperationType.UPSIDEDOWN));
            }
        });
    }

    /**
     * Moves the rendered Object to the given coordinates
     *
     * @param player player
     * @param x      x
     * @param y      y
     * @param z      z
     */
    private void moveRenderCommand(Player player, double x, double y, double z) {
        this.runAsyncTask(() -> {
            if (!this.manager.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                final Operation operation = new Operation(OperationType.MOVE);
                operation.setOperationData(this.manager.getSelection(player).getLocation().clone());
                this.manager.getSelection(player).move(this.manager.getSelection(player).getLocation().add(x, y, z));
                this.manager.addOperation(player, operation);
            }
        });
    }

    /**
     * Moves the rendered Object to the player
     *
     * @param player player
     */
    private void moveRenderToPlayer(Player player) {
        this.runAsyncTask(() -> {
            if (!this.manager.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else {
                final Operation operation = new Operation(OperationType.MOVE);
                operation.setOperationData(this.manager.getSelection(player).getLocation().clone());
                this.manager.getSelection(player).move(player.getLocation().add(0, -2, 0));
                this.manager.addOperation(player, operation);
            }
        });
    }

    /**
     * Joins the rendered Object
     *
     * @param player player
     */
    private void combineCommand(Player player) {
        this.runAsyncTask(() -> {
            if (!this.manager.hasSelection(player)) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
            } else if (!SelectionCommandExecutor.this.manager.getSelection(player).isJoined()) {
                this.manager.getSelection(player).join();
                this.manager.addOperation(player, new Operation(OperationType.COMBINE));
            }
        });
    }

    /**
     * Runs task asynchronously
     *
     * @param runnable runnable
     */
    private void runAsyncTask(Runnable runnable) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    /**
     * Checks if the string can be parsed to double
     *
     * @param value value
     * @return success
     */
    private static boolean tryParseDouble(String value) {
        try {
            Double.parseDouble(value);
        } catch (final NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
