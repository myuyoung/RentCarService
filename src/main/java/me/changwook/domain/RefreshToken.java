package me.changwook.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private String id;

    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    private Long expiryDate;

}
