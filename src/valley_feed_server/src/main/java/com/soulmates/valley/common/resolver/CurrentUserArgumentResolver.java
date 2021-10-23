package com.soulmates.valley.common.resolver;

import com.soulmates.valley.common.dto.UserInfo;
import com.soulmates.valley.common.util.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("--- [CurrentUserArgumentResolver] token : {}", token);
        UserInfo currentUser = JWTParser.getUsersFromJWT(token);
        log.info("--- [currentUser] userId : {}", currentUser.getUserId());
        return currentUser;
    }
}
