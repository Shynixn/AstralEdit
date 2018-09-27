package com.github.shynixn.astraledit.bukkit.logic.business.nms;

import com.github.shynixn.astraledit.api.bukkit.business.entity.PacketArmorstand;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.logic.lib.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;

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
     * @param watchers watchers
     * @return packetArmorstand
     */
    public static PacketArmorstand createPacketArmorstand(Player player, Location location, int id, byte data, Set<Player> watchers) {
        try {
            return (PacketArmorstand) ReflectionUtils
                    .invokeConstructor(ReflectionUtils.invokeClass("com.github.shynixn.astraledit.business.bukkit.nms.VERSION.DisplayArmorstand"
                                    .replace("VERSION", VersionSupport.getServerVersion().getVersionText()))
                            , new Class[]{Player.class, Location.class, int.class, byte.class, Set.class}
                            , new Object[]{player, location, id, data, watchers});
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException | ClassNotFoundException e) {
            AstralEditPlugin.logger().log(Level.WARNING, "Failed to create packetArmorstand.", e);
            throw new RuntimeException("Failed to create packetArmorstand.");
        }
    }
}
