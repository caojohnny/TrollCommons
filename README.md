TrollCommons
=============

**UPDATE: No longer supports Bukkit. Will port to [TridentSDK](http://github.com/TridentSDK).**

Build stats: ![Build Status](https://api.shippable.com/projects/54260b1280088cee586ce9f0/badge?branchName=master)

Public CI + CI builds: https://drone.io/github.com/AgentTroll/TrollCommons

Latest Build download: https://github.com/AgentTroll/TrollCommonsBuilds/blob/master/TrollCommons.jar?raw=true

Documentation/Bugs: http://TrollCommons.atlassian.net

Javadocs: http://agenttroll.github.io/TrollCommons/

Setup:
You can download this jar directly from the link above, or you can use Maven:
```xml
<!-- Repository -->
<repository>
    <id>bc-repo</id>
    <url>https://raw.githubusercontent.com/AgentTroll/TrollCommonsBuilds/master/</url>
    <snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
    </snapshots>
</repository>

<!-- Dependency -->
<dependency>
    <groupId>com.gmail.woodyc40</groupId>
    <artifactId>TrollCommons</artifactId>
    <version>1.0</version>
</dependency>
```

This library was profiled with JProfiler. JProfiler can be downloaded at:
http://www.ej-technologies.com/download/jprofiler/files.html
