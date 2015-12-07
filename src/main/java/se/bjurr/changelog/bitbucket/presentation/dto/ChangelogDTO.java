package se.bjurr.changelog.bitbucket.presentation.dto;

import java.util.List;

public class ChangelogDTO {
 private final List<String> references;
 private final String changelog;

 public ChangelogDTO(String changelog, List<String> references) {
  this.references = references;
  this.changelog = changelog;
 }

 public List<String> getReferences() {
  return references;
 }

 public String getChangelog() {
  return changelog;
 }
}
