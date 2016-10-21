package mycash.cash;

import cn.nukkit.IPlayer;
import cn.nukkit.Server;
import mycash.database.DataBase;
import mycash.exception.PlayerNotHaveEnoughCashExeception;

public class Account {
	private IPlayer owner;
	
	public Account(String player) {
		this(Server.getInstance().getOfflinePlayer(player));
	}
	public Account(IPlayer player) {
		owner = player;
	}
	
	public IPlayer getOwner() {
		return owner;
	}
	
	public int getCash() {
		return DataBase.getInstance().getDB("cash").getInt(owner.getName().toLowerCase(), -1);
	}
	
	public void setCash(int cash) {
		if (cash < 0) throw new IllegalArgumentException("Cash must bigger than 0");
		DataBase.getInstance().getDB("cash").set(owner.getName().toLowerCase(), cash);
	}
	
	public void addCash(int cash) {
		DataBase.getInstance().getDB("cash").set(owner.getName().toLowerCase(), getCash() == -1 ? cash : getCash() + cash);
	}
	
	public void reduceCash(int cash) throws PlayerNotHaveEnoughCashExeception {
		int reduceAmount = getCash() - cash;
		if (reduceAmount < 0) throw new PlayerNotHaveEnoughCashExeception();
		DataBase.getInstance().getDB("cash").set(owner.getName().toLowerCase(), getCash() - cash);
	}
	
	@Override
	public boolean equals(Object obj) {
		return owner.getName().toLowerCase().equals(((Account) obj).owner.getName().toLowerCase());
	}
}
