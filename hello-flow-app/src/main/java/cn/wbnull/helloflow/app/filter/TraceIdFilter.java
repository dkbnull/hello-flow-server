package cn.wbnull.helloflow.app.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * TraceId过滤器，为每次请求生成唯一追踪ID并放入MDC，
 * 同时记录在Security等过滤器阶段被拒绝的请求
 *
 * @author null
 * @date 2026-06-01
 */
@Slf4j
@Component
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID = "traceId";
    public static final String CONTROLLER_HANDLED = "__controllerHandled";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = generateTraceId();
        MDC.put(TRACE_ID, traceId);

        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = getClientIp(request);

        try {
            filterChain.doFilter(request, response);
        } finally {
            if (request.getAttribute(CONTROLLER_HANDLED) == null) {
                long cost = System.currentTimeMillis() - startTime;
                String fullUri = queryString != null ? uri + "?" + queryString : uri;
                log.warn("[拒绝] {} {} | 来源:{} | 状态:{} | 耗时:{}ms",
                        method, fullUri, clientIp, response.getStatus(), cost);
            }
            MDC.remove(TRACE_ID);
        }
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
