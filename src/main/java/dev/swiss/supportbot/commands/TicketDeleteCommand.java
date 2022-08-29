package dev.swiss.supportbot.commands;

import dev.swiss.supportbot.*;
import dev.swiss.supportbot.ticket.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.*;
import net.dv8tion.jda.api.hooks.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.util.*;

/**
 * @author Swiss (swiss@swissdev.com)
 */
public class TicketDeleteCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Member member = event.getMember();
        if (member == null)
            return;

        String[] args = event.getCommandPath().split("/");
        if (args[0].equalsIgnoreCase("ticket")) {
            if (Objects.requireNonNull(event.getSubcommandName()).equalsIgnoreCase("delete")) {
                Ticket ticket = Ticket.getByChannel(event.getTextChannel());
                if(ticket == null) {
                    event.replyEmbeds(
                            new EmbedBuilder()
                                    .setDescription("Invalid ticket channel")
                                    .build()
                    ).queue();
                    return;
                }
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setDescription("Deleted " + event.getTextChannel().getAsMention())
                                .build()
                ).queue();
                ticket.delete();
            }
        }
    }

}
