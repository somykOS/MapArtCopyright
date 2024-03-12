# Map art Copyright
A small server side mod designed to note the author who created the map.
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

# Command /newAuthor <player> works if it's true, and you're one of the authors
authorsCanAddAuthors: true

# Allows to clean a map with a bucket of water in a cartography table
cleanMap: false
```

You can manage config in-game with the command `/mapartcopyright <config-value> true/false`. <br>
To use this command you need the permission `mapartcopyright.changeconfig`. <br>
(Alternatively, you can change it manually in `config/MapArtCopyright/config.yml`)

Additionally, there is a command — `/newAuthor`. It's visible and works only when `authorsCanAddAuthors` is `true`. <br>
As you can see, you can also disable map copying so nobody can copy it (except authors if `authorsCanCopy` is `true`). <br>
I've also added the ability to clean a map with a bucket of water on a cartography table. It won't work if `cleanMap` is `false`. 

***
It's my very first mod. Special thanks for hints to LlamaLad7, Zefir and Bawnorton. <br>
If there is any issue, please visit [GitHub source](https://github.com/somykOS/Fabric-MapArtCopyright-1.20.X) and open one.