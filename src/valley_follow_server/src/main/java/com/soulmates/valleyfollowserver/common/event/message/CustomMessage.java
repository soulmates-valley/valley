package com.soulmates.valleyfollowserver.common.event.message;

import com.soulmates.valleyfollowserver.common.constants.MessageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CustomMessage {
    MessageEnum code;

    Object data;
}
