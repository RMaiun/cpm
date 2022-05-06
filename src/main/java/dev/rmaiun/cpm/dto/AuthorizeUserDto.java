package dev.rmaiun.cpm.dto;

public record AuthorizeUserDto(String app, String domain, String user, DomainAuthorizationType authType) {

}
