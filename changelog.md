# Version 0.9.1 - beta
- fix chocobos not able to dive when having the dive ability

# Version 0.9.0 - beta
- Breeding chocobos with a lovely gysahl green will no longer directly spawn a baby chocobo. Instead, the mother chocobo will place an egg on the ground.
  To hatch this egg, it needs to be picked up and placed in a Chocobo Nest.
- The color of a chocobo can now be changed using dyes. Supported are yellow, lime, blue, white, black, ping, red, purple. (The full MC color range will be added in a future update)
- Added Stamina stat to chocobos that is displayed as a bar of lighting strikes when mounted. Stamina is used for special chocobo abilities and
  regenerates slowly. Standing still increases the regeneration rate slighly.
- Added 4 chocobo abilities that are granted to a chocobo when breeding (really low chance) or by using rare dungeon loot fruits:
  - **Spike Fruit**: Teaches the **Sprint** ability. Allows the chocobo to run 200% faster than normal while holding the sprint key until stamina is exhausted.
  - **Aeroshroom**: Teaches the **Glide** ability. Allows the chocobo to slow glide to the ground. (Disabled while holding the *sneak* button)
  - **Aqua Berry**: Teaches the **Dive** ability. Allows the chocobo to dive underwater. Also grants water breathing.
  - **Dead Pepper**: Teaches the **Fly** ability. Allows the chocobo to fly. Requires a high amount of stamina.
- The chococraft sourcecode is now licensed under the MIT license. Most of the assets are licensed under CC-BY-SA. See the appropriate license document or the readme on github for further information.
- Added chocopedia, which can be used on a chocobo to view its stats. The stats on the screen are (left to right, top to bottom [sorry, tooltips will come later]):
  - Gender
  - Max Health
  - Speed
  - Max Stamina
  - Can Sprint
  - Can Glide
  - Can Dive
  - Can Fly
- Added pickled gysahl green as another food source for players

### Known Issues
- *Chocobos dont take fall damage*
- *Statscaling might be inbalanced. No balance testing has been done yet.*
- *Server consoles might spam "Chocobo moved wrongly" when moving quickly across sloped terrain*

# Version 0.2.1 - alpha
- fix chocobos eating saddles when being rightclicked with one in hand.
  I dont want to force chocobos onto a certain diet, but i dont think
  leather is very nutritious and therefore should be avoided.

# Version 0.2 - alpha
- First release
- Chocobos spawn randomly in the overworld (and maybe other dimensions)
- Gysahl Green crop worldgen
- Temporary breeding system: Use two *lovely gysahl greens* to breed two chocobos.
  The offspring will be of a random color. (This system will change in a future release!)
- Chocobos can be tamed using *gysahl green* (15% chance per green used)
- Tamed Chocobos can be ridden using the chocobo saddle
- special saddles with inventories attached are implemented but not fully tested. You might lose your items!
  (Recipes are missing because of this)
- *Traits and Abilities are currently not implemented.*