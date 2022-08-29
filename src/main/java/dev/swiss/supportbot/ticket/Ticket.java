package dev.swiss.supportbot.ticket;

import com.mongodb.client.model.*;
import dev.swiss.supportbot.*;
import dev.swiss.supportbot.mongodb.*;
import lombok.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.*;
import org.bson.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Swiss (swiss@swissdev.com)
 */

@Data
public class Ticket {

    private static List<Ticket> tickets = new ArrayList<>();

    private long channel;
    private Member member;
    private TicketType type;

    public Ticket(long member, long channel, TicketType ticketType) {
        this.channel = channel;
        this.member = SupportBot.getInstance().getGuild().retrieveMemberById(member).complete();
        this.type = ticketType;

        this.load();
    }

    public void load() {
        Document document = MongoHandler.getTickets().find(Filters.eq("channel", this.channel)).first();
        if(document != null) {
            this.member = SupportBot.getInstance().getGuild().retrieveMemberById(document.getLong("member")).complete();
            this.type = TicketType.valueOf(document.getString("type"));
            tickets.removeIf(ticket -> ticket.getChannel() == this.channel);
            tickets.add(this);
            return;
        }
        document = new Document()
                .append("channel", this.channel)
                .append("member", this.member.getIdLong())
                .append("type", this.type.name());
        MongoHandler.getTickets().insertOne(document);
        tickets.add(this);
    }

    public void save() {
        tickets.removeIf(ticket -> ticket.getChannel() == this.channel);
        tickets.add(this);
        MongoHandler.getTickets().replaceOne(
                Filters.eq("channel", this.channel),
                new Document()
                        .append("channel", this.channel)
                        .append("member", this.member.getIdLong())
                        .append("type", this.type.name())
        );
    }

    public void close() {
        TextChannel channel = SupportBot.getInstance().getGuild().getTextChannelById(this.channel);
        if(channel == null) {
            this.delete();
            return;
        }

        channel.upsertPermissionOverride(this.member)
                .setDeny(Permission.VIEW_CHANNEL)
                .queue();

        channel.sendMessageEmbeds(
                new EmbedBuilder()
                        .setDescription("Ticket Controls")
                        .build()
        )
                .setActionRow(Button.danger("ticket-delete", "Delete"), Button.primary("ticket-reopen", "Reopen"))
                .queue();
    }

    public void delete() {
        TextChannel channel = SupportBot.getInstance().getGuild().getTextChannelById(this.channel);

        if(channel != null) {
            File transcript = this.transcript();

            this.member.getUser().openPrivateChannel().queue((pm) -> {
                pm.sendMessageEmbeds(
                                new EmbedBuilder()
                                        .setTitle(this.member.getUser().getName() + "#" + this.member.getUser().getDiscriminator())
                                        .addField("Ticket Owner", this.member.getUser().getAsMention(), true)
                                        .addField("Panel Name", this.type.name(), true)
                                        .build()
                        )
                        .addFile(transcript)
                        .queue();
            });
            SupportBot.getInstance().getTranscriptsChannel().sendMessageEmbeds(
                    new EmbedBuilder()
                            .setTitle(this.member.getUser().getName() + "#" + this.member.getUser().getDiscriminator())
                            .addField("Ticket Owner", this.member.getUser().getAsMention(), true)
                            .addField("Panel Name", this.type.name(), true)
                            .build()
            )
                    .addFile(transcript)
                    .queue();
            channel.delete().queue();
        }

        tickets.removeIf(ticket -> ticket.getChannel() == this.channel);
        MongoHandler.getTickets().deleteOne(Filters.eq("channel", this.channel));
    }

    public File transcript() {
        File file = null;
        BufferedWriter bw;
        StringBuilder data = new StringBuilder();

        MessageHistory history = MessageHistory.getHistoryFromBeginning(Objects.requireNonNull(SupportBot.getInstance().getGuild().getTextChannelById(this.channel))).complete();
        List<Message> mess = new ArrayList<>(history.getRetrievedHistory());

        Collections.reverse(mess);

        for (Message message : mess) {
            if(message.getMember().getUser().isBot())
                continue;
            data
                    .append("<li>")
                    .append(message.getMember().getUser().getName())
                    .append("#")
                    .append(message.getMember().getUser().getDiscriminator())
                    .append(": ")
                    .append(message.getContentRaw())
                    .append(" ")
                    .append("(")
                    .append(message.getTimeCreated())
                    .append(")")
                    .append("</li>");
        }

        try {
            file = new File("D:\\Workspace\\ImperialRust\\Discord Bots\\Support\\ticket-transcripts\\" + UUID.randomUUID() + ".html");
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("<ul>");
            bw.write(data.toString());
            bw.write("</ul>");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public void reopen() {
        TextChannel channel = SupportBot.getInstance().getGuild().getTextChannelById(this.channel);
        if(channel == null) {
            this.delete();
            return;
        }

        channel.upsertPermissionOverride(this.member)
                .setAllow(Permission.VIEW_CHANNEL)
                .queue();

        channel.sendMessageEmbeds(
                        new EmbedBuilder()
                                .setDescription("Reopened")
                                .build()
                )
                .queue();
    }

    public static Ticket getByChannel(TextChannel channel) {
        return tickets.stream().filter(ticket -> ticket.getChannel() == channel.getIdLong()).findFirst().orElse(null);
    }

    public static void loadTickets() {
        for (Document document : MongoHandler.getTickets().find()) {
            new Ticket(document.getLong("member"), document.getLong("channel"), TicketType.valueOf(document.getString("type")));
        }
    }

}
