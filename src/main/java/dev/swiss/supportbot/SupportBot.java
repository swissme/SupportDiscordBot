package dev.swiss.supportbot;

import com.fasterxml.jackson.databind.*;
import dev.swiss.supportbot.commands.*;
import dev.swiss.supportbot.config.*;
import dev.swiss.supportbot.listeners.*;
import dev.swiss.supportbot.mongodb.*;
import jdk.nashorn.internal.objects.annotations.*;
import lombok.*;
import lombok.Getter;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.*;

import javax.swing.text.html.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * @author Swiss (swiss@swissdev.com)
 */
public class SupportBot {

    @Getter
    private MongoHandler mongoHandler;

    @Getter
    private Guild guild;

    @Getter
    private Config config;

    @Getter
    private Category playerReportCategory;

    @Getter
    private Category paymentIssuesCategory;

    @Getter
    private Category banAppealCategory;

    @Getter
    private Category staffApplicationCategory;

    @Getter
    private Category otherCategory;

    @Getter
    private Role playerReportRole;

    @Getter
    private Role paymentIssuesRole;

    @Getter
    private Role banAppealRole;

    @Getter
    private Role staffApplicationRole;

    @Getter
    private Role supportRole;

    @Getter
    private TextChannel transcriptsChannel;

    @Getter
    private static SupportBot instance;

    public static void main(String[] args) {
        new SupportBot();
    }

    @SneakyThrows
    public SupportBot() {
        instance = this;
        ObjectMapper objectMapper = new ObjectMapper();
        /* ---- Creates the config.json and/or loads it. ---- */
        File file = getResourceAsFile("config.json");
        String userDirectory = System.getProperty("user.dir");
        File newFile = new File(userDirectory + "/config.json");

        if (!newFile.exists()) Files.move(
                file.getAbsoluteFile().toPath(),
                newFile.getAbsoluteFile().toPath()
        );

        Path path = newFile.getAbsoluteFile().toPath();
        byte[] data = Files.readAllBytes(path);
        config = objectMapper.readValue(data, Config.class);
        /* ---------------------------------------------------*/
        if (config.getToken().isEmpty()) {
            System.out.println("Token is invalid");
            System.exit(0);
            return;
        }
        JDA jda = JDABuilder.createDefault(config.getToken()).build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (config.getGuild().isEmpty()) {
            System.out.println("Guild is invalid");
            System.exit(0);
            return;
        }
        guild = jda.getGuildById(config.getGuild());
        if (guild == null) {
            System.out.println("Guild is null");
            System.exit(0);
            return;
        }

        //Categories
        playerReportCategory = jda.getCategoryById(config.getPlayer_report_category());
        if (playerReportCategory == null) {
            System.out.println("Player Report Category is null");
            System.exit(0);
            return;
        }
        paymentIssuesCategory = jda.getCategoryById(config.getPayment_issues_category());
        if (paymentIssuesCategory == null) {
            System.out.println("Payment Issues Category is null");
            System.exit(0);
            return;
        }
        banAppealCategory = jda.getCategoryById(config.getBan_appeal_category());
        if (banAppealCategory == null) {
            System.out.println("Ban Appeal Category is null");
            System.exit(0);
            return;
        }
        staffApplicationCategory = jda.getCategoryById(config.getStaff_application_category());
        if (staffApplicationCategory == null) {
            System.out.println("Staff Application Category is null");
            System.exit(0);
            return;
        }
        otherCategory = jda.getCategoryById(config.getOther_category());
        if (otherCategory == null) {
            System.out.println("Other Category is null");
            System.exit(0);
            return;
        }

        //Roles
        playerReportRole = jda.getRoleById(config.getPlayer_report_role());
        if (playerReportRole == null) {
            System.out.println("Player Report Role is null");
            System.exit(0);
            return;
        }
        paymentIssuesRole = jda.getRoleById(config.getPayment_issues_role());
        if (paymentIssuesRole == null) {
            System.out.println("Payment Issues Role is null");
            System.exit(0);
            return;
        }
        banAppealRole = jda.getRoleById(config.getBan_appeal_role());
        if (banAppealRole == null) {
            System.out.println("Ban Appeal Role is null");
            System.exit(0);
            return;
        }
        staffApplicationRole = jda.getRoleById(config.getStaff_application_role());
        if (staffApplicationRole == null) {
            System.out.println("Staff Application Role is null");
            System.exit(0);
            return;
        }
        supportRole = jda.getRoleById(config.getSupport_role());
        if (supportRole == null) {
            System.out.println("Support Role is null");
            System.exit(0);
            return;
        }
        transcriptsChannel = this.guild.getTextChannelById(config.getTranscripts_channel());
        if (transcriptsChannel == null) {
            System.out.println("Transcripts Channel is null");
            System.exit(0);
            return;
        }
        mongoHandler =
                new MongoHandler(
                        new MongoCredentials(
                                config.isMongo_legacy(),
                                config.getMongo_host(),
                                config.getMongo_port(),
                                config.getMongo_database(),
                                config.isMongo_auth(),
                                config.getMongo_username(),
                                config.getMongo_password()
                        )
                );

        guild.upsertCommand("ticket", "Manages all ticket related commands")
                .addSubcommands(
                        new SubcommandData("panel", "Sends the ticket panel")
                )
                .addSubcommands(
                        new SubcommandData("close", "Closes a ticket")
                )
                .addSubcommands(
                        new SubcommandData("delete", "Deletes and Saves a ticket")
                )
                .addSubcommands(
                        new SubcommandData("reopen", "Reopens a ticket")
                )
                .queue();

        jda.addEventListener(new TicketPanelCommand());
        jda.addEventListener(new SelectionMenuListener());
        jda.addEventListener(new TicketCloseCommand());
        jda.addEventListener(new TicketDeleteCommand());
        jda.addEventListener(new TicketControlsListener());
    }

    private File getResourceAsFile(String resourcePath) {
        try {
            InputStream in = ClassLoader
                    .getSystemClassLoader()
                    .getResourceAsStream(resourcePath);
            if (in == null) {
                return null;
            }

            File tempFile = File.createTempFile(
                    String.valueOf(in.hashCode()),
                    ".tmp"
            );
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                //copy stream
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
