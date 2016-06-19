package mycash;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import mycash.database.DataBase;
import mycash.listener.EventListener;

public class Main extends PluginBase {
	private DataBase db;
	private EventListener listener;
	
	@Override
	public void onEnable() {
		db = new DataBase(this);
		listener = new EventListener(this);
		
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return listener.onCommand(sender, command, label, args);
	}
	
	public DataBase getDB() {
		return db;
	}
}
