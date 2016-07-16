package mycash.database;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import mycash.Main;

public class DataBase extends BaseDB<Main> {
	
	private static DataBase instance;

	@SuppressWarnings("serial")
	public DataBase(Main plugin) {
		super(plugin);
		initMessage();
		initDB("cash", plugin.getDataFolder().getPath() + "/cash.json", Config.JSON);
		initDB("config", plugin.getDataFolder().getPath() + "/config.yml", Config.YAML, new ConfigSection() {{
			put("default-cash", 0);
		}});
		
		registerCommands();
		if (instance == null) instance = this;
	}
	private void registerCommands() {
		registerCommand(get("commands-cash"), get("commands-cash-description"), get("commands-cash-usage"), "mycash.commands.cash");
		registerCommand(get("commands-managecash"), get("commands-managecash-description"), get("commands-managecash-usage"), "");
	}

	public static DataBase getInstance() {
		return instance;
	}
}
