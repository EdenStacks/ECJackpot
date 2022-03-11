# 💰 ECJackpot V1.0.0 💰

Create jackpot using items or your server currency.

Tested on paper 1.18.1.

## Dependency

- SmartInvs 1.2.7

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
reward-commands-global:
  - "pinata spawn place"
  - "broadcast Une piñata va apparaitre sur la place !"

# Command that will be executed for each participant (ONLY).
# If a participant is offline, commands will be executed at his next connection.
reward-commands-each-participant:
  - "say Tu as reçu 100 pièces suite à la fin de la cagnotte &dPinata&eParty &rMerci d'avoir participé."
  - "eco give {name} 100"

# You can choose between two options:
# • MONEY : Player pay with their money.
# • ITEM : Player pay with specific item.
# If you choose ITEM don't forget to uncomment the section below !
currency-type: ITEM
# Uncomment/(comment) this if the currency-type is (not) set to ITEM !
material: STONE
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
    # • {pot_content} : display actual value of the pot.
    # • {pot_max} : display max value of the pot.
    jackpot-information: |-
      &fProgression du jackpot:
      &f{progress_bar} &7(&6{progress_percentage} &7/ &a100%&7)
      &c{pot_content} &7/ &c{pot_max}

    # These lines will be written in the item (the left skull) lore of the menu.
    # • {name} : display the name of the last participant of the jackpot.
    # • {amount} : display the amount of his last participation.
    # • {currency_name} : display the name of the currency used (it can be money or an in-game item).
    # • {since} : display the time elapsed since his last participation.
    last-participant: '&d{name} &ea donné &6{amount} &c{currency_name} &eil y a &b{time_elapsed} &eminute(s).'

    # These lines will be written in the item (the right skull) lore of the menu.
    # • {name} : display the name of the last participant of the jackpot.
    # • {amount} : display the amount of his last participation.
    # • {currency_name} : display the name of the currency used (it can be money or an in-game item).
    best-participant: '&d{name} &ea donné un total de &6{amount} &c{currency_name} &e!'

    # These lines will be written in the item (paper) lore of the menu.
    # • {name_[1-10]} : The name of a player. Number is used to get a player at position x in the
    #                   top sorted by participation amount. Return "✖" if given number is out of range.
    # • {amount_[1-10]} : Participation amount of the player ranked at x position. Return an empty string
    #                     if no player at this position.
    list-participant: |-
      &f• {name_1} {amount_1}
      &f• {name_2} {amount_2}
      &f• {name_3} {amount_3}
      &f• {name_4} {amount_4}
      &f• {name_5} {amount_5}
      &f• {name_6} {amount_6}
      &f• {name_7} {amount_7}
      &f• {name_8} {amount_8}
      &f• {name_9} {amount_9}
      &f• {name_10} {amount_10}

    # These lines will be written in the item (left book) lore of the menu.
    jackpot-rules: |-
      &fLorem ipsum dolor sit amet, consectetur adipiscing elit.
      &fProin facilisis metus et massa sagittis egestas.
      &fProin id magna id dui semper imperdiet et sed elit.
      &fPraesent at dui at eros lobortis porta bibendum ac nulla.
      &fMauris eu lorem dapibus, efficitur massa ut, tristique purus.

    # These lines will be written in the item (right book) lore of the menu.
    jackpot-reward: |-
      &fLorem ipsum dolor sit amet, consectetur adipiscing elit.
      &fProin facilisis metus et massa sagittis egestas.
      &fProin id magna id dui semper imperdiet et sed elit.
      &fPraesent at dui at eros lobortis porta bibendum ac nulla.
      &fMauris eu lorem dapibus, efficitur massa ut, tristique purus.
```

</details>

## Permissions

- `ecjackpot.command.<jackpot_command>` Allow to open a jackpot menu.

## Authors

- [@LudovicAns](https://github.com/LudovicAns)

[![Logo](https://i.imgur.com/QXuYtex.png)](https://github.com/EdenStacks)

