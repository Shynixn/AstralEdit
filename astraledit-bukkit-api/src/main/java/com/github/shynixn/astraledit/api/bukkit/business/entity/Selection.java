package com.github.shynixn.astraledit.api.bukkit.business.entity;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

/**
 * Copyright 2017 Shynixn
 * <p>
 * Do not remove this header!
 * <p>
 * Version 1.0
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017
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
public interface Selection extends AutoCloseable {
    /**
     * Returns the amount of selected blocks
     *
     * @return amount
     */
    int getAmountOfSelectedBlocks();

    /**
     * Renders the selection
     */
    void render();

    /**
     * Renders the selection and destroys the blocks
     */
    void renderAndDestroy();

    /**
     * Joins the blocks together if teared apart
     */
    void join();

    /**
     * Tears the blocks apart if joined
     */
    void tearApart();

    /**
     * Returns if the blocks are joined
     *
     * @return joined
     */
    boolean isJoined();

    /**
     * Places the selection at the current location
     * @param callBack callBack
     */
    void placeBlocks(Runnable callBack);

    /**
     * Moves the object to the given location
     *
     * @param location location
     */
    void move(Location location);

    /**
     * Returns the location of the selection
     *
     * @return location
     */
    Location getLocation();

    /**
     * Flips the selection
     */
    void flip();

    /**
     * UpsideDowns the selection
     */
    void upSideDown();

    /**
     * Mirrors the selection
     */
    void mirror();

    /**
     * Checks if the selection is hidden from other player
     *
     * @return hidden
     */
    boolean isHidden();

    /**
     * Shows the given players the selection
     *
     * @param players players
     */
    void show(Player... players);

    /**
     * Hides the selection from the given player
     *
     * @param players players
     */
    void hide(Player... players);

    /**
     * Returns the owner of the selection
     *
     * @return owner
     */
    Player getOwner();

    /**
     * Returns the angle of the single blocks
     *
     * @return angle
     */
    EulerAngle getBlockAngle();

    /**
     * Returns the rotation of the selection
     *
     * @return rotation
     */
    double getRotation();

    /**
     * Rotates the selection
     *
     * @param amount amount
     */
    void rotate(double amount);

    /**
     * Sets the angle of the single blocks
     *
     * @param eulerAngle angle
     */
    void setBlockAngle(EulerAngle eulerAngle);

    /**
     * Sets auto follow enabled
     *
     * @param enabled enabled
     */
    void setAutoFollowEnabled(boolean enabled);

    /**
     * Set auto follow rotate enabled
     *
     * @param enabled enabled
     */
    void setAutoFollowRotateEnabled(boolean enabled);

    /**
     * Returns if auto follow is enabled
     *
     * @return enabled
     */
    boolean isAutoFollowEnabled();

    /**
     * Returns if auto follow rotate is enabled
     *
     * @return enabled
     */
    boolean isAutoFollowRotateEnabled();
}
