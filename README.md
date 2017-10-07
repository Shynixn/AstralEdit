# AstralEdit [![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://raw.githubusercontent.com/Shynixn/AstralEdit/master/LICENSE)

| branch        | status        | download      |
| ------------- | --------------| --------------| 
| master        | [![Build Status](https://travis-ci.org/Shynixn/AstralEdit.svg?branch=master)](https://travis-ci.org/Shynixn/AstralEdit) |[Download latest release (recommend)](https://github.com/Shynixn/AstralEdit/releases)|
| experimental      | [![Build Status](https://travis-ci.org/Shynixn/AstralEdit.svg?branch=experimental)](https://travis-ci.org/Shynixn/AstralEdit) | [Download snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/shynixn/astraledit/) |

JavaDocs: https://shynixn.github.io/AstralEdit/apidocs/

## Description
WorldEdit Extension to modify your world in a way you have not seen before.

## Features

* Modify your world in a unique and funny way
* Helps being more productive
* Version support 1.8.R1 - 1.12.R1
* Check out the [AstralEdit-Spigot-Page](https://www.spigotmc.org/resources/renderedworldedit_lite.11409/) to get more information. 

## Installation

* [Download the plugin AstralEdit](https://github.com/Shynixn/AstralEdit/releases)
* Put the plugin into your plugin folder
* Start the server (1.8.0 - 1.12.2, Java 8/Java 9)
* Join and play :)

## API

* Reference the AstralEdit.jar in your own projects.
* If you are using maven or gradle you can add it from the central maven repository

### Maven

```xml
<dependency>
     <groupId>com.github.shynixn</groupId>
     <artifactId>astraledit</artifactId>
     <version>1.1.0</version>
     <scope>provided</scope>
</dependency>
```

### Gradle

```xml
dependencies {
    compileOnly 'com.github.shynixn:astraledit:1.1.0'
}
```

## How to use the it

#### Sample: Rendering the WorldEdit selection of the player and moving it to the player's location

```java
//Get a player and a plugin instance
Player player;
Plugin plugin;

 //It is highly recommend to work with selections asynchronly to increase server performance
Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
   @Override
   public void run() {
       Selection selection = AstralEditApi.render(player);
       selection.move(player.getLocation());
   }
});
```
#### Sample: Rendering the blocks between two given corners, moving it to a targetLocation and placing the blocks. 

```java
//Select the locations
Location corner1 = new Location(Bukkit.getWorld("world"), 20, 5, 20);
Location corner2 = new Location(Bukkit.getWorld("world"), 40, 10, 40);
Location targetLocation  = new Location(Bukkit.getWorld("world"), 70, 8, 40);

//Get a player and a plugin instance
Player player;
Plugin plugin;

Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
        @Override
        public void run() {
            Selection selection = AstralEditApi.renderAndDestroy(player,corner1, corner2);
            selection.move(targetLocation);
            selection.placeBlocks();
            AstralEditApi.clearRenderedObject(player); //Do not forget to clean up the selection
        }
});
```

* Check out the [AstralEdit-Spigot-Page](https://www.spigotmc.org/resources/renderedworldedit_lite.11409/) to get more information. 

## Screenshots

![alt tag](http://www.mediafire.com/convkey/6605/czkr85tdoq751g7zg.jpg)

## Gifs

![alt tag](http://www.mediafire.com/convkey/de9a/s37xusb1guym4fbzg.jpg)

## Licence

Copyright 2017 Shynixn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.