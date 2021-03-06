package me.stqlth.birthdaybot.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.birthdaybot.utils.DatabaseMethods;
import me.stqlth.birthdaybot.utils.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Objects;

public class Settings extends Command {

	private DatabaseMethods db;

	public Settings(DatabaseMethods databaseMethods) {
		this.name = "settings";
		this.help = "View your server's current settings";
		this.guildOnly = true;
		this.hidden = true;
		this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};

		this.db = databaseMethods;
	}


	@Override
	protected void execute(CommandEvent event) {
		TextChannel channel = event.getTextChannel();
		currentConfig(event, channel);
	}

	public void currentConfig(CommandEvent event, TextChannel channel) {
		EmbedBuilder builder = new EmbedBuilder();
		SelfUser bot = event.getJDA().getSelfUser();
		Guild guild = event.getGuild();

		long bdayChannel = db.getBirthdayChannel(event.getGuild());
		long bdayRole = db.getBirthdayRole(event.getGuild());
		long trustedRole = db.getTrustedRole(guild);
		int messageTime = db.getGuildMessageTime(event.getGuild());
		String mTime;
		if (messageTime >= 0 && messageTime <= 9) {
			mTime = "0" + messageTime + ":00";
		} else mTime = messageTime + ":00";

		String customMessage = db.getGuildBirthdayMessage(event.getGuild());
		if (customMessage.equalsIgnoreCase("0")) customMessage = "Default";
		String mentionSetting = db.getMentionSetting(event.getGuild());
		String bChannel = "Not Set";
		String bRole = "Not Set";
		String tRole = "Not Set";
		String mSetting = "Disabled";

		try {
			if (bdayChannel != 0) bChannel = Objects.requireNonNull(event.getGuild().getTextChannelById(bdayChannel)).getAsMention();
		} catch (Exception ex) {
			bChannel = "Not Set";
		}
		try {
		if (bdayRole != 0) bRole = Objects.requireNonNull(event.getGuild().getRoleById(bdayRole)).getAsMention();
		} catch (Exception ex) {
			bRole = "Not Set";
		}
		try {
		if (trustedRole != 0) tRole = Objects.requireNonNull(event.getGuild().getRoleById(trustedRole)).getAsMention();
		} catch (Exception ex) {
			tRole = "Not Set";
		}

		try {
			if (!mentionSetting.equals("0") && !mentionSetting.equalsIgnoreCase("everyone") && !mentionSetting.equalsIgnoreCase("here"))
				mSetting = Objects.requireNonNull(event.getGuild().getRoleById(mentionSetting)).getAsMention();
		} catch (Exception ex) {
			mSetting = "Disabled";
		}

		if (mentionSetting.equals("everyone")) mSetting = "@everyone";
		else if (mentionSetting.equals("here")) mSetting = "@here";

		String preventMessages = Utilities.capitalize(String.valueOf(db.getTrustedPreventMessage(guild)));
		String preventRole = Utilities.capitalize(String.valueOf(db.getTrustedPreventRole(guild)));
		String useEmbed = Utilities.capitalize(String.valueOf(db.getUseEmbed(guild)));


		builder.setAuthor(guild.getName() + "'s Settings", null, guild.getIconUrl())
				.setColor(Utilities.getAverageColor(event.getMember().getUser().getAvatarUrl()))
				.addField("Birthday Channel", bChannel, true)
				.addField("Birthday Role", bRole, true)
				.addField("Mention Setting", mSetting, true)
				.addField("Message Time", "" + mTime, true)
				.addField("Custom Message", "" + customMessage, true)
				.addField("Trusted Role", tRole, true)
				.addField("Trusted Prevents Role", "" + preventRole, true)
				.addField("Trusted Prevents Message", "" + preventMessages, true)
				.addField("Use Embed", "" + useEmbed, true)
//				.setThumbnail(bot.getAvatarUrl())
				.setFooter(bot.getName(), bot.getAvatarUrl());

		channel.sendMessage(builder.build()).queue(null, (error) -> {});
	}
}
