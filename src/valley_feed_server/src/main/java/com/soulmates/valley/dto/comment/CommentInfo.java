package com.soulmates.valley.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"id"})
@RequiredArgsConstructor
@AllArgsConstructor
public class CommentInfo {

    private Long id;

    private String content;

    private LocalDateTime createDt;

    private Long userId;

    private String nickname;

    private String profileImg;
}
