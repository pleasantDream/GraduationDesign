package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.pojo.*;
import org.example.service.RecordService;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * @author TZH
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;


    @PostMapping("/physical")
    public String recordPhysical(@RequestBody Physical physical) throws JSONException, IOException {
        String result = recordService.recordPhysical(physical);
        return result;
    }

    @GetMapping("/physical/get")
    public Physical recordPhysicalGet() throws JSONException, JsonProcessingException {
        System.out.println("获取体格测量数据");
        Physical physical = recordService.recordPhysicalGet();
        return physical;
    }

    @PostMapping("/blood")
    public String recordBlood(@RequestBody Blood blood) throws JSONException, IOException {
        String result = recordService.recordBlood(blood);
        return result;
    }

    @PostMapping("/pressure")
    public String recordPressure(@RequestBody Pressure pressure) throws JSONException, IOException {
        String result = recordService.recordPressure(pressure);
        return result;
    }

    @PostMapping("/temperature")
    public String recordTemperature(@RequestBody Temperature temperature) throws JSONException, IOException {
        String result = recordService.recordTemperature(temperature);
        return result;
    }

    @PostMapping("/urine")
    public String recordUrine(@RequestBody Urine urine) throws JSONException, IOException {
        String result = recordService.recordUrine(urine);
        return result;
    }
}
