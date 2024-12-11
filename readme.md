# BiliCraft18th bugfix
为了减少对其他插件的侵入性，开发一个新插件调用其他插件的API来修复bug。
## 1. Oraxen家具领地放置检测
Oraxen家具放置时，无视领地放置权限。支持的领地插件：
- [Factions](https://www.spigotmc.org/resources/saberfactions-1-8-1-21-x-discord-gg-saber-the-complete-factions-solution.69771/)
- [Residence](https://www.spigotmc.org/resources/residence-1-7-10-up-to-1-21.11480/)

## 2. Residence领地wspeed1/wspeed2 bug
玩家出领地后，速度强制设置为`0.2F`（默认速度），导致AuraSkills的速度增益失效。新增`aspeed1`flag解决此问题，`aspeed1`关联Residence设置中的`WalkSpeed.'1'`的值，进/出领地时加/减玩家的速度，而不是set。

## 3. FlagsH旗帜在领地放置/破坏/放大检测
