# 💰 ECJackpot V1.0.0 💰

Create jackpot using items or your server currency.

Tested on paper 1.18.1.


## Dependency
- WIP


## Configuration

You can change different parameters from all configuration files.
<details>
    <summary>config.yml</summary>

```
# You can choose by default between fr and en.
language: fr
```
</details>

<details>
    <summary>pinata.yml</summary>

```
# The display name of the jackpot. It also used in jackpot menu.
display-name: "&dPinata&eParty"

# Command that server will execute when the jackpot is full.
reward-command: "/pinata spawn place"

# You can choose between two options:
# • MONEY : Player pay with their money.
# • ITEM : Player pay with specific item.
# If you choose ITEM don't forget to uncomment the section below !
currency-type: MONEY

# Uncomment this if the currency-type is set to ITEM !
# material: STONE

# Amount of your currency is needed to fill the jackpot.
amount-needed: 10000
 #=====================================#
 #                                     #
 #             MENU SECTION            #
 #                                     #
 #=====================================#
 menu:
   # Command that open the jackpot menu.
   command-name: "pinataparty"
   
   # These lines will be written in the item (cauldron) lore of the menu.
   # You can use :
   # • {progress_bar} : display a progression bar like this ||||||||||||| with color to see progress.
   # • {progress_percentage} : display progression in percentage (ex: 78.46%)
   jackpot-information: |-
     &fProgression du jackpot:
     {progress_bar} &7(&6{progress_percentage} &7/ &a100%&7)
   
   # These lines will be written in the item (the left skull) lore of the menu.
   # • {name} : display the name of the last participant of the jackpot.
   # • {amount} : display the amount of his last participation.
   # • {currency_name} : display the name of the currency used (it can be money or an in-game item).
   # • {since} : display the time elapsed since his last participation.
   last-participant: |-
     {name} a donné {amount} {currency_name} il y a {time_elapsed} minute(s).
   
   # These lines will be written in the item (the right skull) lore of the menu.
   # • {name} : display the name of the last participant of the jackpot.
   # • {amount} : display the amount of his last participation.
   # • {currency_name} : display the name of the currency used (it can be money or an in-game item).
   best-participant: |-
     {name} a donné un total de {amount} {currency_name} !
   
   # These lines will be written in the item (paper) lore of the menu.
   # • {name_<[1-10]>} : The name of a player. Number is used to get a player at position x in the
   #                     top sorted by participation amount. Return "X" if given number is out of range.
   list-participant: |-
     • {name_1}
     • {name_2}
     • {name_3}
     • {name_4}
     • {name_5}
     • {name_6}
     • {name_7}
     • {name_8}
     • {name_9}
     • {name_10}
   
   # These lines will be written in the item (left book) lore of the menu.
   jackpot-rules: |-
     Lorem ipsum dolor sit amet, consectetur adipiscing elit.
     Proin facilisis metus et massa sagittis egestas.
     Proin id magna id dui semper imperdiet et sed elit.
     Praesent at dui at eros lobortis porta bibendum ac nulla.
     Mauris eu lorem dapibus, efficitur massa ut, tristique purus.
   
   # These lines will be written in the item (right book) lore of the menu.
   jackpot-reward: |-
     Lorem ipsum dolor sit amet, consectetur adipiscing elit.
     Proin facilisis metus et massa sagittis egestas.
     Proin id magna id dui semper imperdiet et sed elit.
     Praesent at dui at eros lobortis porta bibendum ac nulla.
     Mauris eu lorem dapibus, efficitur massa ut, tristique purus.
```
</details>


## Permissions
- `quickshoplimiter.command.<jackpot_command>` Allow to open a jackpot menu.

## Authors

- [@LudovicAns](https://github.com/LudovicAns)


[![Logo](https://i.imgur.com/QXuYtex.png)](https://github.com/EdenStacks)

