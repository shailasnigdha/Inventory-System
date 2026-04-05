package com.seproject.inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;

    @JsonProperty("roles")
    private Set<String> roles;

    private LocalDateTime createdAt;
    private boolean enabled;

    // Custom getter to ensure roles are properly displayed as comma-separated string
    public String getRolesAsString() {
        if (roles == null || roles.isEmpty()) {
            return "N/A";
        }
        return String.join(", ", roles);
    }
}

