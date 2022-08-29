package dev.swiss.supportbot.listeners;

import dev.swiss.supportbot.ticket.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.*;
import net.dv8tion.jda.api.hooks.*;
import org.jetbrains.annotations.*;

/**
 * @author Swiss (swiss@swissdev.com)
 */
public class TicketControlsListener extends ListenerAdapter {

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        Member member = event.getMember();
        if(member == null)
            return;
        Ticket ticket = Ticket.getByChannel(event.getTextChannel());

        if(ticket == null) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setDescription("Invalid ticket channel")
                            .build()
            ).queue();
            return;
        }
        event.reply(".").setEphemeral(true).queue();

        if(event.getComponentId().equals("ticket-delete")) {
            ticket.delete();
        } else if(event.getComponentId().equals("ticket-reopen")) {
            ticket.reopen();
        } else if(event.getComponentId().equals("ticket-close")) {
            ticket.close();
        }
    }

}
