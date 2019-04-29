package egcom.yafi.util;

import egcom.yafi.dto.CommentDTO;
import egcom.yafi.dto.TopicDTO;
import egcom.yafi.entity.Comment;
import egcom.yafi.entity.Topic;
import egcom.yafi.projection.PlainThread;


import java.time.format.DateTimeFormatter;

public class Entity2DTO {

    public CommentDTO comment2CommentDTO(Comment t) {
        CommentDTO commentDTO= new CommentDTO();
        commentDTO.id = t.getId();
        commentDTO.content = t.getContent();
        commentDTO.username = t.getYafiUser().getUsername();
        commentDTO.topicName = t.getTopic().getName();
        commentDTO.likeCount = t.getLikeCount();
        commentDTO.createdOn = t.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return commentDTO;
    }

    public TopicDTO topic2TopicDTO(Topic t) {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.id = t.getId();
        topicDTO.name = t.getName();
        topicDTO.createdBy = t.getYafiUser().getUsername();
        topicDTO.content = t.getContent();
        topicDTO.likeCount = t.getLikeCount();
        topicDTO.createdOn = t.getCreatedOn();

        return topicDTO;
    }

//    public TopicDTO topicOnly2TopicDTO(TopicOnly t) {
//        TopicDTO topicDTO = new TopicDTO();
//        topicDTO.name = t.getName();
//
//        return topicDTO;
//    }

    public CommentDTO plainThread2ThreadDTO(PlainThread t) {
        CommentDTO tDTO= new CommentDTO();
        //tDTO.id = t.getId();
        tDTO.content = t.getContent();
        //tDTO.username = t.getYafiUser().getUsername();
        //tDTO.topicName = t.getTopic().getName();
        tDTO.likeCount = t.getLikeCount();
        //tDTO.createdOn = t.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return tDTO;
    }
}
