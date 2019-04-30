package egcom.yafi.repo;

import egcom.yafi.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentRepo extends PagingAndSortingRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"yafiUser", "topic"}, type=EntityGraph.EntityGraphType.LOAD)
    Page<Comment> findAllByTopicNameOrderByCreatedOnAsc(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"yafiUser", "topic"}, type=EntityGraph.EntityGraphType.LOAD)
    Page<Comment> findAllByTopicIdOrderByCreatedOnAsc(Long topicId, Pageable pageable);

    Page<Comment> findAllByYafiUserUsernameOrderByCreatedOnAsc(String username, Pageable pageable);

    @EntityGraph(attributePaths = {"yafiUser", "topic"} ,type = EntityGraph.EntityGraphType.FETCH) //TODO: LEARN THIS
    Page<Comment> findFirst25ByOrderByCreatedOn_Desc(Pageable pageable);

}
