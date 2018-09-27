package com.github.shynixn.astraledit.bukkit;

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
public enum Permission {

    RENDER("astraledit.commands.render"),
    JOIN("astraledit.commands.join"),
    TEAR("astraledit.commands.tear"),
    PLACE("astraledit.commands.place"),
    CLEAR("astraledit.commands.clear"),
    MOVE_COORDINATE("astraledit.commands.movecoordinate"),
    MOVE_PLAYER("astraledit.commands.moveplayer"),
    MIRROR("astraledit.commands.mirror"),
    FLIP("astraledit.commands.flip"),
    UPSIDEDOWN("astraledit.commands.upsidedown"),
    ANGLES("astraledit.commands.angles"),
    ROTATE("astraledit.commands.rotate"),
    TELEPORT_PLAYER("astraledit.commands.teleportplayer"),
    CONVERT_TO_BLOCKS("astraledit.commands.converttoblocks"),
    CONVERT_TO_RENDER("astraledit.commands.converttorender"),
    AUTO_FOLLOW("astraledit.commands.autofollow"),
    AUTO_ROTATE("astraledit.commands.autorotate"),
    UNDO("astraledit.commands.undo"),
    HIDE_OTHER("astraledit.commands.hideother"),
    SHOW_OTHER("astraledit.commands.showother"),;

    private final String text;

    /**
     * Initializes a new permission
     *
     * @param text text
     */
    Permission(String text) {
        this.text = text;
    }

    /**
     * Returns if the given player has permissions
     *
     * @param player player
     * @return has
     */
    public boolean hasPermission(Player player) {
        return player.hasPermission(this.text);
    }

    /**
     * Returns the text of the permission
     *
     * @return text
     */
    public String getText() {
        return this.text;
    }
}
