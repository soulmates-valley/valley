package com.soulmates.valleyfollowserver.common.event;

import com.soulmates.valleyfollowserver.common.constants.MessageEnum;
import com.soulmates.valleyfollowserver.common.event.message.CustomMessage;
import com.soulmates.valleyfollowserver.common.event.message.FollowCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventSender {
    private final RabbitTemplate rabbitTemplate;

    public void sendFollowCreateEvent(Long fromUserId, Long toUserId) {
        CustomMessage message = CustomMessage.builder()
                .code(MessageEnum.FOLLOW_CREATE)
                .data(new FollowCreateEvent(fromUserId, toUserId)).build();

        rabbitTemplate.convertAndSend(MessageEnum.TOPIC_EXCHANGE_NAME.getValue(), MessageEnum.FOLLOW_CREATE.getValue(), message);
    }
}
