# InstantMarker

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/Ranzeplay/InstantMarker/build.yml)
![GitHub last commit](https://img.shields.io/github/last-commit/Ranzeplay/InstantMarker)
![GitHub issues](https://img.shields.io/github/issues/Ranzeplay/InstantMarker)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/Ranzeplay/InstantMarker)

## Description

A Minecraft Fabric Mod that can mark the position you are pointing to and broadcast to all other players.

## Usage

Install the mod, join a server or your single player world and press Z (you can change to other keys in Key Binding settings).

### Configuration

You can modify global configurations in mod menu.

Also, we provided a command system that you can change configurations temporarily in game.

### Commands

All in one root command: `/im`

#### Clear markers

To clear all markers you have created or received, type in the following command.

```
/im clear
```

#### Mute/Unmute

To mute a player's marking operation, you need to type this command and he/she will be added into ignore list. It's only ignored by your client,  but others can still see his/her markers.

```
/im mute <playerName>
```

To unmute a player, type this command and he/she would be removed from ignore list.

```
/im unmute <playerName>
```

#### Sound settings

To enable/disable sound notification when someone (include yourself) marks a position, type the following command

```
/im sound
```

#### Local mode

To share or disable sharing your markers to others, use the following command

```
/im local
```

#### Share detailed informations

To share more informations around the marker, there are many things you can do

```
/im share <items|...>
```

- `items`: Share items around the marker (10x10x6 blocks) to others.

## Dependencies

- oωo
  
  - Link: https://github.com/wisp-forest/owo-lib
  
  - Version requirement: >=0.8.2
  
  - Provides configuration management

- (Optional) Mod Menu
  
  - Link: https://github.com/TerraformersMC/ModMenu
  
  - Version requirement: >=4.0.5
  
  - Provides a global configurations panel
  
  

## License

This project is licensed under MIT License. You can view it in LICENSE in root directory.
