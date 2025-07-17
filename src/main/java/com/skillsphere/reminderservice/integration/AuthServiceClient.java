package com.skillsphere.reminderservice.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "authServiceClient", url = "${auth.service.url}")
public interface AuthServiceClient {
    @GetMapping("/api/admin/users/emails")
    Map<Long, String> getUserEmails(@RequestParam("userIds") String userIdsCsv);
}