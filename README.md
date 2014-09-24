BukkitCommons
=============

**UPDATE: No longer supports Bukkit. Will port to [TridentSDK](http://github.com/TridentSDK).**

Build stats: [![Build Status](https://drone.io/github.com/AgentTroll/BukkitCommons/status.png)](https://drone.io/github.com/AgentTroll/BukkitCommons/latest)

Public CI + CI builds: https://drone.io/github.com/AgentTroll/BukkitCommons

Latest Build download: https://github.com/AgentTroll/BukkitCommonsBuilds/blob/master/BukkitCommons.jar?raw=true

Documentation/Bugs: http://bukkitcommons.atlassian.net

Javadocs: http://agenttroll.github.io/BukkitCommons/

Setup:
You can download this jar directly from the link above, or you can use Maven:
```xml
<!-- Repository -->
<repository>
    <id>bc-repo</id>
    <url>https://raw.githubusercontent.com/AgentTroll/BukkitCommonsBuilds/master/</url>
    <snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
    </snapshots>
</repository>

<!-- Dependency -->
<dependency>
    <groupId>com.gmail.woodyc40</groupId>
    <artifactId>BukkitCommons</artifactId>
    <version>1.0</version>
</dependency>
```

This library was profiled with JProfiler. JProfiler can be downloaded at:
http://www.ej-technologies.com/download/jprofiler/files.html
