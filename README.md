# RandomMarket 🎯

A Minecraft **Random Market** plugin with GUI and Vault economy support.  
Built with **Java & Spigot/Paper API**, this plugin lets players open a random item shop using `/randommarket`.

---

## 🧰 Features
- Custom GUI-based random market  
- Automatically updating item prices  
- Vault economy integration  
- Configurable update interval  
- Easy to modify via `config.yml`

---

## ⚙️ Requirements
- Java **17+**
- Paper / Spigot **1.19 - 1.21**
- [Vault](https://www.spigotmc.org/resources/vault.34315/)
- *(Optional)* [EssentialsX](https://essentialsx.net/downloads.html)

---

## 🚀 Installation
1. Download the latest `RandomMarket.jar` from [Releases](../../releases)  
2. Place the `.jar` file into your server's `plugins/` folder  
3. Ensure you have Vault + an economy plugin installed  
4. Start your server  

---

## 💬 Commands
| Command | Description |
|----------|--------------|
| `/randommarket` | Opens the random market GUI |
| `/rmarket` | Alias for `/randommarket` |

---

## ⚡ Configuration
File: `plugins/RandomMarket/config.yml`

```yaml
update-interval: 10 # in minutes
prefix: "&7[&aRandomMarket&7] "
