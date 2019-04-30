package egcom.yafi.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints =  @UniqueConstraint(columnNames = {"comment_id", "yafi_user_id"}))
public class LikeComment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

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

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public YafiUser getYafiUser() {
        return yafiUser;
    }

    public void setYafiUser(YafiUser yafiUser) {
        this.yafiUser = yafiUser;
    }
}
