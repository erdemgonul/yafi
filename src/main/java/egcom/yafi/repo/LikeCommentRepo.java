package egcom.yafi.repo;

import egcom.yafi.entity.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeCommentRepo extends JpaRepository<LikeComment, Long> {
}
