package com.ofek2608.dnd.impl.pet;

import com.ofek2608.dnd.api.player.Player;
import com.ofek2608.dnd.api.player.PlayerView;
import com.ofek2608.dnd.api.player.pet.PlayerPet;
import com.ofek2608.dnd.api.player.pet.PlayerPets;
import com.ofek2608.dnd.data.Data;
import com.ofek2608.dnd.data.pet.PetFoodResult;
import com.ofek2608.dnd.data.pet.PetKind;
import com.ofek2608.dnd.data.pet.Trap;
import com.ofek2608.gim.Gim;
import com.ofek2608.gim.GimBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public final class PlayerPetImpl implements PlayerPet {
	private final Player player;
	private final PlayerPets pets;
	private final int index;

	@Nullable private Trap trap;
	private long captureTime, breakTime;
	@Nullable private PetKind kind;
	private boolean tame;
	private float health, hunger, love;
	private String name = "";
	private long nextHunt = -1;
	private long lastUpdate;

	public PlayerPetImpl(Player player, PlayerPets pets, int index) {
		this.player = player;
		this.pets = pets;
		this.index = index;
	}

	@Override
	public void save(GimBuilder builder) {
		if (trap != null) builder.child("trap").set(trap.path);
		builder.child("captureTime").set(captureTime);
		builder.child("breakTime").set(breakTime);
		if (kind != null) builder.child("kind").set(kind.path);
		builder.child("health").set(health);
		builder.child("hunger").set(hunger);
		builder.child("love"  ).set(love  );
		builder.child("name").set(name);
		builder.child("nextHunt").set(nextHunt);
		builder.child("lastUpdate").set(lastUpdate);
	}

	@Override
	public void load(Gim data) {
		trap = Data.get(data.get("trap").valueString);
		captureTime = data.get("captureTime").getLong();
		breakTime = data.get("breakTime").getLong();
		kind = Data.get(data.get("kind").valueString);
		health = data.get("health").getFloat();
		hunger = data.get("hunger").getFloat();
		love   = data.get("love"  ).getFloat();
		name = data.get("name").getString("");
		nextHunt = data.get("").getLong(-1);
		lastUpdate = data.get("lastUpdate").getLong();

	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public @Nullable Trap getTrap() {
		return trap;
	}

	@Override
	public long captureTime() {
		return captureTime;
	}

	@Override
	public long breakTime() {
		return breakTime;
	}

	@Override
	public @Nullable PetKind getKind() {
		return kind;
	}

	@Override
	public boolean isTame() {
		return tame;
	}

	@Override
	public float getHealth() {
		return health;
	}

	@Override
	public float getHunger() {
		return hunger;
	}

	@Override
	public float getLove() {
		return love;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public long getNextHunt() {
		return nextHunt;
	}

	@Override
	public long getLastUpdate() {
		return lastUpdate;
	}

	@Override
	public void update() {
		if (kind == null)
			return;

		long currentTime = System.currentTimeMillis();

		if (trap != null) {
			if (0 <= captureTime && captureTime <= currentTime) {
				sendMessagePetCapture();
				captureTime = -1;//no need to update anymore
			}
			if (breakTime <= currentTime) {
				sendMessagePetEscape();
				discard();
			}
			return;
		}

		Random r = new Random();

		long timeDiff = currentTime - lastUpdate;

		long workedUntil = currentTime;

		if (kind.healthChangeRate < 0)
			workedUntil = Math.min(workedUntil, (long) (-health / kind.healthChangeRate));
		if (kind.hungerChangeRate < 0)
			workedUntil = Math.min(workedUntil, (long) (-hunger / kind.hungerChangeRate));
		if (kind.loveChangeRate   < 0)
			workedUntil = Math.min(workedUntil, (long) (-love   / kind.loveChangeRate  ));

		if (this.health == 0 || this.hunger == 0 || this.love == 0)
			workedUntil = lastUpdate;

		this.health = this.health == 0 ? 0 : Math.max(kind.healthChangeRate * timeDiff, 0);
		this.hunger = this.hunger == 0 ? 0 : Math.max(kind.hungerChangeRate * timeDiff, 0);
		this.love   = this.love   == 0 ? 0 : Math.max(kind.loveChangeRate   * timeDiff, 0);

		if (nextHunt >= 0 && kind.hunting != null) {
			int watchdog = 4;

			while (nextHunt < workedUntil) {
				String hunted = kind.hunting.get(r);
				player.getData().getInventory().addItem(hunted, 1);
				sendMessageHunted(hunted);
				nextHunt += randHuntTime(r);

				if (--watchdog <= 0 && nextHunt < workedUntil) {
					nextHunt = watchdog;
					break;
				}
			}
		}

		lastUpdate = currentTime;
	}

	private long randHuntTime(Random r) {
		return kind == null ? 0 : (long) (r.nextExponential() * kind.huntTime);
	}


	@Override
	public void setTrap(Trap trap, Random r) {
		if (this.trap != null || this.kind != null)
			return;

		this.kind = Data.getNonnull(trap.possiblePets.get(r));
		this.trap = trap;
		this.tame = false;
		this.health = kind.maxHealth / 10;
		this.hunger = kind.maxHunger / 10;
		this.name = PetNames.get(new Random());
		this.nextHunt = -1;

		long currentTime = System.currentTimeMillis();

		this.captureTime = currentTime + (long) (r.nextExponential() * trap.captureTime);
		this.breakTime = captureTime + (long) (r.nextExponential() * trap.breakTime * kind.breakTimeModifier);
	}

	@Override
	public void openTrap() {
		if (trap == null)
			return;
		long time = System.currentTimeMillis();
		if (time < captureTime)
			return;
		if (breakTime <= time) {
			discard();
			return;
		}

		trap = null;
		lastUpdate = time;
	}

	@Override
	public void feed(String item) {
		update();
		if (trap != null || kind == null)
			return;

		PetFoodResult feedResult = kind.food.get(item);

		float newHealth = health + feedResult.health;
		float newHunger = hunger + feedResult.hunger;
		float newLove   = love   + feedResult.love  ;

		if (newHealth < 0 || newHunger < 0 || newLove < 0)
			return;

		health = Math.min(newHealth, kind.maxHealth);
		hunger = Math.min(newHunger, kind.maxHunger);
		love   = Math.min(newLove  , 1             );

		if (health == kind.maxHealth && hunger == kind.maxHunger && love == 1)
			tame = true;
	}

	@Override
	public void discard() {
		if (kind == null)
			return;
		trap = null;
		kind = null;
		if (pets.getGuardIndex () == index) pets.setGuardIndex (-1);
		if (pets.getAssistIndex() == index) pets.setAssistIndex(-1);
	}


	@Override
	public void guard() {
		if (kind == null || !tame) return;

		if (pets.getGuardIndex () == index) pets.setGuardIndex (index);
		if (pets.getAssistIndex() == index) pets.setAssistIndex(-1);
		nextHunt = -1;
	}

	@Override
	public void assist() {
		if (kind == null || !tame) return;

		if (pets.getGuardIndex () == index) pets.setGuardIndex (-1);
		if (pets.getAssistIndex() == index) pets.setAssistIndex(index);
		nextHunt = -1;
	}

	@Override
	public void hunt() {
		if (kind == null || !tame) return;

		if (pets.getGuardIndex () == index) pets.setGuardIndex (-1);
		if (pets.getAssistIndex() == index) pets.setAssistIndex(-1);
		nextHunt = System.currentTimeMillis() + randHuntTime(new Random());
	}

	@Override
	public void retire() {
		if (kind == null || !tame) return;

		if (pets.getGuardIndex () == index) pets.setGuardIndex (-1);
		if (pets.getAssistIndex() == index) pets.setAssistIndex(-1);
		nextHunt = -1;
	}

	private void sendMessagePetCapture() {//TODO check if the message look nice
		PlayerView.Context ctx = new PlayerView.Context(player);
		player.getMessage().privateMessage(
				ctx.t("private_message.capture.title"),
				ctx.t("private_message.capture.description")
						.replaceAll("%p", kind == null ? "" : kind.name),
				0x44EE66
		);
	}

	private void sendMessagePetEscape() {//TODO check if the message look nice
		PlayerView.Context ctx = new PlayerView.Context(player);
		player.getMessage().privateMessage(
				ctx.t("private_message.escape.title"),
				ctx.t("private_message.escape.description")
						.replaceAll("%p", kind == null ? "" : kind.name),
				0xEE6644
		);
	}

	private void sendMessageHunted(String hunted) {//TODO check if the message look nice
		PlayerView.Context ctx = new PlayerView.Context(player);
		player.getMessage().privateMessage(
				ctx.t("private_message.hunted.title"),
				ctx.t("private_message.hunted.description")
						.replaceAll("%p", kind == null ? "" : kind.name)
						.replaceAll("%i", ctx.t(hunted)),
				0x4466EE
		);
	}
}
