package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.AstralEditApi;
import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ConvertToRenderCommand implements PlayerCommand {
    private final Plugin plugin;
    private static final String commandName = "convertToRender";

    /**
     * Creates a new instance of ConvertToRenderCommand which depends on a Plugin
     *
     * @param plugin The plugin.
     */
    public ConvertToRenderCommand(Plugin plugin){ this.plugin = plugin; }

    /**
     * Executes the given command if the arguments match.
     *
     * @param player executing the command.
     * @param args   arguments.
     * @return True if this command was executed. False if the arguments do not match.
     */
    @Override
    public boolean onPlayerExecuteCommand(Player player, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase(commandName) && Permission.CONVERT_TO_RENDER.hasPermission(player)){
            this.convertToRenderCommand(player);
            return true;
        }
        return false;
    }
    
    /**
     * Converts a rendered object to blocks
     *
     * @param player player
     */
    private void convertToRenderCommand(final Player player) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> {
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

}
