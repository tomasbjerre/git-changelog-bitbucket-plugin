package se.bjurr.changelog.bitbucket.settings;

import java.util.ArrayList;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class AdminFormValues extends ArrayList<Map<String, String>> {
 private static final long serialVersionUID = 9084184120202816120L;
 public static final String NAME = "name";
 public static final String VALUE = "value";

 public enum FIELDS {
  dateFormat, //
  timeZone, //
  ignoreCommitsIfMessageMatches, //
  untaggedName, //
  noIssueName, //
  template
 }
}
