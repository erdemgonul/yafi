package egcom.yafi.service;

import egcom.yafi.dto.*;
import egcom.yafi.entity.*;
import egcom.yafi.packy.ActiveUserResolver;
import egcom.yafi.packy.Role;
import egcom.yafi.repo.*;
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
    private final LikeTopicRepo likeTopicRepo;
    private final LikeCommentRepo likeCommentRepo;
    private final ActiveUserResolver activeUserResolver;
    private final Entity2DTO entity2DTO;

    public MainService(TopicRepo topicRepo, YafiUserRepo yafiUserRepo, CommentRepo commentRepo, LikeTopicRepo likeTopicRepo,
                       LikeCommentRepo likeCommentRepo, ActiveUserResolver activeUserResolver) {
        this.topicRepo = topicRepo;
        this.yafiUserRepo = yafiUserRepo;
        this.commentRepo = commentRepo;
        this.likeTopicRepo = likeTopicRepo;
        this.likeCommentRepo = likeCommentRepo;
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
        topic.setLikeCount(0L);
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

    public CommentPageDTO readCommentsFromTopic(Long topicId, PageRequest pageRequest) {
        Page<Comment> threads = commentRepo.findAllByTopicIdOrderByCreatedOnAsc(topicId, pageRequest);

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

    public long likeTopic(Long topicId) {
        Optional<Topic> topic = topicRepo.findById(topicId);

        if (!topic.isPresent())
            throw new RuntimeException("Topic with id " + topicId + " doesn not exist");
        else {
            Topic t = topic.get();

            LikeTopic likeTopic = new LikeTopic();
            likeTopic.setYafiUser(t.getYafiUser());
            likeTopic.setTopic(t);
            likeTopicRepo.save(likeTopic);
            t.setLikeCount(t.getLikeCount() + 1);
            topicRepo.save(t);

            return t.getLikeCount();
        }
    }

    public long likeComment(Long commentId) {
        Optional<Comment> comment = commentRepo.findById(commentId);

        if (!comment.isPresent())
            throw new RuntimeException("Comment with id " + commentId + " doesn not exist");
        else {
            Comment c = comment.get();
            LikeComment likeComment = new LikeComment();
            likeComment.setComment(c);
            likeComment.setYafiUser(c.getYafiUser());
            likeCommentRepo.save(likeComment);
            c.setLikeCount(c.getLikeCount() + 1);
            commentRepo.save(c);

            return c.getLikeCount();
        }
    }

    public List<TopicDTO> readMostRecentlyUpdatedTopics() {
        List<Topic> topics = topicRepo.readMostRecentlyUpdatedTopics();

        ArrayList<TopicDTO> topicDTOs = new ArrayList<>();
        for (Topic t: topics)
            topicDTOs.add(entity2DTO.topic2TopicDTO(t));

        return topicDTOs;
    }

    public TopicPageDTO readRecentTopics(PageRequest pageRequest) {
        Page<Topic> topics = topicRepo.findFirst25ByOrderByCreatedOn_Desc(pageRequest);

        ArrayList<TopicDTO> topicDTOs = new ArrayList<>();
        for (Topic t: topics)
            topicDTOs.add(entity2DTO.topic2TopicDTO(t));

        TopicPageDTO topicPageDTO = new TopicPageDTO();
        topicPageDTO.topicDTOs = topicDTOs;
        topicPageDTO.totalPageCount = topics.getTotalPages();

        return topicPageDTO;
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
        List<Topic> topics = topicRepo.findFirst10ByNameContainingOrderByNameAsc(topicName);

        ArrayList<TopicDTO> topicDTOs = new ArrayList<>();
        for (Topic t: topics)
            topicDTOs.add(entity2DTO.topic2TopicDTO(t));

        return topicDTOs;
    }

    public TopicDTO readTopic(String topicName) {
        Topic t = topicRepo.findByName(topicName).get();

        TopicDTO topicDTO = entity2DTO.topic2TopicDTO(t);

        return topicDTO;
    }
}
