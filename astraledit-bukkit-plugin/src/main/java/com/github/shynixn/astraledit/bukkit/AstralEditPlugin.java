package com.github.shynixn.astraledit.bukkit;

import com.github.shynixn.astraledit.api.bukkit.AstralEditApi;
import com.github.shynixn.astraledit.api.bukkit.business.controller.SelectionController;
import com.github.shynixn.astraledit.api.business.service.DependencyService;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;
import com.github.shynixn.astraledit.bukkit.logic.business.nms.VersionSupport;
import com.github.shynixn.astraledit.bukkit.logic.business.service.DependencyServiceImpl;
import com.github.shynixn.astraledit.bukkit.logic.business.service.DependencyWorldEditServiceImpl;
import com.github.shynixn.astraledit.bukkit.logic.business.service.UpdateServiceImpl;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class AstralEditPlugin extends JavaPlugin {
    public static final long SPIGOT_RESOURCEID = 11409;
    public static final String PLUGIN_NAME = "AstralEdit";
    public static final String PREFIX_CONSOLE = ChatColor.LIGHT_PURPLE + "[AstralEdit] ";
    public static final String PREFIX = ChatColor.DARK_RED + "" + ChatColor.BOLD + '[' + ChatColor.RED + "" + ChatColor.BOLD + ChatColor.ITALIC + "AE" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "] " + ChatColor.RED;
    public static final String PREFIX_SUCCESS = PREFIX + ChatColor.GREEN;
    public static final String PREFIX_ERROR = PREFIX + ChatColor.RED;
    private static Logger logger;

    /**
     * Enables the plugin
     */
    @Override
    public void onEnable() {
        logger = this.getLogger();

        Bukkit.getServer().getConsoleSender().sendMessage(PREFIX_CONSOLE + ChatColor.GREEN + "Loading AstralEdit ...");

        final DependencyService dependencyService = new DependencyServiceImpl(this);
        final boolean requiredDependenciesInstalled = dependencyService.checkForInstalledDependencies();

        if (!VersionSupport.isServerVersionSupported(PLUGIN_NAME, PREFIX_CONSOLE) || !requiredDependenciesInstalled) {
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            try {
                this.saveDefaultConfig();
                if (this.getConfig().getBoolean("metrics")) {
                    new Metrics(this);
                }
                this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
                        new UpdateServiceImpl(this).checkForUpdates().thenAcceptAsync(result -> {
                            if (!result) {
                                AstralEditPlugin.logger().log(Level.WARNING, "Failed to check for updates.");
                            }
                        });
                });

                final Method method = AstralEditApi.class.getDeclaredMethod("initialize", Plugin.class, SelectionController.class);
                method.setAccessible(true);
                method.invoke(AstralEditApi.INSTANCE, this, new SelectionManager(new DependencyWorldEditServiceImpl(this), this));
                Bukkit.getServer().getConsoleSender().sendMessage(PREFIX_CONSOLE + ChatColor.GREEN + "Enabled AstralEdit " + this.getDescription().getVersion() + " by Shynixn");
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                AstralEditPlugin.logger().log(Level.WARNING, "Failed to initialize plugin.", e);
            }
        }
    }

    /**
     * Disables the plugin
     */
    @Override
    public void onDisable() {
        try {
            final Method method = AstralEditApi.class.getDeclaredMethod("shutdown");
            method.setAccessible(true);
            method.invoke(AstralEditApi.INSTANCE);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            AstralEditPlugin.logger().log(Level.WARNING, "Failed to initialize plugin.", e);
        }
    }

    /**
     * Returns the logger of the AstralEdit plugin
     *
     * @return logger
     */
    public static Logger logger() {
        return logger;
    }
}
