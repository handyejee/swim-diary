package livoi.swimdiary.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import livoi.swimdiary.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 클라이언트의 요청을 처리할 때 JWT 토큰을 확인하여 인증 정보를 설정하는 필터를 설정하기 위한 클래스입니다.
 * 스프링 시큐리티의 OncePerRequestFilter를 상속받아 매 요청당 한 번씩 실행
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter{

  private final TokenProvider tokenProvider;
  private final static String HEADER_AUTHORIZATION = "Authorization";
  private final static String TOKEN_PREFIX = "Bearer ";

  /**
   * 클라이언트의 요청에서 Authorization 헤더를 읽어와 JWT 토큰을 확인한 후,
   * 유효한 토큰일 경우 해당 토큰을 기반으로 사용자 인증 정보를 SecurityContext에 설정합니다.
   *
   * @param request 클라이언트의 요청 정보
   * @param response 서버에서 클라이언트로 보낼 응답 정보
   * @param filterChain 요청을 처리하는 필터 체인
   * @throws ServletException 서블릿 예외 발생 시 예외 발생
   * @throws IOException I/O 처리 중 오류 발생 시 예외 발생
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
    String token = getAccessToken(authorizationHeader);

    if (tokenProvider.validToken(token)) {
      Authentication authentication = tokenProvider.getAuthentication(token);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }

  /**
   * Authorization 헤더에서 Bearer 토큰을 추출합니다.
   *
   * @param authorizationHeader 요청 헤더에 포함된 Authorization 값
   * @return Bearer 토큰이 있는 경우 토큰 문자열을 반환, 없으면 null 반환
   */
  private String getAccessToken(String authorizationHeader){
    if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
      return authorizationHeader.substring(TOKEN_PREFIX.length());
    }
    return null;
  }

}
