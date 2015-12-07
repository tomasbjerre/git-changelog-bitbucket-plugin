package se.bjurr.changelog.bitbucket.settings;


public class ChangelogSettingsBuilder {
 private String dateFormat;
 private String timeZone;
 private String template;
 private String ignoreCommitsIfMessageMatches;
 private String noIssueName;
 private String untaggedName;

 private ChangelogSettingsBuilder() {
 }

 public static ChangelogSettingsBuilder changelogSettingsBuilder() {
  return new ChangelogSettingsBuilder();
 }

 public ChangelogSettings build() throws ValidationException {
  return new ChangelogSettings(this);
 }

 public String getDateFormat() {
  return dateFormat;
 }

 public ChangelogSettingsBuilder withDateFormat(String dateFormat) {
  this.dateFormat = dateFormat;
  return this;
 }

 public String getTimeZone() {
  return timeZone;
 }

 public ChangelogSettingsBuilder withTimeZone(String timeZone) {
  this.timeZone = timeZone;
  return this;
 }

 public ChangelogSettingsBuilder withIgnoreCommitsIfMessageMatches(String ignoreCommitsIfMessageMatches) {
  this.ignoreCommitsIfMessageMatches = ignoreCommitsIfMessageMatches;
  return this;
 }

 public String getIgnoreCommitsIfMessageMatches() {
  return ignoreCommitsIfMessageMatches;
 }

 public ChangelogSettingsBuilder withUntaggedName(String untaggedName) {
  this.untaggedName = untaggedName;
  return this;
 }

 public String getNoIssueName() {
  return noIssueName;
 }

 public String getUntaggedName() {
  return untaggedName;
 }

 public ChangelogSettingsBuilder withNoIssueName(String noIssueName) {
  this.noIssueName = noIssueName;
  return this;
 }

 public String getTemplate() {
  return template;
 }

 public ChangelogSettingsBuilder withTemplate(String template) {
  this.template = template;
  return this;
 }

}
