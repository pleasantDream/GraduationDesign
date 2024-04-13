package org.example.controller;

import org.example.pojo.*;
import org.example.service.RecordService;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
