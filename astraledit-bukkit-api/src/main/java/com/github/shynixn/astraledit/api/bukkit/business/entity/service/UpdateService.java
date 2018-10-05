package com.github.shynixn.astraledit.api.bukkit.business.entity.service;

import java.util.concurrent.CompletableFuture;

public interface UpdateService {
    CompletableFuture<Boolean> checkForUpdates();
}
