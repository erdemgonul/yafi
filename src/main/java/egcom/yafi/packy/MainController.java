package egcom.yafi.packy;

import egcom.yafi.dto.*;
import egcom.yafi.dto.validator.CreateThreadDTOValidator;
import egcom.yafi.dto.validator.CreateTopicDTOValidator;
import egcom.yafi.dto.validator.CreateUserDTOValidator;
import egcom.yafi.service.MainService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(value = "http://localhost:4200" )//TODO: FIXME
public class MainController {

    private final MainService mainService;
    private final CreateUserDTOValidator createUserDTOValidator;
    private final CreateTopicDTOValidator createTopicDTOValidator;
    private final CreateThreadDTOValidator createThreadDTOValidator;

    public MainController(MainService mainService, CreateUserDTOValidator createUserDTOValidator,
                          CreateTopicDTOValidator createTopicDTOValidator,
                          CreateThreadDTOValidator createThreadDTOValidator) {
        this.mainService = mainService;
        this.createUserDTOValidator = createUserDTOValidator;
        this.createTopicDTOValidator = createTopicDTOValidator;
        this.createThreadDTOValidator = createThreadDTOValidator;
    }

    @RequestMapping("/")
    public String greeting() {
        return "Welcome to YAFI";
    }

    @PostMapping("/login")
    public boolean login() {
        return true;
    }

    @PostMapping("/topic")
    public Long createTopic(@RequestBody CreateTopicDTO createTopicDTO) {
        createTopicDTOValidator.validate(createTopicDTO);
        Long result = mainService.createTopic(createTopicDTO);

        return result;
    }

    @PostMapping("/user")
    public Long createUser(@RequestBody CreateUserDTO createUserDTO) {
        createUserDTOValidator.validate(createUserDTO);
        Long result = mainService.createUser(createUserDTO);

        return result;
    }

    @PostMapping("/comment")
    public Long createComment(@RequestBody CreateCommentDTO createCommentDTO) {
        createThreadDTOValidator.validate(createCommentDTO);
        Long result = mainService.createComment(createCommentDTO);

        return result;
    }

    @PostMapping("/likethread/{threadId}")
    public Long likeThread(@PathVariable Long threadId) {
        Long result = mainService.likeThread(threadId);

        return result;
    }

    @GetMapping("/topic/{topicName}")
    public CommentPageDTO readCommentsFromTopic(@PathVariable String topicName, @RequestParam("page") int page) {
        CommentPageDTO threadDTOs = mainService.readCommentsFromTopic(topicName,  PageRequest.of(page, 2));

        return threadDTOs;
    }

    @GetMapping("/thread/{username}")
    public CommentPageDTO readThreadsFromUser(@PathVariable String username, @RequestParam("page") int page) {
        CommentPageDTO commentPageDTO = mainService.readThreadsFromUser(username, PageRequest.of(page, 2));

        return commentPageDTO;
    }

    @GetMapping("/topics")
    public List<TopicDTO> readTopics() {
        List<TopicDTO> topicDTOs = mainService.readTopics();

        return topicDTOs;
    }

    @GetMapping("/topics/recent")
    public List<TopicDTO> readMostRecentlyUpdatedTopics() {
        List<TopicDTO> topicDTOs = mainService.readMostRecentlyUpdatedTopics();

        return topicDTOs;
    }

    @GetMapping("/threads/recent")
    public CommentPageDTO readRecentThreads(@RequestParam("page") int page) {
        CommentPageDTO commentPageDTO = mainService.readRecentThreads(PageRequest.of(page, 25));

        return commentPageDTO;
    }

    @GetMapping("/topics/search")
    public List<TopicDTO> searchByTopicName(@RequestParam("topicName") String topicName) {
        List<TopicDTO> topicDTOs = mainService.searchByTopicName(topicName);

        return topicDTOs;
    }
}
