package me.stqlth.birthdaybot.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.birthdaybot.utils.Utilities;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServerInfo extends Command {

    public ServerInfo() {
        this.name = "serverinfo";
        this.help = "View information about your server.";
        this.guildOnly = true;
        this.category = new Category("Info");
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE};
    }

    @Override
    protected void execute(CommandEvent event) {

        if (event.getMember().getUser().isBot()) return;

        EmbedBuilder builder = new EmbedBuilder();
        Guild guild = event.getGuild();

        int currentShard = event.getJDA().getShardInfo().getShardId() + 1;
        int totalShards = event.getJDA().getShardInfo().getShardTotal();

        List<Member> totalMembers = event.getGuild().getMembers();
        ArrayList<Member> totalRealMembers = new ArrayList<>();
        ArrayList<Member> totalBots = new ArrayList<>();

        for (Member member : totalMembers) {
            if (!member.getUser().isBot()) {
                totalRealMembers.add(member);
                continue;
            }
            totalBots.add(member);
        }

        ArrayList<Member> onlineMembers = new ArrayList<>();

        for (Member member : totalRealMembers) {
            OnlineStatus status = member.getOnlineStatus();
            if (status == OnlineStatus.ONLINE || status == OnlineStatus.DO_NOT_DISTURB)
                onlineMembers.add(member);
        }


        SelfUser bot = event.getJDA().getSelfUser();
        TextChannel channel = event.getTextChannel();
        String month = guild.getTimeCreated().getMonth().toString().toLowerCase();
        String uMonth = month.substring(0, 1).toUpperCase() + month.substring(1);

        builder.setAuthor(guild.getName(), null, guild.getIconUrl())
                .setColor(Utilities.getAverageColor(event.getMember().getUser().getAvatarUrl()))
                .addField("Member Count", "" + totalRealMembers.size() + " (" + onlineMembers.size() + " currently online)", true)
                .addField("Bot Count", "" + totalBots.size(), true)
                .addField("Channel Count", guild.getTextChannels().size() + " text channels\n"
                        + guild.getVoiceChannels().size() + " voice channels", true)
                .addField("Server Founder", Objects.requireNonNull(guild.getOwner()).getUser().getAsTag(), true)
                .addField("Created On", uMonth + " " + guild.getTimeCreated().getDayOfMonth()
                        + getDayEnding(guild) + " " + guild.getTimeCreated().getYear(), true)
                .addField("Discord Id", guild.getId(), true)
                .addField("Current Shard", "Shard " + currentShard + "/" + totalShards + " total Shards", true)
                .setThumbnail(bot.getAvatarUrl())
                .setFooter(bot.getName(), bot.getAvatarUrl());

        channel.sendMessage(builder.build()).queue(null, (error) -> {});


    }


    private static String getDayEnding(Guild guild) {
        if (guild.getTimeCreated().getDayOfMonth() > 10) {
            if (guild.getTimeCreated().getDayOfMonth() % 10 == 1)
                return "st";
            else if (guild.getTimeCreated().getDayOfMonth() % 10 == 2)
                return "nd";
            else if (guild.getTimeCreated().getDayOfMonth() % 10 == 3)
                return "rd";
            else return "th";
        } else if (guild.getTimeCreated().getDayOfMonth() == 1)
            return "st";
        else if (guild.getTimeCreated().getDayOfMonth() == 2)
            return "nd";
        else if (guild.getTimeCreated().getDayOfMonth() == 3)
            return "rd";
        else return "th";
    }
}



