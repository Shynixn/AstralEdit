package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.AstralEditApi;
import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.api.bukkit.business.entity.Selection;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RenderCommand implements PlayerCommand {

    private JavaPlugin plugin;

    /**
     * Creates an instance of RenderCommand which depends on a JavaPlugin
     * @param plugin the JavaPlugin
     */
    public RenderCommand(JavaPlugin plugin) {
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
        if (args.length != 1 || !args[0].equalsIgnoreCase("render") || !Permission.RENDER.hasPermission(player)) {
            return false;
        } else {
            createRenderCommand(player);
            return true;
        }
    }

    /**
     * Creates a new rendered object for the player
     *
     * @param player player
     */
    private void createRenderCommand(final Player player) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Rendering WorldEdit-Selection asynchronously...");
            final Selection selection = AstralEditApi.INSTANCE.render(player);
            if (selection == null) {
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "Failed rendering WE selection!");
                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "Check if you selected an area with Worldedit.");
            } else {
                player.sendMessage(AstralEditPlugin.PREFIX_SUCCESS + "Finished rendering selection.");
            }
        });
    }
}
