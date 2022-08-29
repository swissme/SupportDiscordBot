package dev.swiss.supportbot.config;

import lombok.*;

import java.util.*;

/**
 * @author Swiss (swiss@swissdev.com)
 */
@Getter
public class Config {

  private String token;
  private String guild;

  private long transcripts_channel;

  private long player_report_category;
  private long payment_issues_category;
  private long ban_appeal_category;
  private long staff_application_category;
  private long other_category;

  private long player_report_role;
  private long payment_issues_role;
  private long ban_appeal_role;
  private long staff_application_role;
  private long support_role;

  private boolean mongo_legacy;
  private String mongo_host;
  private int mongo_port;
  private String mongo_database;
  private boolean mongo_auth;
  private String mongo_username;
  private String mongo_password;
}
