package egcom.yafi.repo;

import egcom.yafi.entity.LikeTopic;
import egcom.yafi.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeTopicRepo extends JpaRepository<LikeTopic, Long> {
}
