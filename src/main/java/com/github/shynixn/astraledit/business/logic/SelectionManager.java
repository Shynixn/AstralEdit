package com.github.shynixn.astraledit.business.logic;

import com.github.shynixn.astraledit.api.entity.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.EulerAngle;

import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public final class SelectionManager implements Runnable, AutoCloseable {
    private final HashMap<Player, Operation[]> operations = new HashMap<>();
    private final HashMap<Player, SelectionHolder> selections = new HashMap<>();
    private final int maxUndoAmount;

    /**
     * Initializes the selection Manager
     *
     * @param plugin plugin
     */
    public SelectionManager(Plugin plugin) {
        super();
        this.maxUndoAmount = plugin.getConfig().getInt("general.max-undo-amount");
        new SelectionListener(this, plugin);
        new SelectionCommandExecutor(this);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0, 1L);
    }

    /**
     * Creates a new selection
     *
     * @param player  player
     * @param corner1 corner1
     * @param corner2 corner2
     * @return selection
     */
    public Selection create(Player player, Location corner1, Location corner2) {
        return new SelectionHolder(player, corner1, corner2);
    }

    /**
     * Adds the selection to the manager to be managed
     *
     * @param player    player
     * @param selection selection
     */
    public void addSelection(Player player, Selection selection) {
        try {
            if (this.hasSelection(player)) {
                this.selections.get(player).close();
            }
            if (selection instanceof SelectionHolder) {
                this.selections.put(player, (SelectionHolder) selection);
            }
        } catch (final Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to clear selection.", e);
        }
    }

    /**
     * Returns the selection of a player
     *
     * @param player player
     * @return selection
     */
    public Selection getSelection(Player player) {
        if (this.selections.containsKey(player)) {
            return this.selections.get(player);
        }
        return null;
    }

    /**
     * Checks if the player has got a selection
     *
     * @param player player
     * @return selection
     */
    public boolean hasSelection(Player player) {
        return this.selections.containsKey(player);
    }

    /**
     * Clears the selection of the player
     *
     * @param player player
     */
    public void clearSelection(Player player) {
        try {
            if (this.selections.containsKey(player)) {
                this.selections.get(player).close();
                this.selections.remove(player);
            }
        } catch (final Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to clear selection.", e);
        }
    }

    /**
     * Adds a new operation to the undo Stack
     *
     * @param player    player
     * @param operation operation
     */
    void addOperation(Player player, Operation operation) {
        if (!this.operations.containsKey(player))
            this.operations.put(player, new Operation[this.maxUndoAmount]);
        Operation oldOperation;
        for (int i = 0; i < this.operations.get(player).length; i++) {
            oldOperation = this.operations.get(player)[i];
            this.operations.get(player)[i] = operation;
            operation = oldOperation;
        }
    }

    /**
     * Removes all operations from the player
     *
     * @param player player
     */
    void clearOperations(Player player) {
        if (this.operations.containsKey(player)) {
            this.operations.remove(player);
        }
    }

    /**
     * Undos an operation
     *
     * @param player player
     */
    boolean undoOperation(Player player) {
        if (!this.hasSelection(player))
            return false;
        if (!this.operations.containsKey(player))
            this.operations.put(player, new Operation[this.maxUndoAmount]);
        final Operation operation = this.operations.get(player)[0];
        if (operation != null) {
            if (operation.getType() == OperationType.MIRROR) {
                this.getSelection(player).mirror();
            } else if (operation.getType() == OperationType.FLIP) {
                this.getSelection(player).flip();
            } else if (operation.getType() == OperationType.UPSIDEDOWN) {
                this.getSelection(player).upSideDown();
            } else if (operation.getType() == OperationType.UNCOMBINE) {
                this.getSelection(player).join();
            } else if (operation.getType() == OperationType.COMBINE) {
                this.getSelection(player).tearApart();
            } else if (operation.getType() == OperationType.ROTATE) {
                this.selections.get(player).unsecureRotate((Double) operation.getOperationData());
            } else if (operation.getType() == OperationType.ANGLES) {
                this.getSelection(player).setBlockAngle((EulerAngle) operation.getOperationData());
            } else if (operation.getType() == OperationType.PLACE || operation.getType() == OperationType.CONVERTOBLOCKS) {
                final List<Container> containers = (List<Container>) operation.getOperationData();
                for (final Container dummy : containers) {
                    dummy.location.toLocation().getBlock().setTypeId(dummy.id);
                    dummy.location.toLocation().getBlock().setData(dummy.data);
                }
            } else if (operation.getType() == OperationType.MOVE) {
                this.selections.get(player).teleport((Location) operation.getOperationData());
            }
            this.removeOperation(player);
            return true;
        }
        return false;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        for (final Player player : this.selections.keySet()) {
            final SelectionHolder selectionHolder = this.selections.get(player);
            if (selectionHolder.isAutoFollowEnabled()) {
                selectionHolder.teleport(player.getLocation());
            }
            if (selectionHolder.isAutoFollowRotateEnabled()) {
                selectionHolder.unsecureRotate(player.getLocation().getYaw());
            }
        }
    }

    /**
     * Removes an operation from the undo stack
     *
     * @param player player
     */
    private void removeOperation(Player player) {
        for (int i = 0; i < this.operations.get(player).length - 1; i++) {
            this.operations.get(player)[i] = this.operations.get(player)[i + 1];
            this.operations.get(player)[i + 1] = null;
        }
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public void close() throws Exception {
        for (final Player player : this.selections.keySet()) {
            this.clearSelection(player);
        }
        this.selections.clear();
        this.operations.clear();
    }
}
