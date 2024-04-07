package org.example.controller;


import org.apache.ibatis.annotations.Param;
import org.example.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TZH
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @PostMapping("/physical")
    public String physicalMeasurement(
            @Param("height") float height,
            @Param("weight") float weight,
            @Param("bmi") float bmi
    ){
        String result = recordService.physicalMeasurement(height, weight, bmi);
        return result;
    }

}
