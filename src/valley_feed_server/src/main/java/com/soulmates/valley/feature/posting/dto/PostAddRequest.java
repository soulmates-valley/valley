package com.soulmates.valley.feature.posting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class PostAddRequest {

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    @Size(max = 1000, message = "내용의 최대 길이는 1000입니다.")
    private String content;

    @Size(max = 3000, message = "코드의 최대 길이는 3000입니다.")
    private String code;

    private String codeType;

    @Size(max = 8, message = "이미지는 최대 8개까지 첨부가능합니다.")
    private List<MultipartFile> images;

    @URL(message = "URL형식이 올바르지 않습니다.")
    private String link;

    private String linkTitle;

    private String linkImage;

    @Size(max = 200, message = "링크내용 최대 길이는 200입니다.")
    private String linkContent;

    private String linkSiteName;

    @Size(max = 5, message = "해시태그는 5개이하로 등록할 수 있습니다.")
    private List<String> hashTag;


    public boolean isExistHashTag() {
        return hashTag != null && !hashTag.isEmpty();
    }

    public boolean isExistImages() {
        return images != null && images.get(0).getSize() > 0;
    }
}
