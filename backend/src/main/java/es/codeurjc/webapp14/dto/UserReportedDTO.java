package es.codeurjc.webapp14.dto;

import java.util.List;


public record UserReportedDTO (
    Long id,
    String name,
    String surname,
    String email,
    String address,
    boolean banned,
    int reports,
    String imageUrl,
    List<String> roles,
    List<ReviewReportedDTO> reviews
) {}
