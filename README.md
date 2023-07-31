# Chococraft 3
This mod is rewritten from scratch and is based on Chococraft 1 and 2.

Due to the massive changes in MC Forge, Chococraft 3 is created mostly from scratch.

# License information
The source code of this mod, which is located under `src/main/java`, is licensed under the MIT license, see [LICENSE-code.md](LICENSE-code.md)

![https://licensebuttons.net/l/by-sa/3.0/88x31.png](https://licensebuttons.net/l/by-sa/3.0/88x31.png)
The assets in this mod are licensed under [**CC-BY-SA 4.0**](https://creativecommons.org/licenses/by-sa/4.0/)

#ATTENTION
Make sure not to use OpenJDK to build the mod, it WILL break random things!

# Setting up a development workspace
1. Ensure that `Java` (found [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)), `Git` (found [here](http://git-scm.com/)) are installed correctly on your system and JDK8 is set as the default JAVA_HOME.
- Clone this repository into a new folder using your IDE, a git gui or the git command line.
- navigate to the folder using a commandline terminal and run the following command, depending on your IDE:
  - Eclipse: `gradlew setupDecompWorkspace eclipse`
  - IntelliJ IDEA: `gradlew setupDecompWorkspace idea`
- You can now open the project in your IDE
- **IntelliJ IDEA Only:** Due to a bug(?) in ForgeGradle, it generates invalid run configurations. To fix this, goto `run -> edit configurations`. You should see 2 configurations: one for the client and one for the server. Select one of the configurations and set the `Use classpath of module` to `chococraft3`

### Alternative IntelliJ IDEA Setup using the Gradle Import
IntelliJ IDEA supports running gradle commands from the IDE as well.
1. Ensure that `Java` (found [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)), `Git` (found [here](http://git-scm.com/)) are installed correctly on your system.
- Clone this repository into a new folder using your IDE, a git gui or the git command line.
- In IDEA, import the build.gradle from the chococraft project
- In the `Import Project from Gradle` Window, make sure you **disable** `Create separate module per source set` and select `use the default gradle wrapper (recommended)`
- In IDEA, goto `View -> Tool Windows -> Gradle`
- Expand the Node `Chococraf3 -> Tasks -> forgegradle` and execute the `setupDecompWorkspace` task via rightlick -> run (this may take a few minutes)
- When this task completes, we need to generate the run configurations, which you can do by executing the `genIntellijRuns` task in the same node
- Due to a bug(?) in ForgeGradle, it generates invalid run configurations. To fix this, goto `run -> edit configurations`. You should see 2 configurations: one for the client and one for the server. Select the client configuration and set the `Use classpath of module` to `chococraft3`. Repeat this for the server configuration as well.

# Credits
* EddieV for creating the first version of [Chococraft for Minecraft 1.1](http://www.minecraftforum.net/forums/search?by-author=EddieV&page=5&search-thread-id=1280466)
* ArnoSaxena for [creating Chococraft 2 for Minecraft 1.4+](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/1282382-1-6-x-1-5-x-forge-torojimas-chococraft-3-0-3). (Most of the Assets used in Chococraft 3 are done by him!)
* Clienthax for porting [Chococraft 2 to Minecraft 1.7.10 and 1.8](http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2269183-1-8-clienthaxs-chococraft-2-happiness-distilled)
The chocobo "kweh" sound, located under `src/main/resources/assets/chococraft/sounds/entities/chocobo/chocobo.ogg` was taken from Final Fantasy 7 and is property of [SquareEnix CO., LTD](http://www.square-enix.com/)