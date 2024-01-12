package hexlet.code.models;
import hexlet.code.repositories.UrlCheckRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

@Getter
@Setter
@ToString
public final class Url {

    private Long id;
    @ToString.Include
    private String name;
    private Timestamp createdAt;


    public Url(String name) {
        this.name = name;
    }

    public Url(String name, Timestamp createdAt) {
        this.name = name;
        this.createdAt = createdAt;
    }
}