package com.github.shynixn.astraledit.api;

import com.github.shynixn.astraledit.api.entity.Selection;
import com.github.shynixn.astraledit.business.bukkit.dependencies.worldedit.WorldEditConnection;
import com.github.shynixn.astraledit.business.logic.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public final class AstralEditApi {

    private static int maxAmountOfBlocksPerPerson = 10000;
    private static SelectionManager manager;

    /**
     * Initialize
     */
    private AstralEditApi() {
        super();
    }

    /**
     * Initializes the api
     *
     * @param plugin plugin
     */
    private static void initialize(Plugin plugin) {
        AstralEditApi.manager = new SelectionManager(plugin);
        maxAmountOfBlocksPerPerson = plugin.getConfig().getInt("general.max-selected-blocks-amount");
    }

    /**
     * Shutdowns the api
     */
    private static void shutdown() {
        try {
            if (manager != null) {
                manager.close();
                manager = null;
            }
        } catch (final Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to clean up.", e);
        }
    }

    /**
     * Renders the object
     *
     * @param player  player
     * @param corner1 corner1
     * @param corner2 corner2
     * @param destroy destroy
     * @return selection
     */
    private static Selection render(Player player, Location corner1, Location corner2, boolean destroy) {
        if (corner1 == null || corner2 == null)
            throw new IllegalArgumentException("Corner1 or Corner2 cannot be null!");
        final Selection selection = manager.create(player, corner1, corner2);
        if (selection.getAmountOfSelectedBlocks() > maxAmountOfBlocksPerPerson)
            return null;
        if (destroy)
            selection.renderAndDestroy();
        else
            selection.render();
        manager.addSelection(player, selection);
        return selection;
    }

    /**
     * Generates a visualized object between the two corners
     *
     * @param player  player
     * @param corner1 corner 1
     * @param corner2 corner 2
     * @return Rendered object
     */
    public static Selection render(Player player, Location corner1, Location corner2) {
        return render(player, corner1, corner2, false);
    }

    /**
     * Generates a visualized object between the two corners and destroys the source
     *
     * @param player  player
     * @param corner1 corner1
     * @param corner2 corner2
     * @return Rendered object
     */
    public static Selection renderAndDestroy(Player player, Location corner1, Location corner2) {
        return render(player, corner1, corner2, true);
    }

    /**
     * Generates a visualized object between the two corners
     *
     * @param player player
     * @return Rendered object
     */
    public static Selection render(Player player) {
        if (!WorldEditConnection.hasSelections(player))
            return null;
        return render(player, WorldEditConnection.getLeftSelection(player), WorldEditConnection.getRightSelection(player), false);
    }

    /**
     * Generates a visualized object between the two corners and destroys the source
     *
     * @param player player
     * @return Rendered object
     */
    public static Selection renderAndDestroy(Player player) {
        if (!WorldEditConnection.hasSelections(player))
            return null;
        return render(player, WorldEditConnection.getLeftSelection(player), WorldEditConnection.getRightSelection(player), true);
    }

    /**
     * Returns the rendered Object
     *
     * @param player player
     * @return Rendered object
     */
    public static Selection getRenderedObject(Player player) {
        return manager.getSelection(player);
    }

    /**
     * Clears the rendered Object
     *
     * @param player player
     */
    public static void clearRenderedObject(Player player) {
        manager.clearSelection(player);
    }
}
