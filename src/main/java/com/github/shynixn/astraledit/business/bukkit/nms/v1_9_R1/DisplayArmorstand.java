package com.github.shynixn.astraledit.business.bukkit.nms.v1_9_R1;

import com.github.shynixn.astraledit.api.entity.PacketArmorstand;
import com.github.shynixn.astraledit.business.bukkit.nms.NMSRegistry;
import com.github.shynixn.astraledit.lib.ItemStackBuilder;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

import java.io.Closeable;
import java.util.Set;

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
    private Set<Player> watchers;

    /**
     * Initializes the armorstand
     *
     * @param player   player
     * @param location location
     * @param id       id
     * @param data     data
     */
    public DisplayArmorstand(Player player, Location location, int id, byte data, Set<Player> watchers) {
        super();
        this.watchers = watchers;
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

        ItemStackBuilder stackBuilder = new ItemStackBuilder(Material.getMaterial(id), 1, data);
        this.getCraftEntity().setHelmet(stackBuilder.build());
        this.getCraftEntity().setBodyPose(new EulerAngle(3.15, 0, 0));
        this.getCraftEntity().setLeftLegPose(new EulerAngle(3.15, 0, 0));
        this.getCraftEntity().setRightLegPose(new EulerAngle(3.15, 0, 0));
        this.getCraftEntity().setGlowing(true);

        if (((ArmorStand) this.armorStand.getBukkitEntity()).getHelmet().getType() == Material.AIR) {
            stackBuilder = new ItemStackBuilder(Material.SKULL_ITEM, 1, (short) 3);
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
        final PacketPlayOutEntityEquipment packetHead =
                new PacketPlayOutEntityEquipment(this.armorStand.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(((ArmorStand) this.armorStand.getBukkitEntity()).getHelmet()));
        this.sendPacket(packetSpawn);
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
        ((ArmorStand) this.armorStand.getBukkitEntity()).setHeadPose(angle);
    }

    /**
     * Returns the pose of the head
     *
     * @return angle
     */
    @Override
    public EulerAngle getHeadPose() {
        return ((ArmorStand) this.armorStand.getBukkitEntity()).getHeadPose();
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
        this.sendPacket(packet, this.player);
        for (final Player player : this.watchers) {
            this.sendPacket(packet, player);
        }
    }

    /**
     * Sends the packet
     *
     * @param player player
     * @param packet packet
     */
    private void sendPacket(Packet<?> packet, Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Returns the craftArmorstand
     *
     * @return stand
     */
    private CraftArmorStand getCraftEntity() {
        return (CraftArmorStand) this.armorStand.getBukkitEntity();
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     * @throws Exception if this resource cannot be closed
     */
    @Override
    public void close() throws Exception {
        this.remove();
        this.player = null;
    }
}
