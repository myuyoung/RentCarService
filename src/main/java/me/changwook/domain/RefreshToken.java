package me.changwook.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @Column
    private String id;

    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    private Long expiryDate;

}
