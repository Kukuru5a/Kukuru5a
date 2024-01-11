package hexlet.code.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class Url {

    private static Long id;
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

    public static Long getId() {
        return id;
    }

    public static void setId(Long id) {
        Url.id = id;
    }
}
