package se.bjurr.changelog.bitbucket.presentation.dto;

import static com.google.common.base.Optional.fromNullable;

import java.util.List;

import com.google.common.base.Optional;

public class ChangelogDTO {
 private final List<String> references;
 private final String changelog;

 public ChangelogDTO(String changelog, List<String> references) {
  this.references = references;
  this.changelog = changelog;
 }

 public ChangelogDTO(List<String> references) {
  this.references = references;
  this.changelog = null;
 }

 public List<String> getReferences() {
  return references;
 }

 public Optional<String> getChangelog() {
  return fromNullable(changelog);
 }
}
