package egcom.yafi.service;

import egcom.yafi.dto.*;
import egcom.yafi.entity.Comment;
import egcom.yafi.entity.Topic;
import egcom.yafi.entity.YafiUser;
import egcom.yafi.packy.ActiveUserResolver;
import egcom.yafi.packy.Role;
import egcom.yafi.repo.CommentRepo;
import egcom.yafi.repo.TopicRepo;
import egcom.yafi.repo.YafiUserRepo;
import egcom.yafi.util.Entity2DTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MainService {

    private final TopicRepo topicRepo;
    private final YafiUserRepo yafiUserRepo;
    private final CommentRepo commentRepo;
    private final ActiveUserResolver activeUserResolver;
    private final Entity2DTO entity2DTO;

    public MainService(TopicRepo topicRepo, YafiUserRepo yafiUserRepo, CommentRepo commentRepo, ActiveUserResolver activeUserResolver) {
        this.topicRepo = topicRepo;
        this.yafiUserRepo = yafiUserRepo;
        this.commentRepo = commentRepo;
        this.activeUserResolver = activeUserResolver;
        entity2DTO = new Entity2DTO();
    }

    public Long createTopic(CreateTopicDTO createTopicDTO) {
        Topic topic = new Topic();
        topic.setName(createTopicDTO.name);
        topic.setContent(createTopicDTO.content);
        YafiUser yafiUser = yafiUserRepo.findByUsername(activeUserResolver.getActiveUser().getUsername());
        topic.setYafiUser(yafiUser);
        topic.setCreatedOn(LocalDateTime.now());
        topic = topicRepo.save(topic);

        return topic.getId();
    }

    public Long createUser(CreateUserDTO createUserDTO) {
        YafiUser yafiUser = new YafiUser();
        yafiUser.setUsername(createUserDTO.username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(createUserDTO.password);
        yafiUser.setPassword(encodedPassword);
        yafiUser.setEnabled(true);
        yafiUser.setRole(Role.USER.getValue());
        yafiUser.setCreatedOn(LocalDateTime.now());
        yafiUser = yafiUserRepo.save(yafiUser);

        return yafiUser.getId();
    }

    public Long createComment(CreateCommentDTO createCommentDTO) {
        YafiUser yafiUser = yafiUserRepo.findByUsername(activeUserResolver.getActiveUser().getUsername());
        Optional<Topic> topic = topicRepo.findByName(createCommentDTO.topicName);
        Comment comment = new Comment();
        comment.setContent(createCommentDTO.content);
        comment.setYafiUser(yafiUser);
        comment.setTopic(topic.orElseThrow( () -> new RuntimeException("Topic with name " + createCommentDTO.topicName + " does not exist")));
        comment.setLikeCount(0L);
        comment.setCreatedOn(LocalDateTime.now());
        comment = commentRepo.save(comment);

        return comment.getId();
    }

    public CommentPageDTO readCommentsFromTopic(String topicName, PageRequest pageRequest) {
        Page<Comment> threads = commentRepo.findAllByTopicNameOrderByCreatedOnAsc(topicName, pageRequest);

        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment t: threads)
            commentDTOS.add(entity2DTO.comment2CommentDTO(t));

        CommentPageDTO commentPageDTO = new CommentPageDTO();
        commentPageDTO.commentDTOs = commentDTOS;
        commentPageDTO.totalPageCount = threads.getTotalPages();

        return commentPageDTO;
    }

    public CommentPageDTO readThreadsFromUser(String username, PageRequest pageRequest) {
        Page<Comment> threads = commentRepo.findAllByYafiUserUsernameOrderByCreatedOnAsc(username, pageRequest);

        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment t: threads)
            commentDTOS.add(entity2DTO.comment2CommentDTO(t));

        CommentPageDTO commentPageDTO = new CommentPageDTO();
        commentPageDTO.commentDTOs = commentDTOS;
        commentPageDTO.totalPageCount = threads.getTotalPages();

        return commentPageDTO;
    }

    public List<TopicDTO> readTopics() {
        List<Topic> topics = topicRepo.findAll();

        ArrayList<TopicDTO> topicDTOs = new ArrayList<>();
        for (Topic t: topics)
            topicDTOs.add(entity2DTO.topic2TopicDTO(t));

        return topicDTOs;
    }

    public long likeThread(Long threadId) {
        Optional<Comment> thread = commentRepo.findById(threadId);

        if (!thread.isPresent())
            throw new RuntimeException("Comment with id " + threadId + " doesn not exist");
        else {
            Comment t = thread.get();
            t.setLikeCount(t.getLikeCount() + 1);
            commentRepo.save(t);

            return t.getLikeCount();
        }
    }

    public List<TopicDTO> readMostRecentlyUpdatedTopics() {
        List<Topic> topics = topicRepo.readMostRecentlyUpdatedTopics();

        ArrayList<TopicDTO> topicDTOs = new ArrayList<>();
        for (Topic t: topics)
            topicDTOs.add(entity2DTO.topic2TopicDTO(t));

        return topicDTOs;
    }

    public CommentPageDTO readRecentThreads(PageRequest pageRequest) {
        Page<Comment> threads = commentRepo.findFirst25ByOrderByCreatedOn_Desc(pageRequest);

        ArrayList<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment t: threads)
            commentDTOS.add(entity2DTO.comment2CommentDTO(t));

        CommentPageDTO commentPageDTO = new CommentPageDTO();
        commentPageDTO.commentDTOs = commentDTOS;
        commentPageDTO.totalPageCount = threads.getTotalPages();

        return commentPageDTO;
    }

    public List<TopicDTO> searchByTopicName(String topicName) {
        List<TopicDTO> topics = topicRepo.findFirst10ByNameContainingOrderByNameAsc(topicName);

//        ArrayList<TopicDTO> topicDTOs = new ArrayList<>();
//        for (TopicOnly t: topics)
//            topicDTOs.add(entity2DTO.topicOnly2TopicDTO(t));

        return topics;
    }
}
