package com.github.shynixn.astraledit.bukkit.logic.business.commandexecutor;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;
import com.github.shynixn.astraledit.bukkit.logic.business.command.*;
import com.github.shynixn.astraledit.bukkit.logic.lib.SimpleCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SelectionCommandExecutor extends SimpleCommandExecutor.Registered {
    private final List<PlayerCommand> commands = new ArrayList<>();

    /**
     * Initializes the commandExecutor.
     *
     * @param manager manager
     */
    public SelectionCommandExecutor(SelectionManager manager) {
        super("awe", JavaPlugin.getPlugin(AstralEditPlugin.class));

        this.commands.add(new RenderCommand(this.plugin));
        this.commands.add(new AutoFollowCommand(manager));
        this.commands.add(new AutoRotateCommand(manager));
        this.commands.add(new RotateCommand(manager, this.plugin));
        this.commands.add(new UpsidedownCommand(manager, this.plugin));
        this.commands.add(new MoveCommand(manager, this.plugin));
        this.commands.add(new AnglesCommand(manager, this.plugin));
        this.commands.add(new JoinCommand(manager, this.plugin));
        this.commands.add(new TearCommand(manager, this.plugin));
        this.commands.add(new UndoCommand(manager, this.plugin));
        this.commands.add(new HideCommand(manager, this.plugin));
        this.commands.add(new ShowCommand(manager, this.plugin));
        this.commands.add(new ClearCommand(this.plugin, manager));
        this.commands.add(new MirrorCommand(this.plugin, manager));
        this.commands.add(new PlaceCommand(this.plugin, manager));
        this.commands.add(new FlipCommand(this.plugin, manager));
        this.commands.add(new TeleportCommand(manager));
        this.commands.add(new ConvertToBlocksCommand(manager, this.plugin));
        this.commands.add(new ConvertToRenderCommand(this.plugin));
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

        if (args.length == 1 && args[0].equalsIgnoreCase("3")) {
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
}
