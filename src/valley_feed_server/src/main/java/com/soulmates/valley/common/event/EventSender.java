package com.soulmates.valley.common.event;

import com.soulmates.valley.common.constants.MessageEnum;
import com.soulmates.valley.common.event.message.CommentCreateEvent;
import com.soulmates.valley.common.event.message.CustomMessage;
import com.soulmates.valley.common.event.message.LikeCreateEvent;
import com.soulmates.valley.common.event.message.PostDocDto;
import com.soulmates.valley.domain.model.doc.PostDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EventSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendCommentCreateEvent(Long fromUserId, Long toUserId, Long postId) {
        CustomMessage message = CustomMessage.builder()
                .code(MessageEnum.COMMENT_CREATE)
                .data(new CommentCreateEvent(fromUserId, toUserId, postId)).build();

        sendEvent(message, MessageEnum.TOPIC_EXCHANGE_NAME.getValue(), MessageEnum.COMMENT_CREATE.getValue());
    }

    public void sendLikeCreateEvent(Long fromUserId, Long toUserId, Long postId) {
        CustomMessage message = CustomMessage.builder()
                .code(MessageEnum.LIKE_CREATE)
                .data(new LikeCreateEvent(fromUserId, toUserId, postId)).build();

        sendEvent(message, MessageEnum.TOPIC_EXCHANGE_NAME.getValue(), MessageEnum.LIKE_CREATE.getValue());
    }

    public void sendPostAndHashTagCreateEvent(PostDoc postDoc) {
        sendPostCreateEvent(postDoc);
        sendHashTagCreateEvent(postDoc);
    }

    public void sendPostCreateEvent(PostDoc postDoc) {
        rabbitTemplate.convertAndSend(MessageEnum.TOPIC_EXCHANGE_NAME.getValue(), MessageEnum.POST_CREATE.getValue(), PostDocDto.of(postDoc));
    }

    public void sendHashTagCreateEvent(PostDoc postDoc) {
        if (!postDoc.isExistHashTag())
            return;
        List<Map<String, String>> hashList = postDoc.getHashTagList();
        for (Map<String, String> hash : hashList) {
            rabbitTemplate.convertAndSend(MessageEnum.TOPIC_EXCHANGE_NAME.getValue(), MessageEnum.HASHTAG_CREATE.getValue(), hash);
        }
    }

    public void sendEvent(CustomMessage message, String exchange, String routingKey) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
