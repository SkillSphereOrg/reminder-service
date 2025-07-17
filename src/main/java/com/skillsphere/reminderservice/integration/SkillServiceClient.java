package com.skillsphere.reminderservice.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "skillServiceClient", url = "${skill.service.url}")
public interface SkillServiceClient {
    @GetMapping("/api/v1/practice-logs/user/inactive")
    List<Long> getInactiveUserIds(@RequestParam("days") int days);
}