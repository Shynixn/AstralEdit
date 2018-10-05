# AstralEdit [![Build Status](https://maven-badges.herokuapp.com/maven-central/com.github.shynixn/astraledit/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.github.shynixn.astraledit/astraledit-bukkit-api) [![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat-square)](https://raw.githubusercontent.com/Shynixn/AstralEdit/master/LICENSE)

| branch        | status        | coverage | version | download |
| ------------- | ------------- | -------- | --------| ---------| 
| master        | [![Build Status](https://img.shields.io/travis/Shynixn/AstralEdit/master.svg?style=flat-square)](https://travis-ci.org/Shynixn/AstralEdit) | [![Coverage](https://img.shields.io/codecov/c/github/shynixn/astraledit/master.svg?style=flat-square)](https://codecov.io/gh/Shynixn/AstralEdit/branch/master)|![GitHub license](https://img.shields.io/nexus/r/https/oss.sonatype.org/com.github.shynixn.astraledit/astraledit-bukkit-plugin.svg?style=flat-square)  |[Download latest release](https://github.com/Shynixn/AstralEdit/releases)|
| experimental   | [![Build Status](https://img.shields.io/travis/Shynixn/AstralEdit/experimental.svg?style=flat-square)](https://travis-ci.org/Shynixn/AstralEdit)|[![Coverage](https://img.shields.io/codecov/c/github/shynixn/astraledit/experimental.svg?style=flat-square)](https://codecov.io/gh/Shynixn/AstralEdit/branch/experimental) |![GitHub license](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.github.shynixn.astraledit/astraledit-bukkit-plugin.svg?style=flat-square) |  [Download snapshots](https://oss.sonatype.org/content/repositories/snapshots/com/github/shynixn/astraledit/astraledit-bukkit-plugin/) |

JavaDocs: https://shynixn.github.io/AstralEdit/apidocs/

## Description
AstralEdit is a spigot plugin to render WorldEdit selections in Minecraft.

## Features

* View your WorldEdit selection in advance
* Move, rotate, flip, mirror selections
* Place your selection anywhere
* Version support 1.8.R1 - 1.12.R1

## Installation

* Please check out the [AstralEdit-Spigot-Page](https://www.spigotmc.org/resources/11409/) for further information.

## Screenshots

![alt tag](http://www.mediafire.com/convkey/7e52/ruxjmez4fx820jjzg.jpg)
![alt tag](http://www.mediafire.com/convkey/de9a/s37xusb1guym4fbzg.jpg)

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

## Contributing

* Fork the AstralEdit project on github and clone it to your local environment.
* Use BuildTools.jar from spigotmc.org to build the following dependencies.

```text
    java -jar BuildTools.jar --rev 1.8
    java -jar BuildTools.jar --rev 1.8.3
    java -jar BuildTools.jar --rev 1.8.8
    java -jar BuildTools.jar --rev 1.9
    java -jar BuildTools.jar --rev 1.9.4
    java -jar BuildTools.jar --rev 1.10
    java -jar BuildTools.jar --rev 1.11
    java -jar BuildTools.jar --rev 1.12
    java -jar BuildTools.jar --rev 1.13
    java -jar BuildTools.jar --rev 1.13.1
```

* Install the created libraries to your local maven repository.

```text
    mvn install:install-file -Dfile=spigot-1.8.jar -DgroupId=org.spigotmc -DartifactId=spigot18R1 -Dversion=1.8.0-R1.0 -Dpackaging=jar
    mvn install:install-file -Dfile=spigot-1.8.3.jar -DgroupId=org.spigotmc -DartifactId=spigot18R2 -Dversion=1.8.3-R2.0 -Dpackaging=jar
    mvn install:install-file -Dfile=spigot-1.8.8.jar -DgroupId=org.spigotmc -DartifactId=spigot18R3 -Dversion=1.8.8-R3.0 -Dpackaging=jar
    mvn install:install-file -Dfile=spigot-1.9.jar -DgroupId=org.spigotmc -DartifactId=spigot19R1 -Dversion=1.9.0-R1.0 -Dpackaging=jar
    mvn install:install-file -Dfile=spigot-1.9.4.jar -DgroupId=org.spigotmc -DartifactId=spigot19R2 -Dversion=1.9.4-R2.0 -Dpackaging=jar
    mvn install:install-file -Dfile=spigot-1.10.2.jar -DgroupId=org.spigotmc -DartifactId=spigot110R1 -Dversion=1.10.2-R1.0 -Dpackaging=jar
    mvn install:install-file -Dfile=spigot-1.11.jar -DgroupId=org.spigotmc -DartifactId=spigot111R1 -Dversion=1.11.0-R1.0 -Dpackaging=jar
    mvn install:install-file -Dfile=spigot-1.12.jar -DgroupId=org.spigotmc -DartifactId=spigot112R1 -Dversion=1.12.0-R1.0 -Dpackaging=jar
    mvn install:install-file -Dfile=spigot-1.13.jar -DgroupId=org.spigotmc -DartifactId=spigot113R1 -Dversion=1.13.0-R1.0 -Dpackaging=jar
    mvn install:install-file -Dfile=spigot-1.13.1.jar -DgroupId=org.spigotmc -DartifactId=spigot113R2 -Dversion=1.13.1-R2.0 -Dpackaging=jar
```

* Reimport the AstralEdit maven project and execute 'mvn package' afterwards.
* The generated astraledit-bukkit-plugin/target/astraledit-bukkit-plugin-version.jar can be used for testing on a server.

## Licence

Copyright 2015-2018 Shynixn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.