package egcom.yafi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints =  @UniqueConstraint(columnNames = {"topic_id", "yafi_user_id"}))
public class LikeTopic {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "yafi_user_id")
    private YafiUser yafiUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public YafiUser getYafiUser() {
        return yafiUser;
    }

    public void setYafiUser(YafiUser yafiUser) {
        this.yafiUser = yafiUser;
    }
}
