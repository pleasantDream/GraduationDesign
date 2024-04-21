package org.example.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.example.enums.ChatGptConstant;
import org.example.mapper.RecordMapper;
import org.example.pojo.*;
import org.example.service.RecordService;
import org.example.utils.ThreadLocalUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;


/**
 * @author TZH
 */
@Service
public class RecordServiceImpl implements RecordService {

    private final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Autowired
    private RecordMapper recordMapper;

    public Physical physicalUtil(Physical physical) throws JSONException, IOException {
        // 多次字符串拼接场景下StringBuilder比String更快
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了体格检查的体检项目,结果为:性别:");
        sb.append(physical.getGender()).append("年龄:").append(physical.getAge());
        sb.append("身高: ").append(physical.getHeight()).append("米,");
        sb.append("体重: ").append(physical.getWeight()).append("千克,");
        float bmi = physical.getWeight()/(physical.getHeight()*physical.getHeight());
        sb.append("BMI指数: ").append(bmi);
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Physical类的一个实例中添加结果
        physical.setResult(result);
        physical.setBmi(bmi);
        return physical;
    }

    @Override
    public Result recordPhysical(Physical physical) throws JSONException, IOException {
        Physical physical1 = physicalUtil(physical);

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        physical1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addPhysical(physical1);
        return Result.success();
    }

    public Blood BloodUtil(Blood blood) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了血液分析的体检项目,结果为:性别:");
        sb.append(blood.getGender()).append("\n年龄:").append(blood.getAge());
        sb.append("血红蛋白: ").append(blood.getHb()).append("g/l,");
        sb.append("血细胞计数: ").append(blood.getWbc()).append("*10^9/l,");
        sb.append("血小板计数: ").append(blood.getPlt()).append("*10^9/l,");
        sb.append("血糖: ").append(blood.getGlucose()).append("mg/dl");
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Blood类的一个实例中添加分析结果
        blood.setResult(result);

        return blood;
    }

    @Override
    public Result recordBlood(Blood blood) throws JSONException, IOException {
        Blood blood1 = BloodUtil(blood);

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        blood1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addBlood(blood1);
        return Result.success();
    }

    public Pressure pressureUtil(Pressure pressure) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了血压测量的体检项目,结果为:性别:");
        sb.append(pressure.getGender()).append("\n年龄:").append(pressure.getAge());
        sb.append("高压: ").append(pressure.getHighPressure()).append("mmHg,");
        sb.append("低压: ").append(pressure.getLowPressure()).append("mmHg");
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Physical类的一个实例中添加分析结果
        pressure.setResult(result);

        return pressure;
    }

    @Override
    public Result recordPressure(Pressure pressure) throws JSONException, IOException {
        Pressure pressure1 = pressureUtil(pressure);

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        pressure1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addPressure(pressure1);
        return Result.success();
    }

    public Map<String, Object> temperatureUtil(Temperature temperature) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了体温测量的体检项目,结果为:性别:");
        sb.append(temperature.getGender()).append("年龄:").append(temperature.getAge());
        sb.append("体温: ").append(temperature.getTemperature()).append("摄氏度");
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Physical类的一个实例中添加分析结果
        temperature.setResult(result);
        Map<String, Object> map = new HashMap<>();
        map.put("temperature", temperature);
        map.put("userMessage",content);

        return map;
    }

    @Override
    public Result recordTemperature(Temperature temperature) throws JSONException, IOException {
        Map<String, Object> temperatureMap = temperatureUtil(temperature);
        Temperature temperature1 = (Temperature)temperatureMap.get("temperature");
        String userMessage = (String) temperatureMap.get("userMessage");

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        temperature1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addTemperature(temperature1);
        recordMapper.addHsitory(userId, userMessage, temperature1.getResult());
        return Result.success();
    }

    public Urine UrineUtil(Urine urine) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了尿液分析的体检项目,结果为:性别:");
        sb.append(urine.getGender()).append("年龄:").append(urine.getAge());
        sb.append("尿液比重: ").append(urine.getSg()).append(",");
        sb.append("尿液PH值: ").append(urine.getPh()).append(",");
        sb.append("蛋白质: ").append(urine.getProtein()).append("mg/dl,");
        sb.append("白细胞酯酶: ").append(urine.getLe()).append("U/l");
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Physical类的一个实例中添加分析结果
        urine.setResult(result);

        return urine;
    }

    @Override
    public Result recordUrine(Urine urine) throws JSONException, IOException {
        Urine urine1 = UrineUtil(urine);

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        urine1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addUrine(urine1);
        return Result.success();
    }

    @Override
    public Physical recordPhysicalGet() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Physical physical = recordMapper.getPhysical(userId);
        System.out.println(physical);

        return physical;
    }

    @Override
    public Pressure recordPressureGet() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Pressure pressure = recordMapper.getPressure(userId);
        System.out.println(pressure);

        return pressure;
    }

    @Override
    public Blood recordBloodGet() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Blood blood = recordMapper.getBlood(userId);
        System.out.println(blood);

        return blood;
    }

    @Override
    public Temperature recordTemperatureGet() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Temperature temperature = recordMapper.getTemperature(userId);
        System.out.println(temperature);

        return temperature;
    }

    @Override
    public Urine recordUrineGet() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Urine urine = recordMapper.getUrine(userId);
        System.out.println(urine);

        return urine;
    }

    @Override
    public Result recordPhysicalUpdate(Physical physical) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Physical physical1 = physicalUtil(physical);
        physical1.setUserId(userId);
        recordMapper.physicalUpdate(physical1);

        System.out.println(Result.success());
        return Result.success();
    }

    @Override
    public Result recordBloodUpdate(Blood blood) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Blood blood1 = BloodUtil(blood);
        blood1.setUserId(userId);
        recordMapper.bloodUpdate(blood1);
        return Result.success();
    }

    @Override
    public Result recordPressureUpdate(Pressure pressure) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Pressure pressure1 = pressureUtil(pressure);
        pressure1.setUserId(userId);
        recordMapper.pressureUpdate(pressure1);
        return Result.success();
    }

    @Override
    public Result recordTemperatureUpdate(Temperature temperature) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Map<String, Object> temperatureMap = temperatureUtil(temperature);

        Temperature temperature1 = (Temperature)temperatureMap.get("temperature");
        temperature1.setUserId(userId);
        recordMapper.temperatureUpdate(temperature1);
        return Result.success();
    }

    @Override
    public Result recordUrineUpdate(Urine urine) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Urine urine1 = UrineUtil(urine);
        urine1.setUserId(userId);
        recordMapper.urineUpdate(urine1);
        return Result.success();
    }

    @Override
    public void test(String message) throws JSONException, IOException {
        String result =  wenXinYiYan(message);
        System.out.println(result);
    }

    @Override
    public List<History> recordTemperatureHistory() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        List<History> histories = recordMapper.getHistory(userId);
        return histories;
    }


    public String wenXinYiYan(String content) throws IOException, JSONException {
        String accessToken = getAccessToken();
        MediaType mediaType = MediaType.parse("application/json");

        StringBuilder sb = new StringBuilder();
        sb.append("{\"messages\":[{\"role\":\"user\",\"content\":\"");
        sb.append(content);
        sb.append("\"}]}");
        String payload = sb.toString();
        System.out.println(payload);

        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token="+accessToken)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("result");
    }

    public String getAccessToken() throws IOException, JSONException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(ChatGptConstant.URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }

}
