package com.github.shynixn.astraledit.api.bukkit;

import com.github.shynixn.astraledit.api.bukkit.business.controller.SelectionController;
import com.github.shynixn.astraledit.api.bukkit.business.entity.Selection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public final class AstralEditApi {
    public static final AstralEditApi INSTANCE = new AstralEditApi();

    private int maxAmountOfBlocksPerPerson = 10000;
    private SelectionController manager;
    private Plugin plugin;

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
    private void initialize(Plugin plugin, SelectionController selectionController) {
        this.manager = selectionController;
        this.plugin = plugin;
        this.maxAmountOfBlocksPerPerson = plugin.getConfig().getInt("general.max-selected-blocks-amount");
    }

    /**
     * Shutdowns the api
     */
    private void shutdown() {
        try {
            if (this.manager != null) {
                this.manager.close();
                this.manager = null;
            }
        } catch (final Exception e) {
            this.plugin.getLogger().log(Level.WARNING, "Failed to clean up.", e);
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
    private Selection render(Player player, Location corner1, Location corner2, boolean destroy) {
        if (corner1 == null || corner2 == null)
            throw new IllegalArgumentException("Corner1 or Corner2 cannot be null!");
        final Selection selection = this.manager.create(player, corner1, corner2);
        if (selection.getAmountOfSelectedBlocks() > this.maxAmountOfBlocksPerPerson)
            return null;
        if (destroy)
            selection.renderAndDestroy();
        else
            selection.render();
        this.manager.addSelection(player, selection);
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
    public Selection render(Player player, Location corner1, Location corner2) {
        return this.render(player, corner1, corner2, false);
    }

    /**
     * Generates a visualized object between the two corners and destroys the source
     *
     * @param player  player
     * @param corner1 corner1
     * @param corner2 corner2
     * @return Rendered object
     */
    public Selection renderAndDestroy(Player player, Location corner1, Location corner2) {
        return this.render(player, corner1, corner2, true);
    }

    /**
     * Generates a visualized object between the two corners
     *
     * @param player player
     * @return Rendered object
     */
    public Selection render(Player player) {
        if (!this.manager.getWorldEditController().getLeftClickLocation(player).isPresent() || !this.manager.getWorldEditController().getRightClickLocation(player).isPresent())
            return null;
        return this.render(player, this.manager.getWorldEditController().<Location, Player>getLeftClickLocation(player).get(), this.manager.getWorldEditController().<Location, Player>getRightClickLocation(player).get(), false);
    }

    /**
     * Generates a visualized object between the two corners and destroys the source
     *
     * @param player player
     * @return Rendered object
     */
    public Selection renderAndDestroy(Player player) {
        if (!this.manager.getWorldEditController().getLeftClickLocation(player).isPresent() || !this.manager.getWorldEditController().getRightClickLocation(player).isPresent())
            return null;
        return this.render(player, this.manager.getWorldEditController().<Location, Player>getLeftClickLocation(player).get(), this.manager.getWorldEditController().<Location, Player>getRightClickLocation(player).get(), true);
    }

    /**
     * Returns the rendered Object
     *
     * @param player player
     * @return Rendered object
     */
    public Selection getRenderedObject(Player player) {
        return this.manager.getSelection(player);
    }

    /**
     * Clears the rendered Object
     *
     * @param player player
     */
    public void clearRenderedObject(Player player) {
        this.manager.clearSelection(player);
    }
}
