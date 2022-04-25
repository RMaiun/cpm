package dev.rmaiun.cpm.doman;


public class Domain {

  private Long id;
  private String code;
  private Long appId;

  public Domain() {
  }

  public Domain(Long id, String code, Long appId) {
    this.id = id;
    this.code = code;
    this.appId = appId;
  }

  public Long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public Long getAppId() {
    return appId;
  }
}
