package com.github.shynixn.astraledit;

import com.github.shynixn.astraledit.lib.LocationBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Shynixn 2017.
 * <p>
 * Version 1.1
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017 by Shynixn
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
public class DirectionTest {

    @Test
    public void onRelativeRightDirectionTest() {
        final World world = mock(World.class);
        final Location location1 = new Location(world, 5, 282, 201.2);
        final LocationBuilder location3 = new LocationBuilder(location1);
        assertEquals(location1.getX(), location3.getX());
        location3.relativePosition(5, LocationBuilder.Direction.RIGHT);

        assertEquals(0.0, location3.getX());
        assertEquals(282.0, location3.getY());
        assertEquals(201.2, location3.getZ());
        assertEquals(0.0, location3.getYaw());
        assertEquals(0.0, location3.getPitch());
    }

    @Test
    public void onRelativeLeftDirectionTest() {
        final World world = mock(World.class);
        final Location location1 = new Location(world, 5, 282, 201.2);
        final LocationBuilder location3 = new LocationBuilder(location1);
        assertEquals(location1.getX(), location3.getX());
        location3.relativePosition(7, LocationBuilder.Direction.LEFT);

        assertEquals(12.0, location3.getX());
        assertEquals(282.0, location3.getY());
        assertEquals(201.2, location3.getZ());
        assertEquals(0.0, location3.getYaw());
        assertEquals(0.0, location3.getPitch());
    }

    @Test
    public void onRelativeForwardDirectionTest() {
        final World world = mock(World.class);
        final Location location1 = new Location(world, 5, 282, 201.2);
        final LocationBuilder location3 = new LocationBuilder(location1);
        assertEquals(location1.getX(), location3.getX());
        location3.relativePosition(18, LocationBuilder.Direction.FORWARD);

        assertEquals(5.000000000000001, location3.getX());
        assertEquals(282.0, location3.getY());
        assertEquals(219.2, location3.getZ());
        assertEquals(0.0, location3.getYaw());
        assertEquals(0.0, location3.getPitch());
    }

    @Test
    public void onRelativeBackDirectionTest() {
        final World world = mock(World.class);
        final Location location1 = new Location(world, 5, 282, 201.2);
        final LocationBuilder location3 = new LocationBuilder(location1);
        assertEquals(location1.getX(), location3.getX());
        location3.relativePosition(9, LocationBuilder.Direction.BACKWARDS);

        assertEquals(4.999999999999999, location3.getX());
        assertEquals(282.0, location3.getY());
        assertEquals(192.2, location3.getZ());
        assertEquals(0.0, location3.getYaw());
        assertEquals(0.0, location3.getPitch());
    }

    @Test
    public void onRelativeUpDirectionTest() {
        final World world = mock(World.class);
        final Location location1 = new Location(world, 5, 282, 201.2);
        final LocationBuilder location3 = new LocationBuilder(location1);
        assertEquals(location1.getX(), location3.getX());
        location3.relativePosition(11, LocationBuilder.Direction.UP);

        assertEquals(5.0, location3.getX());
        assertEquals(293.0, location3.getY());
        assertEquals(201.2, location3.getZ());
        assertEquals(0.0, location3.getYaw());
        assertEquals(0.0, location3.getPitch());
    }

    @Test
    public void onRelativeDownDirectionTest() {
        final World world = mock(World.class);
        final Location location1 = new Location(world, 5, 282, 201.2);
        final LocationBuilder location3 = new LocationBuilder(location1);
        assertEquals(location1.getX(), location3.getX());
        location3.relativePosition(27, LocationBuilder.Direction.DOWN);

        assertEquals(5.0, location3.getX());
        assertEquals(255.0, location3.getY());
        assertEquals(201.2, location3.getZ());
        assertEquals(0.0, location3.getYaw());
        assertEquals(0.0, location3.getPitch());
    }
}
