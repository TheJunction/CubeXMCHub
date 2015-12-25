package net.CubeXMC.Hub;

import java.util.ArrayList;
import java.util.UUID;

public class HeadHunt {

	private final UUID uuid;
	private final ArrayList<String> collectedSkulls = new ArrayList<>();
	private final ArrayList<String> achievements = new ArrayList<>();
	//private int Coins;

	public HeadHunt(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUUID() {
		return uuid;
	}

	public ArrayList<String> collectedSkulls() {
		return this.collectedSkulls;
	}

	public boolean collectSkull(String name) {
		if (!collectedSkulls.contains(name)) {
			collectedSkulls.add(name);

			return true;
		}
		return false;
	}

	public ArrayList<String> getAchievements() {
		return achievements;
	}

	public void addAche(String achie) {
		if (!achievements.contains(achie)) {
			achievements.add(achie);
		}
	}

	/*public int getCoins() {
		return Coins;
	}

	public void setCoins(int coins) {
		Coins = coins;
	}*/

}