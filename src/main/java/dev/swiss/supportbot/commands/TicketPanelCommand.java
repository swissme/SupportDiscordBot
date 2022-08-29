package dev.swiss.supportbot.commands;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.interactions.components.selections.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Swiss (swiss@swissdev.com)
 */
public class TicketPanelCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Member member = event.getMember();
        if(member == null)
            return;

        String[] args = event.getCommandPath().split("/");
        if(args[0].equalsIgnoreCase("ticket")) {
            if (Objects.requireNonNull(event.getSubcommandName()).equalsIgnoreCase("panel")) {
                if(!member.hasPermission(Permission.ADMINISTRATOR))
                    return;
                TextChannel channel = event.getTextChannel();
                channel.sendMessageEmbeds(
                        new EmbedBuilder()
                                .setDescription("**Are you in need of support?**\n\n" +
                                        "Then you're in luck, select a ticket type in the selection menu below to create a new ticket.\n\n" +
                                        "*( Note: Any person who abuses of the ticket system will be banned )*")
                                .build()
                ).setActionRow(
                        SelectionMenu.create("ticket-type")
                                .setPlaceholder("Choose your ticket type")
                                .setRequiredRange(1, 1)
                                .addOption("Player Report", "player-report", "Do you suspect someone on our ingame or discord server is rulebreaking?", Emoji.fromUnicode("\uD83D\uDCDC"))
                                .addOption("Payment Issues", "payment-issues", "Do you have a problem with our store?", Emoji.fromUnicode("\uD83D\uDCB5"))
                                .addOption("Ban Appeal", "ban-appeal", "Are you banned on our server?", Emoji.fromMarkdown("<:ban:943778536362217493>"))
                                .addOption("Staff Application", "staff-application", "Do you enjoy our server and want to help out?", Emoji.fromUnicode("\uD83D\uDCD6"))
                                .addOption("Other", "other", "Do you have any other problems?", Emoji.fromUnicode("\uD83C\uDF9F"))
                                .build()
                )
                .queue();
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setDescription("Sent!")
                                .build()
                ).setEphemeral(true).queue();
            }
        }
    }
}
