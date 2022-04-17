package dev.rmaiun.cpm.doman;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.springframework.data.annotation.Id;

public class BusinessRole {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String code;
  private String workspace;
  private Application app;
  private BusinessRole parent;
}
