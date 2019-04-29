package egcom.yafi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    @Column(length = 2048)
    private String content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "yafi_user_id")
    private YafiUser yafiUser;

    @NotNull
    private Long likeCount;

    private LocalDateTime createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YafiUser getYafiUser() {
        return yafiUser;
    }

    public void setYafiUser(YafiUser yafiUser) {
        this.yafiUser = yafiUser;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
}
