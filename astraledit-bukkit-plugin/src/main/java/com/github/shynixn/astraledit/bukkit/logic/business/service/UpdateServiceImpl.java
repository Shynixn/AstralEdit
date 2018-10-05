package com.github.shynixn.astraledit.bukkit.logic.business.service;

import com.github.shynixn.astraledit.api.bukkit.business.entity.service.UpdateService;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class UpdateServiceImpl implements UpdateService {

    private static final long SPIGOT_RESOURCEID = 11409;
    private static final String BASE_URL = "https://api.spigotmc.org/legacy/update.php?resource=";
    private final Plugin plugin;

    /**
     * Created a new instance of UpdateServiceImpl
     *
     * @param plugin for running the check async and checking the version
     */
    public UpdateServiceImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Checks if the plugin is up-to-date and notifies the user in console
     * if they are out-of-date or notifies the user to check for updates
     * if they are using a snapshot version
     *
     * @return True if the check completed successfully
     */
    @Override
    public CompletableFuture<Boolean> checkForUpdates() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String prefix = AstralEditPlugin.PREFIX_CONSOLE;
                String pluginName = AstralEditPlugin.PLUGIN_NAME;
                String version = plugin.getDescription().getVersion();
                boolean isUpToDate = version.equals(getLatestVersion());


                if (!isUpToDate) {
                    if (version.endsWith("SNAPSHOT")) {
                        Bukkit.getServer().getConsoleSender()
                              .sendMessage(prefix + ChatColor.YELLOW + "================================================"
                                           + prefix + ChatColor.YELLOW + "You are using a snapshot of " + pluginName
                                           + prefix + ChatColor.YELLOW + "Please check regularly if there is a new version"
                                           + prefix + ChatColor.YELLOW + "================================================");
                    } else {
                        Bukkit.getServer().getConsoleSender()
                              .sendMessage(prefix + ChatColor.YELLOW + "================================================"
                                           + prefix + ChatColor.YELLOW +  pluginName + " is outdated"
                                           + prefix + ChatColor.YELLOW + "Please download the latest version from spigotmc.org"
                                           + prefix + ChatColor.YELLOW + "=====================================================x");
                    }
                }

                return true;
            } catch (IOException e) {
                return false;
            }
        }, (runnable) -> plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable));
    }

    /**
     * Get the latest version from SpigotMC with the SPIGOT_RESOURCEID
     *
     * @return Version string from SpigotMC
     * @throws IOException If a connection couldn't be established
     */
    private String getLatestVersion() throws IOException {
        try (InputStream inputStream = new URL(BASE_URL + SPIGOT_RESOURCEID).openStream()) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
                try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    return bufferedReader.readLine();
                }
            }
        }
    }
}
