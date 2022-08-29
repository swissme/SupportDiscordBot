package dev.swiss.supportbot.commands;

import dev.swiss.supportbot.ticket.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.*;
import net.dv8tion.jda.api.hooks.*;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Swiss (swiss@swissdev.com)
 */
public class TicketCloseCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Member member = event.getMember();
        if (member == null)
            return;

        String[] args = event.getCommandPath().split("/");
        if (args[0].equalsIgnoreCase("ticket")) {
            if (Objects.requireNonNull(event.getSubcommandName()).equalsIgnoreCase("close")) {
                Ticket ticket = Ticket.getByChannel(event.getTextChannel());
                if(ticket == null) {
                    event.replyEmbeds(
                            new EmbedBuilder()
                                    .setDescription("Invalid ticket channel")
                                    .build()
                    ).queue();
                    return;
                }
                ticket.close();
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setDescription("Closed " + event.getTextChannel().getAsMention())
                                .build()
                ).queue();
            }
        }
    }

}
