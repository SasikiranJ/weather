package com.raptor.weather.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@WebFilter("/*")
public class RateLimiterFilter implements Filter {

    private static final long MAX_REQUESTS = 3;
    private static final long TIME_WINDOW = 60 * 1000;
    private final ConcurrentHashMap<String, RequestInfo> requestMap = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String clientIp = getClientIp(httpRequest);

        RequestInfo requestInfo = requestMap.getOrDefault(clientIp, new RequestInfo(0, System.currentTimeMillis()));

        long currentTime = System.currentTimeMillis();

        if (currentTime - requestInfo.timestamp > TIME_WINDOW) {
            requestInfo.timestamp = currentTime;
            requestInfo.count = 0;
        }

        if (requestInfo.count >= MAX_REQUESTS) {
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Rate limit exceeded. Please try again later.");
            return;
        }

        requestInfo.count++;
        requestMap.put(clientIp, requestInfo);

        chain.doFilter(request, response);
    }


    private String getClientIp(HttpServletRequest request) {
        // Extract the client IP address from the request
        String remoteAddr = request.getRemoteAddr();
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null) {
            return forwardedFor.split(",")[0];  // Return the first IP in the X-Forwarded-For header
        }
        return remoteAddr;

    }

    private static class RequestInfo {
        long timestamp;
        int count;

        public RequestInfo(int count, long timestamp) {
            this.timestamp = timestamp;
            this.count = count;
        }
    }


}