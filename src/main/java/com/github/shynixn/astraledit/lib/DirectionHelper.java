package com.github.shynixn.astraledit.lib;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

@Deprecated
public class DirectionHelper {
    public static Location getLocationTo(Location location, Direction direction, double distance) {
        if (direction == Direction.FRONT || direction == Direction.BACK) {
            if (Direction.BACK == direction) {
                distance = distance * -1;
            }
            return new Location(location.getWorld(), calcNewX(location, distance, 1), location.getY(), calcNewZ(location, distance, 1), location.getYaw(), location.getPitch());
        } else if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            if (Direction.RIGHT == direction) {
                distance = distance * -1;
            }
            return new Location(location.getWorld(), calcNewX(location, distance, 0), location.getY(), calcNewZ(location, distance, 0), location.getYaw(), location.getPitch());
        } else if (direction == Direction.UP) {
            return new Location(location.getWorld(), location.getX(), location.getY() + distance, location.getZ(), location.getYaw(), location.getPitch());
        } else if (direction == Direction.DOWN) {
            distance = distance * -1;
            return new Location(location.getWorld(), location.getX(), location.getY() + distance, location.getZ(), location.getYaw(), location.getPitch());
        }
        return null;
    }

    public static Location getLocationTo(Entity entity, Direction direction, double distance) {
        return getLocationTo(entity.getLocation().clone(), direction, distance);
    }

    public static Location getLocationTo(Entity entity, double frontback, double updown, double leftright) {
        return getLocationTo(entity.getLocation().clone(), frontback, leftright, updown);
    }

    public static Location getLocationTo(Location location, double frontback, double updown, double leftright) {
        if (frontback < 0)
            location = getLocationTo(location, Direction.BACK, frontback * -1);
        else
            location = getLocationTo(location, Direction.FRONT, frontback);
        if (leftright < 0)
            location = getLocationTo(location, Direction.LEFT, frontback * -1);
        else
            location = getLocationTo(location, Direction.RIGHT, frontback);
        if (updown < 0)
            location = getLocationTo(location, Direction.DOWN, frontback * -1);
        else
            location = getLocationTo(location, Direction.UP, frontback);
        return location;
    }

    private static double calcNewZ(Location location, double distance, int direction) {
        return (location.getZ() + (distance * Math.sin(Math.toRadians(location.getYaw() + 90 * direction))));
    }

    private static double calcNewX(Location location, double distance, int direction) {
        return (location.getX() + (distance * Math.cos(Math.toRadians(location.getYaw() + 90 * direction))));
    }

    public static enum Direction {
        LEFT,
        RIGHT,
        DOWN,
        UP,
        FRONT,
        BACK;
    }
}
