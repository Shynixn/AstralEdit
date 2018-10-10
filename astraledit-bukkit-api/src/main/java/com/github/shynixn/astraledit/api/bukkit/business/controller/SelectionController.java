package com.github.shynixn.astraledit.api.bukkit.business.controller;

import com.github.shynixn.astraledit.api.bukkit.business.entity.Operation;
import com.github.shynixn.astraledit.api.bukkit.business.entity.Selection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Shynixn 2018.
 * <p>
 * Version 1.2
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2018 by Shynixn
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public interface SelectionController extends AutoCloseable {
    /**
     * Creates a new selection for the given player, corner1, corner2.
     *
     * @param player  player
     * @param corner1 corner1
     * @param corner2 corner2
     * @return selection
     */
    Selection create(Player player, Location corner1, Location corner2);

    /**
     * Adds the selection to the manager to be managed
     *
     * @param player    player
     * @param selection selection
     */
    void addSelection(Player player, Selection selection);

    /**
     * Checks if the player has got a selection.
     *
     * @param player player
     * @return selection
     */
    boolean hasSelection(Player player);

    /**
     * Returns the selection of a player
     *
     * @param player player
     * @return selection
     */
    Selection getSelection(Player player);

    /**
     * Clears the selection of the player
     *
     * @param player player
     */
    void clearSelection(Player player);

    /**
     * Gets the worldedit controller.
     *
     * @return controll
     */
    WorldEditController getWorldEditController();

    void addOperation(Player player, Operation operation);
    boolean undoOperation(Player player);
}
