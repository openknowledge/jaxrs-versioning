package de.openknowledge.jaxrs.versioning.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class StreetV1 {

  private String name;

  private String number;

  public StreetV1(String name, String number) {
    this.name = name;
    this.number = number;
  }
}
