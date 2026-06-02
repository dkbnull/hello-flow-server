package cn.wbnull.helloflow.app.aspect;

import cn.wbnull.helloflow.app.filter.TraceIdFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口日志切面，记录Controller入参和出参
 * <p>
 *
 * @author null
 * @RequestBody 参数以JSON格式记录，其他参数以key=value格式记录
 * @date 2026-05-26
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLogAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void controllerPointcut() {
    }

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        markControllerHandled();
        long startTime = System.currentTimeMillis();

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String uri = getRequestUri();
        String params = buildParams(joinPoint);

        log.info("[请求] {} | {}.{} | {}", uri, className, methodName, params);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            long cost = System.currentTimeMillis() - startTime;
            log.error("[异常] {} | {}.{} | 耗时:{}ms | 错误:{}", uri, className, methodName, cost, e.getMessage());
            throw e;
        }

        long cost = System.currentTimeMillis() - startTime;
        String response = toJsonString(result);
        log.info("[响应] {} | {}.{} | 耗时:{}ms | {}", uri, className, methodName, cost, response);
        return result;
    }

    private void markControllerHandled() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            attributes.getRequest().setAttribute(TraceIdFilter.CONTROLLER_HANDLED, true);
        }
    }

    private String getRequestUri() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getMethod() + " " + request.getRequestURI();
        }
        return "";
    }

    private String buildParams(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();

        List<String> bodyParams = new ArrayList<>();
        List<String> otherParams = new ArrayList<>();

        for (int i = 0; i < paramNames.length; i++) {
            Object arg = args[i];
            if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                continue;
            }
            if (arg instanceof MultipartFile file) {
                otherParams.add(paramNames[i] + "=文件:" + file.getOriginalFilename());
                continue;
            }
            if (hasRequestBody(paramAnnotations[i])) {
                bodyParams.add(toJsonString(arg));
            } else if (arg != null) {
                otherParams.add(paramNames[i] + "=" + arg);
            }
        }

        StringBuilder sb = new StringBuilder();
        if (!bodyParams.isEmpty()) {
            sb.append("Body=").append(String.join(", ", bodyParams));
        }
        if (!otherParams.isEmpty()) {
            if (!sb.isEmpty()) {
                sb.append(" ");
            }
            sb.append("Params=[").append(String.join(", ", otherParams)).append("]");
        }
        return sb.toString();
    }

    private boolean hasRequestBody(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof RequestBody) {
                return true;
            }
        }
        return false;
    }

    private String toJsonString(Object obj) {
        if (obj == null) {
            return "null";
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
}
