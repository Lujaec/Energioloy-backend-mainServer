package com.example.backendmainserver.auth.presentation;

import com.example.backendmainserver.global.exception.GlobalException;
import com.example.backendmainserver.global.response.ErrorCode;
import com.example.backendmainserver.global.security.jwt.JwtProvider;
import com.example.backendmainserver.user.application.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        String accessToken = AuthorizationExtractor.extract(request);

        jwtProvider.validateToken(accessToken);
        Long id = jwtProvider.extractId(accessToken).orElseThrow(
                () -> {
                    throw new GlobalException(ErrorCode.INVALID_JWT_ACCESS_TOKEN);
                }
        );

        return userService.getUser(id);
    }
}
