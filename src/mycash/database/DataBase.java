package mycash.database;

import java.io.File;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import mycash.Main;

public class DataBase extends BaseDB<Main> {
	
	private static DataBase instance;

	@SuppressWarnings("serial")
	public DataBase(Main plugin) {
		super(plugin);
		initMessage();
		initDB("cash", new File(plugin.getDataFolder(), "cash.json"), Config.JSON);
		initDB("waitlist", new File(plugin.getDataFolder(), "waitlist.json"), Config.JSON);
		initDB("config", new File(plugin.getDataFolder(), "config.yml"), Config.YAML, new ConfigSection() {{
			put("default-cash", 0);
		}});
		initDB("permlist", new File(plugin.getDataFolder(), "permlist.json"), Config.JSON);
		initDB("log", new File(plugin.getDataFolder(), "log.json"), Config.JSON);
		
		registerCommands();
		if (instance == null) instance = this;
	}
	private void registerCommands() {
		registerCommand(get("commands-cash"), get("commands-cash-description"), get("commands-cash-usage"), "mycash.commands.cash.*");
		registerCommand(get("commands-managecash"), get("commands-managecash-description"), get("commands-managecash-usage"), "mycash.commands.managecash.*");
	}

	public static DataBase getInstance() {
		return instance;
	}
}
