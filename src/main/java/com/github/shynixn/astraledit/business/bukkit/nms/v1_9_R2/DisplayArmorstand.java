package com.github.shynixn.astraledit.business.bukkit.nms.v1_9_R2;

import com.github.shynixn.astraledit.api.entity.PacketArmorstand;
import com.github.shynixn.astraledit.business.bukkit.nms.NMSRegistry;
import com.github.shynixn.astraledit.lib.ItemStackBuilder;
import net.minecraft.server.v1_9_R2.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import java.io.Closeable;

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
public class DisplayArmorstand implements PacketArmorstand {

    private Player player;
    private final EntityArmorStand armorStand;
    private int storedId;
    private byte storedData;

    /**
     * Initializes the armorstand
     *
     * @param player player
     * @param id     id
     * @param data   data
     */
    public DisplayArmorstand(Player player, Location location, int id, byte data) {
        super();
        this.player = player;
        this.armorStand = new EntityArmorStand(((CraftWorld) player.getWorld()).getHandle());
        final NBTTagCompound compound = new NBTTagCompound();
        compound.setBoolean("invulnerable", true);
        compound.setBoolean("Invisible", true);
        compound.setBoolean("PersistenceRequired", true);
        compound.setBoolean("NoBasePlate", true);
        this.armorStand.a(compound);
        this.armorStand.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        this.storedId = id;
        this.storedData = data;

        final ItemStackBuilder stackBuilder = new ItemStackBuilder(Material.getMaterial(id), 1, data);
        ((ArmorStand) this.armorStand.getBukkitEntity()).setHelmet(stackBuilder.build());
        if (((ArmorStand) this.armorStand.getBukkitEntity()).getHelmet().getType() == Material.AIR) {
            stackBuilder.setType(Material.SKULL_ITEM);
            stackBuilder.getData().setData((byte) 3);
            if (id == Material.WATER.getId() || id == Material.STATIONARY_WATER.getId()) {
                stackBuilder.setSkin(NMSRegistry.WATER_HEAD);
            } else if (id == Material.LAVA.getId() || id == Material.STATIONARY_LAVA.getId()) {
                stackBuilder.setSkin(NMSRegistry.LAVA_HEAD);
            } else {
                stackBuilder.setSkin(NMSRegistry.NOT_FOUND);
            }
            ((ArmorStand) this.armorStand.getBukkitEntity()).setHelmet(stackBuilder.build());
        }
    }

    /**
     * Spawns the armorstand
     */
    @Override
    public void spawn() {
        final PacketPlayOutSpawnEntityLiving packetSpawn = new PacketPlayOutSpawnEntityLiving(this.armorStand);
        this.sendPacket(packetSpawn);
        final PacketPlayOutEntityEquipment packetHead =
                new PacketPlayOutEntityEquipment(this.armorStand.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(((ArmorStand) this.armorStand.getBukkitEntity()).getHelmet()));
        this.sendPacket(packetHead);
    }

    /**
     * Teleports the armorstand to the given location
     *
     * @param location location
     */
    @Override
    public void teleport(Location location) {
        this.armorStand.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        final PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport(this.armorStand);
        this.sendPacket(teleportPacket);
    }

    /**
     * Removes the armorstand
     */
    @Override
    public void remove() {
        final PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy(this.armorStand.getId());
        this.sendPacket(destroyPacket);
    }

    /**
     * Returns the location of the armorstand
     *
     * @return location
     */
    @Override
    public Location getLocation() {
        return this.armorStand.getBukkitEntity().getLocation();
    }

    /**
     * Sets the pose of
     *
     * @param angle angle
     */
    @Override
    public void setHeadPose(EulerAngle angle) {
        ((ArmorStand) this.armorStand).setHeadPose(angle);
    }

    /**
     * Returns the pose of the head
     *
     * @return angle
     */
    @Override
    public EulerAngle getHeadPose() {
        return ((ArmorStand) this.armorStand).getHeadPose();
    }

    /**
     * Returns the stored block id
     *
     * @return id
     */
    @Override
    public int getStoredBlockId() {
        return this.storedId;
    }

    /**
     * Sets the stored block id
     *
     * @param id id
     */
    @Override
    public void setStoreBlockId(int id) {
        this.storedId = id;
    }

    /**
     * Returns the stored block data
     *
     * @return data
     */
    @Override
    public byte getStoredBlockData() {
        return this.storedData;
    }

    /**
     * Sets the stored block data
     *
     * @param data data
     */
    @Override
    public void setStoredBlockData(byte data) {
        this.storedData = data;
    }

    /**
     * Sends the packet
     *
     * @param packet packet
     */
    private void sendPacket(Packet<?> packet) {
        ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
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
        this.remove();
        this.player = null;
    }
}
