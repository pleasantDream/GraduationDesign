package org.example.controller;

import ch.qos.logback.classic.joran.serializedModel.HardenedModelInputStream;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.ibatis.annotations.Param;
import org.example.pojo.*;
import org.example.service.RecordService;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import java.io.IOException;
import java.util.List;


/**
 * @author TZH
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;


    @PostMapping("/history")
    public List<History> recordHistory(@RequestParam("startRow") Integer startRow,
                                       @RequestParam("item") String item){
        System.out.println("获取咨询历史");
        List<History> histories = recordService.recordHistory(startRow, item);

        return histories;
    }
    @PostMapping("/count")
    public Integer recordCount(@RequestParam("startRow") Integer startRow,
                               @RequestParam("item") String item){
        System.out.println("剩余记录条数");
        Integer count = recordService.recordCount(startRow, item);
        return count;
    }
    @PostMapping("/chat")
    public String recordChat(@RequestParam("question") String question,
                             @RequestParam("item") String item) throws JSONException, IOException {
        System.out.println("体检咨询聊天");
        String answer = recordService.recordChat(question, item);

        return answer;
    }



    @PostMapping("/physical")
    public Result recordPhysical(@RequestBody Physical physical) throws JSONException, IOException {
        System.out.println("新增体格测量数据");
        System.out.println(physical.getHeight());
        Result result = recordService.recordPhysical(physical);
        return result;
    }

    @GetMapping("/physical/get")
    public Physical recordPhysicalGet() throws JSONException, JsonProcessingException {
        System.out.println("获取体格测量数据");
        Physical physical = recordService.recordPhysicalGet();
        return physical;
    }

    @PostMapping("/physical/update")
    public Result recordPhysicalUpdate(@RequestBody Physical physical) throws JSONException, IOException {
        System.out.println("更新体格测量数据");
        Result result = recordService.recordPhysicalUpdate(physical);

        return result;
    }


    @PostMapping("/blood")
    public Result recordBlood(@RequestBody Blood blood) throws JSONException, IOException {
        Result result = recordService.recordBlood(blood);
        return result;
    }
    @GetMapping("/blood/get")
    public Blood recordBloodGet() throws JSONException, JsonProcessingException {
        System.out.println("获取血液分析数据");
        Blood blood = recordService.recordBloodGet();
        return blood;
    }

    @PostMapping("/blood/update")
    public Result recordBloodUpdate(@RequestBody Blood blood) throws JSONException, IOException {
        System.out.println("更新血液分析数据");
        Result result = recordService.recordBloodUpdate(blood);

        return result;
    }

    @PostMapping("/pressure")
    public Result recordPressure(@RequestBody Pressure pressure) throws JSONException, IOException {
        Result result = recordService.recordPressure(pressure);
        return result;
    }
    @GetMapping("/pressure/get")
    public Pressure recordPressureGet() throws JSONException, JsonProcessingException {
        System.out.println("获取血压测量数据");
        Pressure pressure = recordService.recordPressureGet();
        return pressure;
    }
    @PostMapping("/pressure/update")
    public Result recordPressureUpdate(@RequestBody Pressure pressure) throws JSONException, IOException {
        System.out.println("更新血压测量数据");
        Result result = recordService.recordPressureUpdate(pressure);
        return result;
    }


    @PostMapping("/temperature")
    public Result recordTemperature(@RequestBody Temperature temperature) throws JSONException, IOException {
        Result result = recordService.recordTemperature(temperature);
        return result;
    }
    @GetMapping("/temperature/get")
    public Temperature recordTemperatureGet() throws JSONException, JsonProcessingException {
        System.out.println("获取体温数据");
        Temperature temperature = recordService.recordTemperatureGet();
        return temperature;
    }

    @PostMapping("/temperature/update")
    public Result recordTemperatureUpdate(@RequestBody Temperature temperature) throws JSONException, IOException {
        System.out.println("更新体温测量数据");
        Result result = recordService.recordTemperatureUpdate(temperature);
        return result;
    }


    @PostMapping("/urine")
    public Result recordUrine(@RequestBody Urine urine) throws JSONException, IOException {
        Result result = recordService.recordUrine(urine);
        return result;
    }
    @GetMapping("/urine/get")
    public Urine recordUrineGet() throws JSONException, JsonProcessingException {
        System.out.println("获取尿液分析数据");
        Urine urine = recordService.recordUrineGet();
        return urine;
    }

    @PostMapping("/urine/update")
    public Result recordUrineUpdate(@RequestBody Urine Urine) throws JSONException, IOException {
        System.out.println("更新尿液测分析数据");
        Result result = recordService.recordUrineUpdate(Urine);
        return result;
    }
    @PostMapping("/test")
    public void test(@RequestParam("message") String message) throws JSONException, IOException {
        System.out.println("测试");
        recordService.test(message);
    }

    @GetMapping("/getRecord")
    public Result getRecord(@RequestParam("item") String item){
        System.out.println("获取体检记录: "+item);
        return recordService.getRecord(item);
    }
}
