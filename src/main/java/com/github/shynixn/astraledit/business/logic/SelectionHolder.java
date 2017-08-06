package com.github.shynixn.astraledit.business.logic;

import com.github.shynixn.astraledit.api.entity.PacketArmorstand;
import com.github.shynixn.astraledit.api.entity.Selection;
import com.github.shynixn.astraledit.business.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.business.bukkit.nms.NMSRegistry;
import com.github.shynixn.astraledit.lib.DirectionHelper;
import com.github.shynixn.astraledit.lib.LocationBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

class SelectionHolder implements Selection {

    private boolean usingFollow;
    private boolean usingRotationFollow;

    private LocationBuilder downCorner;
    private LocationBuilder upCorner;

    private boolean isCombined;
    private PacketArmorstand[][][] stands;
    private Location lastLocation;
    private boolean isDestroyed = true;
    private double yaw = -50;
    private boolean isMirrored;
    private boolean flipped;
    private boolean upSideDown;
    private boolean hidden;
    private Player player;

    /**
     * Initializes a new selectionHolder
     *
     * @param player    player
     * @param location1 location1
     * @param location2 location2
     */
    SelectionHolder(Player player, Location location1, Location location2) {
        super();
        this.player = player;
        if (location1 == null || location2 == null)
            throw new IllegalArgumentException("Arguments of selection cannot be null!");
        this.calculateDownLocation(location1, location2);
        this.calculateUpLocation(location1, location2);
        this.lastLocation = this.getDownCornerLocation().toLocation();
    }

    /**
     * Renders the selection
     */
    @Override
    public void render() {
        this.render(false);
    }

    /**
     * Renders the selection and destroys the blocks
     */
    @Override
    public void renderAndDestroy() {
        this.render(true);
    }

    /**
     * Joins the blocks together if teared apart
     */
    @Override
    public void join() {
        if (!this.isCombined) {
            this.combineSelection();
            this.isCombined = true;
        }
    }

    /**
     * Tears the blocks apart if joined
     */
    @Override
    public void tearApart() {
        if (this.isCombined) {
            this.isCombined = false;
            this.teleport(this.getLocation());
        }
    }

    /**
     * Returns if the blocks are joined
     *
     * @return joined
     */
    @Override
    public boolean isJoined() {
        return this.isCombined;
    }

    /**
     * Places the selection at the current location
     */
    @Override
    public void placeBlocks() {
        this.tearApart();
        getPlugin().getServer().getScheduler().runTask(getPlugin(), () -> this.changeSetter(0, 0, 0, 0));
    }

    /**
     * Moves the object to the given location
     *
     * @param location location
     */
    @Override
    public void move(Location location) {
        this.teleport(location);
    }

    /**
     * Returns the location of the selection
     *
     * @return location
     */
    @Override
    public Location getLocation() {
        return this.lastLocation;
    }

    /**
     * Flips the selection
     */
    @Override
    public void flip() {
        this.flipped = !this.flipped;
        this.teleport(this.getLocation());
    }

    /**
     * UpsideDowns the selection
     */
    @Override
    public void upSideDown() {
        this.upSideDown = !this.upSideDown;
        this.teleport(this.getLocation());
    }

    /**
     * Mirros the selection
     */
    @Override
    public void mirror() {
        this.isMirrored = !this.isMirrored;
        this.teleport(this.getLocation());
    }

    /**
     * Checks if the selection is hidden from other player
     *
     * @return hidden
     */
    @Override
    public boolean isHidden() {
        return this.hidden;
    }

