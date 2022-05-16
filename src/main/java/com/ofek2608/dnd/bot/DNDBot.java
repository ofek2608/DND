package com.ofek2608.dnd.bot;

import com.ofek2608.dnd.api.Player;
import com.ofek2608.dnd.impl.PlayerImpl;
import com.ofek2608.dnd.resources.ResourcesReader;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public final class DNDBot {
	private DNDBot() {}
	public static void loadClass() {}

	public static final String TOKEN = ResourcesReader.getString("token.txt");
	public static final JDA BOT = build();


	private static JDA build() {
		JDABuilder builder = JDABuilder.createDefault(TOKEN);

//		builder.enableCache(CacheFlag.MEMBER_OVERRIDES);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);

		JDA jda;
		try {
			jda = builder.build();
		} catch (LoginException e) {
			System.out.println("Couldn't login");
			e.printStackTrace();
			System.exit(-1);
			return null;
		}
		try {
			jda.awaitStatus(JDA.Status.CONNECTED);
		} catch (InterruptedException ignored) {
		}
		return jda;
	}


	static {
		Presence presence = BOT.getPresence();
		presence.setStatus(OnlineStatus.ONLINE);
		presence.setActivity(Activity.playing("Dungeons and Dragons"));

		BOT.addEventListener(new DNDBotListener());
	}




}
