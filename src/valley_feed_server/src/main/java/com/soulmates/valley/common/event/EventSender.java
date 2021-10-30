package com.soulmates.valley.common.event;

import com.soulmates.valley.common.constants.MessageEnum;
import com.soulmates.valley.common.event.message.CommentCreateEvent;
import com.soulmates.valley.common.event.message.CustomMessage;
import com.soulmates.valley.common.event.message.LikeCreateEvent;
import com.soulmates.valley.common.event.message.PostDocDto;
import com.soulmates.valley.domain.model.PostDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class EventSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendEventCommentCreate(Long fromUserId, Long toUserId, Long postId) {
        CustomMessage message = CustomMessage.builder()
                .code(MessageEnum.COMMENT_CREATE)
                .data(new CommentCreateEvent(fromUserId, toUserId, postId)).build();

        sendEvent(message, MessageEnum.TOPIC_EXCHANGE_NAME.getRoutingKey(), MessageEnum.COMMENT_CREATE.getRoutingKey());
    }

    public void sendEventLikeCreate(Long fromUserId, Long toUserId, Long postId) {
        CustomMessage message = CustomMessage.builder()
                .code(MessageEnum.LIKE_CREATE)
                .data(new LikeCreateEvent(fromUserId, toUserId, postId)).build();

        sendEvent(message, MessageEnum.TOPIC_EXCHANGE_NAME.getRoutingKey(), MessageEnum.LIKE_CREATE.getRoutingKey());
    }

    public void sendEventPostAndHashTagCreate(PostDoc postDoc) {
        sendEventPostCreate(postDoc);
        sendEventHashTagCreate(postDoc);
    }

    public void sendEventPostCreate(PostDoc postDoc) {
        rabbitTemplate.convertAndSend(MessageEnum.TOPIC_EXCHANGE_NAME.getRoutingKey(), MessageEnum.POST_CREATE.getRoutingKey(), PostDocDto.of(postDoc));
    }

    public void sendEventHashTagCreate(PostDoc postDoc) {
        if (!postDoc.isExistHashTag())
            return;
        List<Map<String, String>> hashList = postDoc.getHashTagList();
        for (Map<String, String> hash : hashList) {
            rabbitTemplate.convertAndSend(MessageEnum.TOPIC_EXCHANGE_NAME.getRoutingKey(), MessageEnum.HASHTAG_CREATE.getRoutingKey(), hash);
        }
    }

    public void sendEvent(CustomMessage message, String exchange, String routingKey) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
