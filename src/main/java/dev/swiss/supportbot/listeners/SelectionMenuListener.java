package dev.swiss.supportbot.listeners;

import dev.swiss.supportbot.*;
import dev.swiss.supportbot.ticket.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.interactions.components.*;
import org.jetbrains.annotations.*;

import java.util.*;

/**
 * @author Swiss (swiss@swissdev.com)
 */
public class SelectionMenuListener extends ListenerAdapter {

    @Override
    public void onSelectionMenu(@NotNull SelectionMenuEvent event) {
        Member member = event.getMember();
        if(member == null)
            return;
        TextChannel channel = null;
        if(Objects.requireNonNull(event.getValues()).get(0).equals("player-report")) {
            Role role = SupportBot.getInstance().getPlayerReportRole();
            Category category = SupportBot.getInstance().getPlayerReportCategory();
            channel = category.createTextChannel("player-report-" + member.getIdLong())
                    .addPermissionOverride(member, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(role, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(SupportBot.getInstance().getGuild().getPublicRole(), null, Collections.singleton(Permission.VIEW_CHANNEL))
                    .complete();
            channel.sendMessage(SupportBot.getInstance().getPlayerReportRole().getAsMention()).queue();
            channel.sendMessage(member.getAsMention()).queue();
            channel.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setDescription("**Player Report**\n" +
                                    "- Hello there " + member.getAsMention() + ", thank you for making a Player Report Ticket. A member from our Administration Team  will be handling this shorty!\n" +
                                    "Please answer the following questions in regard to your report:\n" +
                                    "\n" +
                                    "```- Your Steam 64:\n" +
                                    "- What is the players name:\n" +
                                    "- What is his 64 ID (If possible) \n" +
                                    "- Reason For The Report:\n" +
                                    "- Any proof you can provide:\n" +
                                    "- Any additional comments: ```\n" +
                                    "\n" +
                                    " A member from our Administration team will respond as fast as they can!\n")
                            .build()
            )
                    .setActionRow(
                            Button.primary("ticket-close", "Close")
                    )
                    .queue();
            Ticket ticket = new Ticket(member.getIdLong(), channel.getIdLong(), TicketType.PLAYER_REPORT);
        } else if(Objects.requireNonNull(event.getValues()).get(0).equals("payment-issues")) {
            Role role = SupportBot.getInstance().getPaymentIssuesRole();
            Category category = SupportBot.getInstance().getPaymentIssuesCategory();
            channel = category.createTextChannel("payment-issues-" + member.getIdLong())
                    .addPermissionOverride(member, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(role, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(SupportBot.getInstance().getGuild().getPublicRole(), null, Collections.singleton(Permission.VIEW_CHANNEL))
                    .complete();
            channel.sendMessage(SupportBot.getInstance().getPaymentIssuesRole().getAsMention()).queue();
            channel.sendMessage(member.getAsMention()).queue();
            channel.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setDescription("**Payment Support**\n" +
                                    "Hello there " + member.getAsMention() + ", Thank you for creating a Payment Support Ticket! I assure you we will fix any issue that has accrued & we are very sorry for any inconvenience this may provide.\n" +
                                    "Please answer the following questions in regard to your issue:\n" +
                                    "\n" +
                                    "```- Tebex Payment ID:\n" +
                                    "- Steam 64 ID:\n" +
                                    "- Issue that had occurred: ```\n" +
                                    "\n" +
                                    " A member from our Payment Support team will respond as fast as they can!\n")
                            .build()
            )
                    .setActionRow(
                        Button.primary("ticket-close", "Close")
                    )
                    .queue();
            Ticket ticket = new Ticket(member.getIdLong(), channel.getIdLong(), TicketType.PAYMENT_ISSUES);
        } else if(Objects.requireNonNull(event.getValues()).get(0).equals("ban-appeal")) {
            Role role = SupportBot.getInstance().getBanAppealRole();
            Category category = SupportBot.getInstance().getBanAppealCategory();
            channel = category.createTextChannel("ban-appeal-" + member.getIdLong())
                    .addPermissionOverride(member, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(role, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(SupportBot.getInstance().getGuild().getPublicRole(), null, Collections.singleton(Permission.VIEW_CHANNEL))
                    .complete();
            channel.sendMessage(SupportBot.getInstance().getBanAppealRole().getAsMention()).queue();
            channel.sendMessage(member.getAsMention()).queue();
            channel.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setDescription("**Ban Appeal**\n" +
                                    "Hello there " + member.getAsMention() + ", thank you for creating a Ban Appeal for Imperial Rust Servers! Please follow the format below regarding your ban appeal!\n" +
                                    "\n" +
                                    "```- What is your Steam 64 ID:\n" +
                                    "- Who was the Staff Member that banned you:\n" +
                                    "- What was the reason for the ban:\n" +
                                    "- Please explain your side of the story regarding what happened in the moments leading up to said ban:\n" +
                                    "- Do you believe this ban was fair (If not, why not):\n" +
                                    "- Why should you be unbanned: ```\n" +
                                    "\n" +
                                    "A member from our Ban Appeal team will respond as fast as they can!\n")
                            .build()
            )
                    .setActionRow(
                            Button.primary("ticket-close", "Close")
                    )
                    .queue();
            Ticket ticket = new Ticket(member.getIdLong(), channel.getIdLong(), TicketType.BAN_APPEAL);
        } else if(Objects.requireNonNull(event.getValues()).get(0).equals("staff-application")) {
            Role role = SupportBot.getInstance().getStaffApplicationRole();
            Category category = SupportBot.getInstance().getStaffApplicationCategory();
            channel = category.createTextChannel("staff-application-" + member.getIdLong())
                    .addPermissionOverride(member, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(role, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(SupportBot.getInstance().getGuild().getPublicRole(), null, Collections.singleton(Permission.VIEW_CHANNEL))
                    .complete();
            channel.sendMessage(SupportBot.getInstance().getStaffApplicationRole().getAsMention()).queue();
            channel.sendMessage(member.getAsMention()).queue();
            channel.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setDescription("```1 What is your age:\n" +
                                    "2 What name do you go by online?:\n" +
                                    "3 Steam PageURL or steamid64:\n" +
                                    "4 Why do you want to apply to our staff team? (2-3 lines):\n" +
                                    "5 What draws you to our staff team over any other staff team?:\n" +
                                    "6 Do you have any prior staff experience on a rust server?:\n" +
                                    "7 Why do you want to staff instead of being a player? (2-3 lines):\n" +
                                    "8 Do you have any friends on our staff team, if so who?:\n" +
                                    "9 Why should we accept you over any other applicant? (3-5+ lines):\n" +
                                    "10 What is your average playtime during the week?:\n" +
                                    "11 Why did you not apply sooner?:\n" +
                                    "12 Where do you see yourself progressing to in our staff team? (2-3 sentences):\n" +
                                    "13 Are you willing to progress through the ranks?(yes/no):\n" +
                                    "14 Rate yourself from 1-10, justify the reason for your score?:\n" +
                                    "15 Are you considering staying long term?:\n" +
                                    "16 What skills do you possess? and how can they be used to there full potential on our staff team?:\n" +
                                    "17 If the roles were flipped would you hire yourself? tell me why (3 lines):\n" +
                                    "18 What is your full discord?:\n" +
                                    "19 What Region are you?:\n" +
                                    "20 What is your availability every day?:\n" +
                                    "21 What do you Want to get from Imperial?:```")
                            .build()
            )
                    .setActionRow(
                            Button.primary("ticket-close", "Close")
                    )
                    .queue();
            Ticket ticket = new Ticket(member.getIdLong(), channel.getIdLong(), TicketType.STAFF_APPLICATION);
        } else if(Objects.requireNonNull(event.getValues()).get(0).equals("other")) {
            Role role = SupportBot.getInstance().getSupportRole();
            Category category = SupportBot.getInstance().getOtherCategory();
            channel = category.createTextChannel("other-" + member.getIdLong())
                    .addPermissionOverride(member, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(role, Collections.singleton(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(SupportBot.getInstance().getGuild().getPublicRole(), null, Collections.singleton(Permission.VIEW_CHANNEL))
                    .complete();
            channel.sendMessage(SupportBot.getInstance().getSupportRole().getAsMention()).queue();
            channel.sendMessage(member.getAsMention()).queue();
            channel.sendMessageEmbeds(
                    new EmbedBuilder()
                            .setDescription("**Support Message**\n" +
                                    "\n" +
                                    "- Hello there " + member.getAsMention() + ", thank you for making a Support Ticket. A member from our Support Team will be handling this shorty!\n")
                            .build()
            )
                    .setActionRow(
                            Button.primary("ticket-close", "Close")
                    )
                    .queue();
            Ticket ticket = new Ticket(member.getIdLong(), channel.getIdLong(), TicketType.OTHER);
        }
        if(channel != null) {
            event.replyEmbeds(
                    new EmbedBuilder()
                            .setDescription("A ticket has been opened at " + channel.getAsMention())
                            .build()
            ).setEphemeral(true).queue();
        }
    }

}
