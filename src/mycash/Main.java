package mycash;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import mycash.database.DataBase;
import mycash.listener.EventListener;
import mycash.manager.CashManager;

public class Main extends PluginBase {
	private DataBase db;
	private EventListener listener;
	private CashManager manager;
	
	@Override
	public void onEnable() {
		db = new DataBase(this);
		listener = new EventListener(this);
		manager = new CashManager();
		
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return listener.onCommand(sender, command, label, args);
	}
	
	public DataBase getDB() {
		return db;
	}
	public CashManager getCashManager() {
		return manager;
	}
}
