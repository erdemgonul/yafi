package egcom.yafi.repo;

import egcom.yafi.dto.TopicDTO;
import egcom.yafi.entity.Comment;
import egcom.yafi.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TopicRepo extends JpaRepository<Topic, Long> {

    Optional<Topic> findByName(String topicName);

    @Query( value = "select topic.*"
            + " from topic, (select topic_id, MAX(created_on) AS thread_created_on from thread group by topic_id order by MAX(created_on) desc) AS x"
            + " where x.topic_id = topic.id order by x.thread_created_on desc;", nativeQuery = true)
    List<Topic> readMostRecentlyUpdatedTopics();

    List<Topic> findFirst10ByNameContainingOrderByNameAsc(String topicName);

    //@EntityGraph(attributePaths = {"yafiUser", "topic"} ,type = EntityGraph.EntityGraphType.FETCH) //TODO: LEARN THIS
    Page<Topic> findFirst25ByOrderByCreatedOn_Desc(Pageable pageable);
}
