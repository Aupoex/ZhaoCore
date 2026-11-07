# 巅峰配件

目标环境：Minecraft 1.20.1 / Forge / TaCZ 1.1.8-hotfix。

所有有效 TaCZ 枪械默认新增一个 `apex_attachments` 槽位。它有独立存储，使用 TaCZ 原有的安装、拆卸、同步和属性缓存机制。槽位中文名为“巅峰配件”，空槽使用本模组自带的占位图片；选中时使用枪械概览视角。安装后的配件不在枪械上渲染。

图标文件位于 `src/main/resources/assets/zhaocore/textures/gui/apex_attachments.png`，当前是复制进项目的 TaCZ 军事垂直握把图片，尺寸为 **64×64 PNG**。以后直接替换这个文件并重新构建即可，无需修改 Java 代码；替换图也使用 64×64。其他类型的槽位保持原来的图标，不可安装状态仍显示 TaCZ 的禁用标识。

具体配件仍通过 TaCZ 的适配标签限制安装对象。新增槽位不意味着所有巅峰配件都能安装到所有枪上。

以下文件放入已有 TaCZ 枪包即可添加一个伤害增加 3 的示例配件。示例 ID 为 `zhaocore:apex_damage`，不是槽位类型 ID。

`data/zhaocore/index/attachments/apex_damage.json`：

```json
{
  "name": "zhaocore.attachment.apex_damage.name",
  "type": "apex_attachments",
  "data": "zhaocore:apex_damage_data",
  "display": "zhaocore:apex_damage_display"
}
```

`data/zhaocore/data/attachments/apex_damage_data.json`：

```json
{
  "damage": {
    "addend": 3
  }
}
```

`assets/zhaocore/display/attachments/apex_damage_display.json`：

```json
{
  "slot": "zhaocore:gui/apex_attachments"
}
```

无需配件模型和材质；示例物品图标同样引用 ZhaoCore 自带的图片。属性数据使用 TaCZ 原有格式，可以按需配置伤害、后坐力、精度等修正。具体配件的物品图标仍由各自的 `display.slot` 决定。

`assets/zhaocore/lang/zh_cn.json` 中加入：

```json
{
  "zhaocore.attachment.apex_damage.name": "巅峰伤害配件"
}
```

给 M4A1 开放该配件，在 `data/tacz/tacz_tags/attachments/allow_attachments/m4a1.json` 的数组中加入 `"zhaocore:apex_damage"`。也可以在另一个枪包的同名路径提供数组：TaCZ 会合并适配标签。

```json
[
  "zhaocore:apex_damage"
]
```

完成后重载枪包，使用 `/give @s tacz:attachment{AttachmentId:"zhaocore:apex_damage"}` 获取配件，手持 M4A1 打开 TaCZ 改装界面，选择“巅峰配件”进行安装。

# 其他功能调整

- 火焰抗性基础值为 1，火焰伤害倍率为 `max(0, 2 - 抗性)`；抗性达到 2 时提前取消攻击。抗火药水仍保留原有每级 +0.2 属性的设置。
- 爆头率、被爆头率、爆头转换的注册与事件处理已注释停用，原代码保留。
- 末影迁移的 `EnderReachEventsMixin` 和范围挖掘中的同类入口 `RangeDiggingEnchantmentMixin` 已从配置停用，源码保留；`ender_reach` 不再自动添加给玩家。
- `rank`、`intensity` 继续注册，`AttributeSupplierBuilderMixin` 已停用，不再自动添加给实体。
- 解除 TaCZ 趴下时上仰 45°、下俯 30° 的额外输入限制，保留开镜灵敏度计算和 Minecraft 原有视角范围。
- 原本未启用的 `AttributeSetEffectMixin`、`ConditionalAttributeSetEffectMixin`、`DetonationEffectMixin` 保持未启用。

# 开发验证

`./gradlew.bat runGameTestServer` 运行 `src/test` 中的游戏内回归测试，覆盖属性注册与挂载、火焰伤害（含抗火药水）、配件 JSON、安装、NBT 保存与重载、伤害修正缓存和拆卸。测试代码与测试配件不会进入发布 JAR。

如果已有完整依赖缓存、只想离线验证，可运行：

```powershell
./gradlew.bat --offline -Dnet.minecraftforge.gradle.check.certs=false -x downloadAssets runGameTestServer
```

其中证书参数仅跳过 ForgeGradle 的启动联网探测，`-x downloadAssets` 跳过服务器测试不需要的客户端音效下载。

`./gradlew.bat runData` 同时执行开发用客户端检查，通过 Forge 的 CLIENT 数据启动器验证客户端 JSON、槽位图标分支和趴下俯仰 Mixin，不打开游戏窗口。这不替代改装界面的手动视觉检查。

开发依赖中的 Gunsmith Lib 已更新至适配 TaCZ 1.1.8 的 6.4.4；原先的 5.0.0 会在客户端注入 `GunDisplayInstance` 时崩溃。实际游戏环境也应使用匹配版本。

AMR（Apotheosis Modern Ragnarok）开发依赖已更新为适配 TaCZ 1.1.8 的 [Forge 7.0.0](https://www.curseforge.com/minecraft/mc-mods/apotheosis-modern-ragnarok/files/8056882)。原先 6.0.0 面向 TaCZ 1.1.7，其枪声处理会将 `EntityTrackingGunSoundInstance` 强转为 `EntityBoundSoundInstanceAccessor`，导致掏枪时崩溃。实际游戏环境需要移除 AMR 6.0.0 并换为 7.0.0；仅替换 ZhaoCore JAR 无法更新外部模组。ZhaoCore 的依赖声明会拒绝已安装的旧版 AMR。

客户端开发检查现包含 AMR 枪声入口，验证无魔法词缀枪械的掏枪、射击、换弹声音 ID 原样返回，以及缺失声音时保持为空，并加载实际声音播放目标以检查 Mixin 应用。

此前在 AMR 6.0.0 中观察到缓存的伤害来源保留攻击失败状态：某次火焰攻击被取消后，复用同一来源的后续攻击可能被它额外免疫。回归测试使用独立伤害来源隔离各个抗性用例；该测试隔离措施保留。
