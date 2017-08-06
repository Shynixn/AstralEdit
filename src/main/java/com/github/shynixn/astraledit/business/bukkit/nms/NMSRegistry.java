package com.github.shynixn.astraledit.business.bukkit.nms;

import com.github.shynixn.astraledit.api.entity.PacketArmorstand;
import com.github.shynixn.astraledit.business.bukkit.nms.v1_12_R1.DisplayArmorstand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
public class NMSRegistry {
    public static final String WATER_HEAD = "emack0714";
    public static final String LAVA_HEAD = "Spe";
    public static final String NOT_FOUND = "X_AgusRodri_X";

    /**
     * Creates a new packetArmorstand
     *
     * @param player   player
     * @param location location
     * @param id       id
     * @param data     data
     * @return packetArmorstand
     */
    public static PacketArmorstand createPacketArmorstand(Player player, Location location, int id, byte data) {
        return new DisplayArmorstand(player, location, id, data);
    }
}
