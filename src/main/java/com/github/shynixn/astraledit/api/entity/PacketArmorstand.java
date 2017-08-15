package com.github.shynixn.astraledit.api.entity;

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
public interface PacketArmorstand extends AutoCloseable {

    /**
     * Spawns the armorstand
     */
    void spawn();

    /**
     * Teleports the armorstand to the given location
     *
     * @param location location
     */
    void teleport(Location location);

    /**
     * Removes the armorstand
     */
    void remove();

    /**
     * Returns the location of the armorstand
     *
     * @return location
     */
    Location getLocation();

    /**
     * Sets the pose of
     *
     * @param angle angle
     */
    void setHeadPose(EulerAngle angle);

    /**
     * Returns the pose of the head
     *
     * @return angle
     */
    EulerAngle getHeadPose();

    /**
     * Returns the stored block id
     *
     * @return id
     */
    int getStoredBlockId();

    /**
     * Sets the stored block id
     *
     * @param id id
     */
    void setStoreBlockId(int id);

    /**
     * Returns the stored block data
     *
     * @return data
     */
    byte getStoredBlockData();

    /**
     * Sets the stored block data
     *
     * @param data data
     */
    void setStoredBlockData(byte data);
}
