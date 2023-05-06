package com.emotunes.emotunes.controller;

import com.emotunes.emotunes.service.SchedulingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/scheduling")
@Api("Scheduling Controller")
@RequiredArgsConstructor
public class SchedulingController {

    private final SchedulingService schedulingService;

    @Scheduled(cron = "0 0 0 * * *")
    @PutMapping(value = "re-train")
    @ApiOperation("Re-train user models")
    public void retrain() {
        schedulingService.scheduleReTraining();
    }
}
