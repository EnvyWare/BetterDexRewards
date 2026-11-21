# BetterDexRewards [![Discord](https://img.shields.io/discord/831966641586831431)](https://discord.gg/7vqgtrjDGw)

BetterDexRewards allows you to design rewards for players that are related to dex completion.

# Installation Instructions
Head over to releases and grab the version that corresponds to the Minecraft server you are running:

For 1.16.5 - `BetterDexRewards-Forge-x.x.x-1.16.5.jar`

Install this Into your `/mods` folder.

# Configuration Instructions

Default Config
```
database: # sql database details
    pool-name: BetterDexRewards
    ip: 0.0.0.0
    port: 3306
    username: admin
    password: password
    database: servername
config-interface: # GUI information
    title: BetterDexRewards # gui name
    height: 6 # gui height
    fill-type: BLOCK # Fill style (https://github.com/Pixelmon-Development/API/wiki/GUI-Settings)
    filler-items:
        one: # the background item
            type: minecraft:stained_glass_pane
            amount: 1
            damage: 15
            name: ' '
            lore: []
reward-stages: # The stages players get rewards at
    one:
        x-pos: 1 # gui pos of the display item
        y-pos: 1 # gui pos of the display item
        display-item: # display item normally
            type: minecraft:stained_glass_pane
            amount: 1
            damage: 12
            name: This is 1%
            lore: []
        complete-item: # display item when complete
            type: minecraft:stained_glass_pane
            amount: 1
            damage: 12
            name: Complete
            lore: []
        to-claim-item: # Display item when needs claiming
            type: minecraft:stained_glass_pane
            amount: 1
            damage: 12
            name: Claim me!
            lore: []
        required-percentage: 1.0 # Percentage required to complete this rank
        reward-commands: # The commands performed upon claiming
        - give %player% minecraft:diamond 1
        reward-messages: # The messages sent upon claiming
        - '&e&l(!) &eYou have completed 1% of the dex!'
claim-reminder-message: # Message sent to remain the player they've not claimed their reward yet
- '&e&l(!) &eYou have a PokeDex reward level you can claim!'
claim-update-message: # Message sent upon a new level being able to be claimed
- '&e&l(!) &eYou have a new PokeDex reward level you can claim!'
```
Config Explanation:

Database settings (**This is required for this to function! If you are hosting through a hosting company, you usually get MySQL Databases included**)

 - Pool-name - Can be left as Is, only change If storing multiple server's ForgeEconomies on the same database server
 - IP - IP Address of your MySQL database
 - Port - Port of your MySQL database (Default: 3306)
 - Username - Username of your MySQL database
 - Password - Password of your MySQL database
 - Database - Database name of your MySQL database
 
 Reward Stages are defined in the next section
 
 ```
     one:
        x-pos: 1 # gui pos of the display item
        y-pos: 1 # gui pos of the display item
        display-item: # display item normally
            type: minecraft:stained_glass_pane
            amount: 1
            damage: 12
            name: This is 1%
            lore: []
        complete-item: # display item when complete
            type: minecraft:stained_glass_pane
            amount: 1
            damage: 12
            name: Complete
            lore: []
        to-claim-item: # Display item when needs claiming
            type: minecraft:stained_glass_pane
            amount: 1
            damage: 12
            name: Claim me!
            lore: []
        required-percentage: 1.0 # Percentage required to complete this rank
        reward-commands: # The commands performed upon claiming
        - give %player% minecraft:diamond 1
        reward-messages: # The messages sent upon claiming
        - '&e&l(!) &eYou have completed 1% of the dex!'
```

You can copy and paste the block above however you will need to change some stuff as a bare minimum for things to work with each tier you create.
- one (change this to something different for each tier you would like)
- x-pos
- y-pos
- required-percentage

You should also update the following with each tier you create
- name (in display-item)
- reward-commands
- reward-messages

# Note about 1.16
Damage is no longer used in 1.16 so you will need to update the `display-item`, `complete-item`, and `to-claim-item`. This can simply be fixed by removing the damage line entirely and then adding the color you want before stained_glass_pane. For example, `red_stained_glass_pane`

# Requirements and bonuses (1.21.1)
Each reward stage is its own file in `config/BetterDexRewards/rewards/`. On top of `required-dex`, a stage can list:

- `requirements`: hard gates. Every one must be met to claim the stage, and `failed-message` is sent for each that isn't.
- `bonuses`: optional extras. While the player meets one, the stage needs `dex-reduction` less dex (a count for `id: count` stages, percentage points for `id: percentage` stages, never below 0), and its `rewards` are paid once: with the stage claim, or later by clicking the claimed stage again once the bonus is met.

Both use `type` and `data` the same way: `permission` is built in (data is the permission node), and other mods can register more (StardustCORE registers `gym`). An unknown type is never met.

```yaml
required-dex:
  id: count
  count: 150.0
bonuses:
- id: agni
  type: gym
  data: agni
  dex-reduction: 10.0
  description: '&eBeat the Agni gym'
  rewards: # same shape as the stage's own rewards
    guaranteed-reward:
      commands:
      - give %player% minecraft:emerald 1
      messages:
      - '&e&l(!) &eYou earned the Agni bonus!'
```

Stage items can use `%required_dex%` (the threshold after the player's bonuses), `%base_dex%` (as configured) and `%bonuses%`, which must be a lore line of its own and becomes one line per bonus, formatted by `bonus-lines` in `config.yml`.
