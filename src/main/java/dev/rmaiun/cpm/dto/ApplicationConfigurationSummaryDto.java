package dev.rmaiun.cpm.dto;

public record ApplicationConfigurationSummaryDto(String application,
                                                 long domainsCreated,
                                                 long groupsCreated,
                                                 long usersProcessed
) {

}
