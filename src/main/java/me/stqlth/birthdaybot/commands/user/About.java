package me.stqlth.birthdaybot.commands.user;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import me.stqlth.birthdaybot.config.BirthdayBotConfig;
import me.stqlth.birthdaybot.utils.EmbedSender;
import me.stqlth.birthdaybot.utils.ErrorManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.Permission;

import java.awt.*;
import java.util.Objects;

import static me.stqlth.birthdaybot.utils.Utilities.isPrivate;


public class About extends Command {

    public About()
    {
        this.name = "about";
        this.aliases = new String[]{"botabout","hi"};
        this.guildOnly = false;
        this.help = "View information about BirthdayBot.";
        this.category = new Category("Info");
        this.botPermissions = new Permission[]{Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS};
    }

    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        String botIcon = event.getSelfUser().getAvatarUrl();
        String ownerIcon = Objects.requireNonNull(event.getJDA().getUserById(BirthdayBotConfig.getOwnerId())).getAvatarUrl();

        builder.setDescription("Hello! I am <@656621136808902656>, a bot built by <@478288246858711040>!"+
                "\n"+
                "\nI was written for Discord in Java, using the JDA library ("+ JDAInfo.VERSION+")"+
                "\nI'm currently in Version 1.3.0. " +
                "\n" +
                "\nSpecial thanks to <@212772875793334272> for helping me with this bot! Checkout his bot " +
                "[FriendTime](https://github.com/KevinNovak/Friend-Time), and add it to your discord [here]" +
                "(https://discordapp.com/oauth2/authorize?client_id=471091072546766849&scope=bot&permissions=522304)!" +
                "\n"+
                "\nType `bday help` and I'll display you a list of commands you can use!"+
//                "\nSee some of my other stats with `" + getMessageInfo.getPrefix(g) + "stats`"+
                "\n"+
                "\nFor additional help, contact <@478288246858711040> or join our discord server [here](https://discord.gg/9gUQFtz).")
                .setColor(Color.decode("#00e1ff"))
                .setThumbnail(botIcon)
                .setAuthor("BirthdayBot", null, botIcon)
                .setFooter("© 2020 Stqlth", ownerIcon);
        try {
            if (!isPrivate(event)) event.getTextChannel().sendMessage(builder.build()).queue(null, ErrorManager.GENERAL);
            else event.getPrivateChannel().sendMessage(builder.build()).queue(null, ErrorManager.PRIVATE);
        } catch (Exception ignored) {}
    }

}
