package egcom.yafi.dto.validator;

import egcom.yafi.dto.CreateCommentDTO;
import org.springframework.stereotype.Component;

@Component
public class CreateThreadDTOValidator {

    public void validate(CreateCommentDTO createCommentDTO) {
        if (createCommentDTO == null)
            throw new IllegalArgumentException("createCommentDTO can not be null");
        else if (createCommentDTO.topicName == null)
            throw new IllegalArgumentException("topicName can not be null");
        else if (createCommentDTO.topicName.isEmpty())
            throw new IllegalArgumentException("topicName can not be empty");
        else if (createCommentDTO.content == null)
            throw new IllegalArgumentException("content can not be null");
        else if (createCommentDTO.content.isEmpty())
            throw new IllegalArgumentException("content can not be empty");
    }
}
