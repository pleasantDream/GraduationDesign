package org.example.controller;

import org.example.pojo.Record;
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

    @PostMapping
    public String physicalMeasurement(@RequestBody Record record) throws JSONException, IOException {
        String result = recordService.physicalMeasurement(record);
        return result;
    }
//    @PostMapping("/physical")
//    public String physicalMeasurement(
//            @Param("height") float height,
//            @Param("weight") float weight,
//            @Param("bmi") float bmi,
//            @Param("userId") Integer userId,
//            @Param("categoryId")Integer categoryId
//    ){
//        String result = recordService.physicalMeasurement(height, weight, bmi, userId, categoryId);
//        return result;
//    }
}
