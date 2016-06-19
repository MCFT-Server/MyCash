package mycash.database;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import mycash.Main;

public class DataBase extends BaseDB<Main> {

	@SuppressWarnings("serial")
	public DataBase(Main plugin) {
		super(plugin);
		setPrefix("[캐쉬]");
		initDB("cash", plugin.getDataFolder().getPath() + "/cash.json", Config.JSON);
		initDB("config", plugin.getDataFolder().getPath() + "/config.yml", Config.YAML, new ConfigSection() {{
			put("default-cash", 0);
		}});
	}

}
