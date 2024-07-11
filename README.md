# Map art Copyright
A small server side mod designed to note the author who created the map. Allows to disable map copying.
***
![How copyright looks](https://cdn.modrinth.com/data/oI6kpFqG/images/06b8185f18d6da844fa0b8c1ce2ded71dc2bfbde.png)
***
Default config values:
```yaml
# Adds NBT 'authors' when creating a new filled map
copyright: true

# Works if 'copyright' is/was 'true'. Up to 5 players can be displayed in a map lore
displayAuthorsLore: true

# Nobody can make a copy of a map (except authors if 'authorsCanCopy' is 'true')
disableCopy: false

# Works if 'copyright' is/was 'true'
authorsCanCopy: true

# Authors can use `/mapAuthor add <player>` command
authorsCanAddAuthors: true

# Allows to clean a map with a bucket of water in a cartography table
cleanMap: false
```

You can manage config in-game with the command `/mapartcopyright <config-value> true/false`. <br>
To use this command you need the permission `mapartcopyright.changeconfig`. <br>
(Alternatively, you can change it manually in `config/MapArtCopyright/config.yml`)

To use `/mapAuthor add <player>` you need to be one of the map authors or have the `mapartcopyright.addauthor` permission, and for `/mapAuthor remove <player>` you need to have the `mapartcopyright.removeauthor` permission.<br>

This mod uses [fabric-permission-api](https://github.com/lucko/fabric-permissions-api/). <br>
To manage these permission, you can use [LuckPerms](https://modrinth.com/mod/luckperms) or any other mod that can be used in this way. <br>

If you only want to block map copying, you can set all `false`, except `disableCopy`.<br>
`cleanMap` enables the ability to clean a map with a bucket of water on a cartography table.

***
It's my very first mod. Special thanks for hints to LlamaLad7, [Zefir](https://modrinth.com/user/Stikulzon) and [Bawnorton](https://modrinth.com/user/Bawnorton). <br>
If there is any issue, please visit the [GitHub page](https://github.com/somykOS/Fabric-MapArtCopyright-1.20.X/issues) and open one. <br>
And if you need this mod for another MC version, you can open a new discussion on the [GitHub page](https://github.com/somykOS/Fabric-MapArtCopyright-1.20.X/discussions).
