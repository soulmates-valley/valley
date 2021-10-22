package com.soulmates.valley.feature.posting.util;

import com.soulmates.valley.common.constants.ErrorEnum;
import com.soulmates.valley.common.exception.CustomException;
import com.soulmates.valley.domain.model.doc.PostDoc;
import com.soulmates.valley.feature.posting.dto.PostDetail;
import com.soulmates.valley.feature.posting.dto.PostInfo;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostCombiner {

    private PostCombiner(){}

    public static List<PostDetail> combinePostList(List<PostDoc> postDocList, List<PostInfo> postInfoList) {
        if (postDocList.size() != postInfoList.size())
            throw new CustomException(ErrorEnum.ETC);

        sortPostDocByDateDesc(postDocList);
        sortPostNodeByDateDesc(postInfoList);

        List<PostDetail> postDetailList = new LinkedList<>();
        Iterator<PostDoc> postDocIter = postDocList.listIterator();
        Iterator<PostInfo> postInfoIter = postInfoList.listIterator();

        while (postDocIter.hasNext() && postInfoIter.hasNext()) {
            PostDoc postDoc = postDocIter.next();
            PostInfo postNode = postInfoIter.next();
            if (postDoc.getId() == postNode.getPostId()) {
                postDetailList.add(PostDetail.of(postDoc, postNode.isUserLiked()));
            }
        }

        if (postDetailList.size() != postInfoList.size())
            throw new CustomException(ErrorEnum.ETC);

        return postDetailList;
    }

    public static List<PostDoc> sortPostDocByDateDesc(List<PostDoc> postDocList) {
        postDocList.sort((o1, o2) -> {
            if (o1.getCreateDt().isAfter(o2.getCreateDt()))
                return -1;
            else if (o1.getCreateDt().isBefore(o2.getCreateDt()))
                return 1;
            else
                return 0;
        });
        return postDocList;
    }

    public static List<PostInfo> sortPostNodeByDateDesc(List<PostInfo> postNodeList) {
        postNodeList.sort((o1, o2) -> {
            if (o1.getCreateDt().isAfter(o2.getCreateDt()))
                return -1;
            else if (o1.getCreateDt().isBefore(o2.getCreateDt()))
                return 1;
            else
                return 0;
        });
        return postNodeList;
    }
}