    /**
     * Shows the given players the selection
     *
     * @param players players
     */
    @Override
    public void show(Player... players) {
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null) {
                        for (final Player player : players) {
                            if (!player.equals(this.player)) {
                                this.stands[i][j][k].remove();
                                this.stands[i][j][k].spawn();
                            }
                        }
                    }
                }
            }
        }
        this.hidden = false;
    }

    /**
     * Hides the selection from the given player
     *
     * @param players players
     */
    @Override
    public void hide(Player... players) {
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null) {
                        for (final Player player : players) {
                            if (!player.equals(this.player)) {
                                this.stands[i][j][k].remove();
                            }
                        }
                    }
                }
            }
        }
        this.hidden = true;
    }

    /**
     * Returns the owner of the selection
     *
     * @return owner
     */
    @Override
    public Player getOwner() {
        return this.player;
    }

    /**
     * Sets the angle of the single blocks
     *
     * @param eulerAngle angle
     */
    @Override
    public void setBlockAngle(EulerAngle eulerAngle) {
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null)
                        this.stands[i][j][k].setHeadPose(eulerAngle);
                }
            }
        }
    }

    /**
     * Sets auto follow enabled
     *
     * @param enabled enabled
     */
    @Override
    public void setAutoFollowEnabled(boolean enabled) {
        this.usingFollow = enabled;
    }

    /**
     * Set auto follow rotate enabled
     *
     * @param enabled enabled
     */
    @Override
    public void setAutoFollowRotateEnabled(boolean enabled) {
        this.usingRotationFollow = enabled;
    }

    /**
     * Returns if auto follow is enabled
     *
     * @return enabled
     */
    @Override
    public boolean isAutoFollowEnabled() {
        return this.usingFollow;
    }

    /**
     * Returns if auto follow rotate is enabled
     *
     * @return enabled
     */
    @Override
    public boolean isAutoFollowRotateEnabled() {
        return this.usingRotationFollow;
    }

    /**
     * Returns the angle of the single blocks
     *
     * @return angle
     */
    @Override
    public EulerAngle getBlockAngle() {
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null)
                        return this.stands[i][j][k].getHeadPose();
                }
            }
        }
        return null;
    }

    /**
     * Returns the rotation of the selection
     *
     * @return rotation
     */
    @Override
    public double getRotation() {
        return this.getLocation().getYaw();
    }

    /**
     * Rotates the selection
     *
     * @param amount amount
     */
    @Override
    public void rotate(double amount) {
        final Location location = this.setNewYaw(amount);
        this.rotate(location);
    }

    /**
     * Returns the amount of selected blocks
     *
     * @return amount
     */
    @Override
    public int getAmountOfSelectedBlocks() {
        int amount = 0;
        final Location location = this.getDownCornerLocation().toLocation();
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    final Location loc = new Location(location.getWorld(), location.getBlockX() + i, location.getBlockY() + j, location.getBlockZ() + k);
                    if (loc.getBlock().getType() != Material.AIR) {
                        amount++;
                    }
                }
            }
        }
        return amount;
    }

    /**
     * Returns the temporary storage
     *
     * @return storage
     */
    List<Container> getTemporaryStorage() {
        final List<Container> temporaryStorage = new ArrayList<>();
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null) {
                        final Location location = this.stands[i][j][k].getLocation().add(0, 1.5, 0);
                        final Container container = new Container(location.getBlock().getTypeId(), location.getBlock().getData(), new LocationBuilder(location));
                        temporaryStorage.add(container);
                    }
                }
            }
        }
        return temporaryStorage;
    }

    /**
     * Rotates the selection unsecure
     *
     * @param yaw yaw
     */
    void unsecureRotate(double yaw) {
        final Location location = this.getLocation();
        location.setYaw((float) yaw);
        this.rotate(location);
    }

    /**
     * Teleports the object
     *
     * @param location location
     */
    void teleport(Location location) {
        final float yaw = this.lastLocation.getYaw();
        this.lastLocation = location.clone();
        this.lastLocation.setYaw(yaw);
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null) {
                        final Location loc = new Location(location.getWorld(), location.getX() + i + 0.5, location.getY() + j - 1.2, location.getBlockZ() + k + 0.5);
                        this.stands[i][j][k].teleport(loc);
                    }
                }
            }
        }
        this.rotate(0);
    }

    /**
     * Destroys the blocks
     */
    private void destroyBlocks() {
        getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(getPlugin(), () -> {
            final Location location2 = SelectionHolder.this.getDownCornerLocation().toLocation();
            for (int i = 0; i < SelectionHolder.this.getXWidth(); i++) {
                for (int j = 0; j < SelectionHolder.this.getYWidth(); j++) {
                    for (int k = 0; k < SelectionHolder.this.getZWidth(); k++) {
                        final Location loc = new Location(location2.getWorld(), location2.getBlockX() + i, location2.getBlockY() + j, location2.getBlockZ() + k);
                        loc.getBlock().setType(Material.AIR);
                    }
                }
            }
        }, 20L);
    }

    /**
     * Combines the selection
     */
    private void combineSelection() {
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null) {
                        double x = 0, y = 0, z = 0;
                        if (i > 0)
                            x = -0.4 * i;
                        if (j > 0)
                            y = -0.4 * j;
                        if (k > 0)
                            z = -0.4 * k;
                        this.stands[i][j][k].teleport(this.stands[i][j][k].getLocation().add(x, y, z));
                    }
                }
            }
        }
    }

    /**
     * Sets the new yaw
     *
     * @param amount amount
     * @return location
     */
    private Location setNewYaw(double amount) {
        double yaw = this.getRotation();
        if (yaw + amount < 0) {
            amount -= yaw;
            yaw = 360;
        }
        if (yaw + amount > 360) {
            amount -= (360 - yaw);
            yaw = 0;
        }
        yaw += amount;
        this.yaw = yaw;
        final Location location = this.getLocation();
        location.setYaw((float) this.yaw);
        return location;
    }

    /**
     * Executes the rotate calculation
     *
     * @param location location
     */
    private void rotate(Location location) {
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null) {
                        Location location2;
                        if (!this.isMirrored) {
                            location2 = DirectionHelper.getLocationTo(location, DirectionHelper.Direction.LEFT, i);
                            if (!this.flipped) {
                                location2 = DirectionHelper.getLocationTo(location2, DirectionHelper.Direction.UP, j);
                                location2 = DirectionHelper.getLocationTo(location2, DirectionHelper.Direction.FRONT, k);
                            } else {
                                location2 = DirectionHelper.getLocationTo(location2, DirectionHelper.Direction.FRONT, j);
                                location2 = DirectionHelper.getLocationTo(location2, DirectionHelper.Direction.UP, k);
                            }
                        } else {
                            location2 = DirectionHelper.getLocationTo(location, DirectionHelper.Direction.FRONT, i);
                            if (!this.flipped) {
                                location2 = DirectionHelper.getLocationTo(location2, DirectionHelper.Direction.UP, j);
                                location2 = DirectionHelper.getLocationTo(location2, DirectionHelper.Direction.LEFT, k);
                            } else {
                                location2 = DirectionHelper.getLocationTo(location2, DirectionHelper.Direction.LEFT, j);
                                location2 = DirectionHelper.getLocationTo(location2, DirectionHelper.Direction.UP, k);
                            }
                        }
                        this.stands[i][j][k].teleport(location2);
                    }
                }
            }
        }
        final boolean wasCombined = this.isCombined;
        this.isCombined = false;
        if (wasCombined) {
            this.join();
        }
        if (this.upSideDown) {
            this.upSideDownCalculation();
        }
    }

    /**
     * Render calculation
     *
     * @param destroy shouldDestroy
     */
    private void render(boolean destroy) {
        if (this.isDestroyed) {
            this.stands = new PacketArmorstand[this.getXWidth()][this.getYWidth()][this.getZWidth()];
            final Location location = this.getDownCornerLocation().toLocation();
            for (int i = 0; i < this.getXWidth(); i++) {
                for (int j = 0; j < this.getYWidth(); j++) {
                    for (int k = 0; k < this.getZWidth(); k++) {
                        final Location loc = new Location(location.getWorld(), location.getBlockX() + i, location.getBlockY() + j, location.getBlockZ() + k);
                        if (loc.getBlock().getType() != Material.AIR) {
                            this.stands[i][j][k] = NMSRegistry.createPacketArmorstand(this.player
                                    , new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY() - 1.2, loc.getZ() + 0.5), loc.getBlock().getTypeId(), loc.getBlock().getData());
                            this.stands[i][j][k].spawn();
                        }
                    }
                }
            }
            if (destroy) {
                this.destroyBlocks();
            }
            this.isDestroyed = false;
        }
    }

    /**
     * Settter calculation
     *
     * @param a       a
     * @param b       b
     * @param c       c
     * @param counter coutner
     */
    private void changeSetter(final int a, final int b, final int c, final int counter) {
        if (this.stands[a][b][c] != null) {
            this.stands[a][b][c].getLocation().add(0, 1.5, 0).getBlock().setTypeId(this.stands[a][b][c].getStoredBlockId());
            this.stands[a][b][c].getLocation().add(0, 1.5, 0).getBlock().setData(this.stands[a][b][c].getStoredBlockData());
        }
        if (counter > 100) {
            getPlugin().getServer().getScheduler().runTaskLater(getPlugin(), () -> this.changeCalculation(a, b, c, 0), 1L);
        } else {
            this.changeCalculation(a, b, c, counter + 1);
        }
    }

    /**
     * Calculation part of the placing
     *
     * @param a       a
     * @param b       b
     * @param c       c
     * @param counter coutner
     */
    private void changeCalculation(final int a, final int b, final int c, final int counter) {
        if ((a + 1) < this.getXWidth()) {
            this.changeSetter(a + 1, b, c, counter);
        } else if ((b + 1) < this.getYWidth()) {
            this.changeSetter(0, b + 1, c, counter);
        } else if ((c + 1) < this.getZWidth()) {
            this.changeSetter(0, 0, c + 1, counter);
        }
    }

    /**
     * Upside Down Calculation
     */
    private void upSideDownCalculation() {
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null) {
                        this.stands[i][j][k].teleport(this.stands[i][j][k].getLocation().add(0, -1 * j * 2, 0));
                    }
                }
            }
        }
    }

    /**
     * Returns the upCorner
     *
     * @return upCorner
     */
    private LocationBuilder getUpCornerLocation() {
        return this.upCorner;
    }

    /**
     * Returns the downCorner
     *
     * @return downCorner
     */
    private LocationBuilder getDownCornerLocation() {
        return this.downCorner;
    }

    /**
     * Returns the width of the x axe
     *
     * @return width
     */
    private int getXWidth() {
        return this.upCorner.getBlockX() - this.downCorner.getBlockX() + 1;
    }

    /**
     * Returns the width of the y axe
     *
     * @return width
     */
    private int getYWidth() {
        return this.upCorner.getBlockY() - this.downCorner.getBlockY() + 1;
    }

    /**
     * Returns the width of the z axe
     *
     * @return width
     */
    private int getZWidth() {
        return this.upCorner.getBlockZ() - this.downCorner.getBlockZ() + 1;
    }

    /**
     * Calculates the upLocation
     *
     * @param corner1 corner1
     * @param corner2 corner2
     */
    private void calculateUpLocation(Location corner1, Location corner2) {
        final int x;
        if (corner1.getBlockX() > corner2.getBlockX()) {
            x = corner1.getBlockX();
        } else {
            x = corner2.getBlockX();
        }
        final int y;
        if (corner1.getBlockY() > corner2.getBlockY()) {
            y = corner1.getBlockY();
        } else {
            y = corner2.getBlockY();
        }
        final int z;
        if (corner1.getBlockZ() > corner2.getBlockZ()) {
            z = corner1.getBlockZ();
        } else {
            z = corner2.getBlockZ();
        }
        this.upCorner = new LocationBuilder(new Location(corner1.getWorld(), x, y, z));
    }

    /**
     * Calculates the downLocation
     *
     * @param corner1 corner1
     * @param corner2 corner2
     */
    private void calculateDownLocation(Location corner1, Location corner2) {
        final int x;
        if (corner1.getBlockX() < corner2.getBlockX()) {
            x = corner1.getBlockX();
        } else {
            x = corner2.getBlockX();
        }
        final int y;
        if (corner1.getBlockY() < corner2.getBlockY()) {
            y = corner1.getBlockY();
        } else {
            y = corner2.getBlockY();
        }
        final int z;
        if (corner1.getBlockZ() < corner2.getBlockZ()) {
            z = corner1.getBlockZ();
        } else {
            z = corner2.getBlockZ();
        }
        this.downCorner = new LocationBuilder(new Location(corner1.getWorld(), x, y, z));
    }

    private static Plugin getPlugin() {
        return JavaPlugin.getPlugin(AstralEditPlugin.class);
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     * <p>
     * <p>While this interface method is declared to throw {@code
     * Exception}, implementers are <em>strongly</em> encouraged to
     * declare concrete implementations of the {@code close} method to
     * throw more specific exceptions, or to throw no exception at all
     * if the close operation cannot fail.
     * <p>
     * <p> Cases where the close operation may fail require careful
     * attention by implementers. It is strongly advised to relinquish
     * the underlying resources and to internally <em>mark</em> the
     * resource as closed, prior to throwing the exception. The {@code
     * close} method is unlikely to be invoked more than once and so
     * this ensures that the resources are released in a timely manner.
     * Furthermore it reduces problems that could arise when the resource
     * wraps, or is wrapped, by another resource.
     * <p>
     * <p><em>Implementers of this interface are also strongly advised
     * to not have the {@code close} method throw {@link
     * InterruptedException}.</em>
     * <p>
     * This exception interacts with a thread's interrupted status,
     * and runtime misbehavior is likely to occur if an {@code
     * InterruptedException} is {@linkplain Throwable#addSuppressed
     * suppressed}.
     * <p>
     * More generally, if it would cause problems for an
     * exception to be suppressed, the {@code AutoCloseable.close}
     * method should not throw it.
     * <p>
     * <p>Note that unlike the {@link Closeable#close close}
     * method of {@link Closeable}, this {@code close} method
     * is <em>not</em> required to be idempotent.  In other words,
     * calling this {@code close} method more than once may have some
     * visible side effect, unlike {@code Closeable.close} which is
     * required to have no effect if called more than once.
     * <p>
     * However, implementers of this interface are strongly encouraged
     * to make their {@code close} methods idempotent.
     *
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public void close() throws Exception {
        for (int i = 0; i < this.getXWidth(); i++) {
            for (int j = 0; j < this.getYWidth(); j++) {
                for (int k = 0; k < this.getZWidth(); k++) {
                    if (this.stands[i][j][k] != null) {
                        this.stands[i][j][k].close();
                    }
                }
            }
        }
        this.player = null;
        this.isDestroyed = true;
    }
}
