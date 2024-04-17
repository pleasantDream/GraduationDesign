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
import java.util.Map;


/**
 * @author TZH
 */
@Service
public class RecordServiceImpl implements RecordService {

    private final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public String recordPhysical(Physical physical) throws JSONException, IOException {
        // 多次字符串拼接场景下StringBuilder比String更快
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了体格检查的体检项目,结果为:\n性别:");
        sb.append(physical.getGender()).append("\n年龄:").append(physical.getAge());
        sb.append("\n身高: ").append(physical.getHeight()).append("米,");
        sb.append("\n体重: ").append(physical.getWeight()).append("千克,");
        float bmi = physical.getWeight()/(physical.getHeight()*physical.getHeight());
        sb.append("\nBMI指数: ").append(bmi).append("\n");
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Physical类的一个实例中添加分析结果
        physical.setResult(result);

        physical.setBmi(bmi);
        // 往表中插入数据
        recordMapper.addPhysical(physical);
        return result;
    }

    @Override
    public String recordBlood(Blood blood) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了血液分析的体检项目,结果为:\n性别:");
        sb.append(blood.getGender()).append("\n年龄:").append(blood.getAge());
        sb.append("\n血红蛋白: ").append(blood.getHb()).append("g/l,");
        sb.append("\n血细胞计数: ").append(blood.getWbc()).append("*10^9/l,");
        sb.append("\n血小板计数: ").append(blood.getPlt()).append("*10^9/l,");
        sb.append("\n血糖: ").append(blood.getGlucose()).append("mg/dl\n");
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Physical类的一个实例中添加分析结果
        blood.setResult(result);
        // 往表中插入数据
        recordMapper.addBlood(blood);
        return result;

    }

    @Override
    public String recordPressure(Pressure pressure) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了血压测量的体检项目,结果为:\n性别:");
        sb.append(pressure.getGender()).append("\n年龄:").append(pressure.getAge());
        sb.append("\n高压: ").append(pressure.getHighPressure()).append("mmHg,");
        sb.append("\n低压: ").append(pressure.getLowPressure()).append("mmHg\n");
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Physical类的一个实例中添加分析结果
        pressure.setResult(result);
        // 往表中插入数据
        recordMapper.addPressure(pressure);
        return result;
    }

    @Override
    public String recordTemperature(Temperature temperature) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了体温测量的体检项目,结果为:\n性别:");
        sb.append(temperature.getGender()).append("\n年龄:").append(temperature.getAge());
        sb.append("\n体温: ").append(temperature.getTemperature()).append("摄氏度\n");
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Physical类的一个实例中添加分析结果
        temperature.setResult(result);
        // 往表中插入数据
        recordMapper.addTemperature(temperature);
        return result;
    }

    @Override
    public String recordUrine(Urine urine) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在我完成了尿液分析的体检项目,结果为:\n性别:");
        sb.append(urine.getGender()).append("\n年龄:").append(urine.getAge());
        sb.append("\n尿液比重: ").append(urine.getSg()).append(",");
        sb.append("\n尿液PH值: ").append(urine.getPh()).append(",");
        sb.append("\n蛋白质: ").append(urine.getProtein()).append("mg/dl,");
        sb.append("\n酮体: ").append(urine.getKet()).append(",");
        sb.append("\n潜血: ").append(urine.getBld()).append(",");
        sb.append("\n白细胞酯酶: ").append(urine.getLe()).append("U/l,");
        sb.append("\n细胞计数: ").append(urine.getCc()).append("亿cells/ml\n");
        sb.append("请你分析我的体检结果,并给出对应的的建议。50-100字");
        String content = sb.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(content);
        System.out.println(result);
        // 往Physical类的一个实例中添加分析结果
        urine.setResult(result);
        // 往表中插入数据
        recordMapper.addUrine(urine);
        return result;
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

    public String wenXinYiYan(String content) throws IOException, JSONException {
        System.out.println("User:"+content);
        String accessToken = getAccessToken();
        MediaType mediaType = MediaType.parse("application/json");
        // 原始json字符串
        String jsonStr = "{\"messages\":[{\"role\":\"user\",\"content\":\"\"}]}";
        // 将 JSON 字符串解析为 JSONObject
        JSONObject json = new JSONObject(jsonStr);
        // 更新 content 字段的值
        JSONArray messagesArray = json.getJSONArray("messages");
        JSONObject messageObj = messagesArray.getJSONObject(0);
        messageObj.put("content", content);
        // 获取更新后的 JSON 字符串
        String updatedJsonStr = json.toString();


        RequestBody body = RequestBody.create(mediaType, updatedJsonStr);
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
