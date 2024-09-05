# Map art Copyright
A small server side mod designed to note the author who created the map. Allows to disable map copying.
***
![How copyright looks](https://cdn.modrinth.com/data/oI6kpFqG/images/06b8185f18d6da844fa0b8c1ce2ded71dc2bfbde.png)
***
Default config values:
```yaml
# Adds NBT 'authors' when creating a new filled map
copyright: true

# Nobody can make a copy of a map (except authors if 'authorsCanCopy' is 'true')
disableCopy: false

# Works if 'copyright' is/was 'true'
authorsCanCopy: true

# Allows to clean a map with a bucket of water in a cartography table
cleanMap: false
```

You can manage config in-game with the command `/map-art config <config-value> true/false`. <br>
To use this command you need the permission `map-art-copyright.change-config`. <br>
(Alternatively, you can change it manually in `config/mapartcopyright.yml`)

To use `/map-art add <player>` or `/map-art remove <player>` you need to be main author (the first who created filled map) or have the `map-art-copyright.add-author`, `map-art-copyright.remove-author` permissions. <br>

This mod uses [fabric-permission-api](https://github.com/lucko/fabric-permissions-api/). <br>
To manage these permission, you can use [LuckPerms](https://modrinth.com/mod/luckperms) or any other mod that can be used in this way. <br>

If you only want to block map copying, you can set all `false`, except `disableCopy`.<br>
`cleanMap` enables the ability to clean a map with a bucket of water on a cartography table.

---
You can visit my little [contact card](https://somykos.github.io/web/), <br>
And are welcome to support me via the following links:<br>
<a href="https://ko-fi.com/somyk">
<img src="https://raw.githubusercontent.com/somykOS/web/c03742bd86ca2ce0f6f39bcd3cfe683ad98926a2/public/external/kofi_s_logo_nolabel.svg" alt="ko-fi" width="100"/>
</a>
<a href="https://send.monobank.ua/jar/8RCzun35pC">
<img src="https://raw.githubusercontent.com/somykOS/web/5ac2e685429eb0cc369dc220ce3b93d2a22893c0/public/external/monobank_logo.svg" alt="monobank" width="80"/>
</a>
