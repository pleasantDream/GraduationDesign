package org.example.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.assertj.core.error.ShouldHaveSizeGreaterThanOrEqualTo;
import org.example.enums.ChatGptConstant;
import org.example.mapper.RecordMapper;
import org.example.pojo.*;
import org.example.service.RecordService;
import org.example.utils.ThreadLocalUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.BlockingDeque;


/**
 * @author TZH
 */
@Service
public class RecordServiceImpl implements RecordService {

    private final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public Integer recordCount(Integer startRow, String item) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Integer count = recordMapper.getSum(userId, item);
        System.out.println("剩余记录条数:");
        System.out.println(count-startRow-5);
        return count-startRow-5;
    }

    @Override
    public Result  getRecord(String item) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<LinkedHashMap> list = recordMapper.getRecordPhysical(userId,item);
        System.out.println(list);
        return Result.success(list);
    }

    @Override
    public List<History> recordHistory(Integer startRow, String item) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<History> histories = recordMapper.getHistory(userId,item, startRow);

        return histories;
    }

    @Override
    public String recordChat(String question, String item) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        // 咨询的话只查找最近的五条对话记录
        List<History> histories = recordMapper.getHistory(userId,item, 0);
        StringBuilder sb = new StringBuilder();
        sb.append("{\"messages\":[");
        for (int i = histories.size() - 1; i >= 0; i--) {
            History history = histories.get(i);
            sb.append("{\"role\": \"user\",\"content\":\"").append(history.getQuestion());
            sb.append("\"},{\"role\":\"assistant\",\"content\":\"");
            sb.append(history.getAnswer()).append("\"},");
        }
        sb.append("{\"role\":\"user\",\"content\":\"").append(question).append("\"}]}");
        String payload = sb.toString();
        System.out.println(payload);
        String answer = wenXinYiYan(payload);
        String answer2 = answer.replace("\n","");
        System.out.println(answer2);

        recordMapper.addHsitory(userId, question, answer2, item);

        return answer;
    }

    public Map<String, Object> physicalUtil(Physical physical) throws JSONException, IOException {
        // 多次字符串拼接场景下StringBuilder比String更快
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个负责我的专业医生，现在我完成了体格检查的体检项目,结果为:性别:");
        sb.append(physical.getGender()).append("年龄:").append(physical.getAge());
        sb.append("身高: ").append(physical.getHeight()).append("米,");
        sb.append("体重: ").append(physical.getWeight()).append("千克,");
        float bmi = physical.getWeight()/(physical.getHeight()*physical.getHeight());
        sb.append("BMI指数: ").append(bmi);
        sb.append("请你分析我的体检结果,并给出对应的的建议。");
        String content = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        sb2.append("{\"messages\":[{\"role\":\"user\",\"content\":\"");
        sb2.append(content);
        sb2.append("\"}]}");
        String payload = sb2.toString();

        // 得到分析建议结果
        String result = wenXinYiYan(payload);
        String result2 = result.replace("\n", "");
        System.out.println(result2);
        // 往Physical类的一个实例中添加结果
        physical.setResult(result2);
        physical.setBmi(bmi);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("physical",physical);
        map.put("question",content);
        return map;
    }

    @Override
    public Result recordPhysical(Physical physical) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Map<String, Object> map2 = physicalUtil(physical);
        Physical physical1 = (Physical) map2.get("physical");
        String question = (String)map2.get("question");
        physical1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addPhysical(physical1);
        recordMapper.addHsitory(userId, question, physical1.getResult(), "体格分析");
        return Result.success();
    }

    public Map<String, Object> BloodUtil(Blood blood) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个负责我的专业医生，现在我完成了血液分析的体检项目,结果为:性别:");
        sb.append(blood.getGender()).append("年龄:").append(blood.getAge());
        sb.append("血红蛋白: ").append(blood.getHb()).append("g/l,");
        sb.append("血细胞计数: ").append(blood.getWbc()).append("*10^11/l,");
        sb.append("血小板计数: ").append(blood.getPlt()).append("*10^12/l,");
        sb.append("血糖: ").append(blood.getGlucose()).append("mg/dl");
        sb.append("请你分析我的体检结果,并给出对应的的建议。");
        String content = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        sb2.append("{\"messages\":[{\"role\":\"user\",\"content\":\"");
        sb2.append(content);
        sb2.append("\"}]}");
        String payload = sb2.toString();
        // 得到分析建议结果,并将换行符替换掉
        String result = wenXinYiYan(payload);
        String result2 = result.replace("\n","");

        // 往Blood类的一个实例中添加分析结果
        blood.setResult(result2);
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("blood",blood);
        map.put("question", content);

        return map;
    }

    @Override
    public Result recordBlood(Blood blood) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Map<String, Object> map2 = BloodUtil(blood);
        Blood blood1 = (Blood) map2.get("blood");
        String question = (String)map2.get("question");
        blood1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addBlood(blood1);
        recordMapper.addHsitory(userId, question, blood1.getResult(), "血液分析");
        return Result.success();
    }

    public Map<String, Object> pressureUtil(Pressure pressure) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个负责我的专业医生，现在我完成了血压测量的体检项目,结果为:性别:");
        sb.append(pressure.getGender()).append("年龄:").append(pressure.getAge());
        sb.append("高压: ").append(pressure.getHighPressure()).append("mmHg,");
        sb.append("低压: ").append(pressure.getLowPressure()).append("mmHg");
        sb.append("请你分析我的体检结果,并给出对应的的建议。");
        String content = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        sb2.append("{\"messages\":[{\"role\":\"user\",\"content\":\"");
        sb2.append(content);
        sb2.append("\"}]}");
        String payload = sb2.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(payload);
        String result2 = result.replace("\n", "");
        // 往Physical类的一个实例中添加分析结果
        pressure.setResult(result2);
        Map<String,Object> map = new HashMap<>();
        map.put("pressure", pressure);
        map.put("question", content);

        return map;
    }

    @Override
    public Result recordPressure(Pressure pressure) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Map<String, Object> map2 = pressureUtil(pressure);
        Pressure pressure1 = (Pressure) map2.get("pressure");
        String question = (String) map2.get("question");
        pressure1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addPressure(pressure1);
        recordMapper.addHsitory(userId, question, pressure1.getResult(), "血压分析");
        return Result.success();
    }

    public Map<String, Object> temperatureUtil(Temperature temperature) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个负责我的专业医生，现在我完成了体温测量的体检项目,结果为:性别:");
        sb.append(temperature.getGender()).append("年龄:").append(temperature.getAge());
        sb.append("体温: ").append(temperature.getTemperature()).append("摄氏度");
        sb.append("请你分析我的体检结果,并给出对应的的建议。");
        String content = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        sb2.append("{\"messages\":[{\"role\":\"user\",\"content\":\"");
        sb2.append(content);
        sb2.append("\"}]}");
        String payload = sb2.toString();

        // 得到分析建议结果,并将换行符替换掉
        String result = wenXinYiYan(payload);
        String result2 = result.replace("\n", "");

        // 往Physical类的一个实例中添加分析结果
        temperature.setResult(result2);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("temperature", temperature);
        map.put("question",content);

        return map;
    }

    @Override
    public Result recordTemperature(Temperature temperature) throws JSONException, IOException {
        Map<String, Object> temperatureMap = temperatureUtil(temperature);
        Temperature temperature1 = (Temperature)temperatureMap.get("temperature");
        String question = (String) temperatureMap.get("question");

        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        temperature1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addTemperature(temperature1);
        recordMapper.addHsitory(userId, question, temperature1.getResult(), "体温分析");
        return Result.success();
    }

    public Map<String, Object> UrineUtil(Urine urine) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个负责我的专业医生，现在我完成了尿液分析的体检项目,结果为:性别:");
        sb.append(urine.getGender()).append("年龄:").append(urine.getAge());
        sb.append("尿液比重: ").append(urine.getSg()).append(",");
        sb.append("尿液PH值: ").append(urine.getPh()).append(",");
        sb.append("蛋白质: ").append(urine.getProtein()).append("mg/dl,");
        sb.append("白细胞酯酶: ").append(urine.getLe()).append("U/l");
        sb.append("请你分析我的体检结果,并给出对应的的建议。");
        String content = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        sb2.append("{\"messages\":[{\"role\":\"user\",\"content\":\"");
        sb2.append(content);
        sb2.append("\"}]}");
        String payload = sb2.toString();
        // 得到分析建议结果
        String result = wenXinYiYan(payload);
        String result2 = result.replace("\n", "");
        // 往Physical类的一个实例中添加分析结果
        urine.setResult(result2);
        Map<String,Object> map = new HashMap<>();
        map.put("urine",urine);
        map.put("question",content);

        return map;
    }

    @Override
    public Result recordUrine(Urine urine) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Map<String ,Object> map2 = UrineUtil(urine);
        Urine urine1 = (Urine) map2.get("urine");
        String question = (String) map2.get("question");
        urine1.setUserId(userId);
        // 往表中插入数据
        recordMapper.addUrine(urine1);
        recordMapper.addHsitory(userId, question, urine1.getResult(), "尿液分析");
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

        Map<String, Object> map2 = physicalUtil(physical);
        Physical physical1 = (Physical) map2.get("physical");
        String question = (String) map2.get("question");
        physical1.setUserId(userId);

        Integer count = recordMapper.getCount(userId,"tb_physical");
        if (count < 5){
            // 记录小于五条直接插入
            recordMapper.physicalUpdate(physical1);
        }else {
            // 删除最早的一条记录然后插入
            recordMapper.deleteCount(userId,"tb_physical");
            recordMapper.physicalUpdate(physical1);
        }
        // 重置咨询历史
        recordMapper.deleteHistory(userId, "体格分析");
        recordMapper.addHsitory(userId, question, physical1.getResult(), "体格分析");

        return Result.success();
    }

    @Override
    public Result recordBloodUpdate(Blood blood) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Map<String, Object> map2 = BloodUtil(blood);
        Blood blood1 = (Blood)map2.get("blood");
        String question = (String)map2.get("question");
        blood1.setUserId(userId);

        recordMapper.bloodUpdate(blood1);
        recordMapper.deleteHistory(userId,"血液分析");
        recordMapper.addHsitory(userId, question, blood1.getResult(),"血液分析");
        return Result.success();
    }

    @Override
    public Result recordPressureUpdate(Pressure pressure) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Map<String,Object> map2 = pressureUtil(pressure);
        Pressure pressure1 = (Pressure) map2.get("pressure");
        String question = (String) map2.get("question");
        pressure1.setUserId(userId);
        recordMapper.pressureUpdate(pressure1);
        recordMapper.deleteHistory(userId,"血压分析");
        recordMapper.addHsitory(userId, question, pressure1.getResult(), "血压分析");
        return Result.success();
    }

    @Override
    public Result recordTemperatureUpdate(Temperature temperature) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        Map<String, Object> temperatureMap = temperatureUtil(temperature);

        Temperature temperature1 = (Temperature)temperatureMap.get("temperature");
        temperature1.setUserId(userId);
        String question = (String) temperatureMap.get("question");

        recordMapper.temperatureUpdate(temperature1);
        recordMapper.deleteHistory(userId, "体温分析");
        recordMapper.addHsitory(userId, question, temperature1.getResult(),"体温分析");

        return Result.success();
    }

    @Override
    public Result recordUrineUpdate(Urine urine) throws JSONException, IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        Map<String,Object> map2 = UrineUtil(urine);
        Urine urine1 = (Urine) map2.get("urine");
        String question = (String) map2.get("question");
        urine1.setUserId(userId);

        recordMapper.urineUpdate(urine1);
        recordMapper.deleteHistory(userId, "尿液分析");
        recordMapper.addHsitory(userId, question, urine1.getResult(), "尿液分析");
        return Result.success();
    }

    @Override
    public void test(String message) throws JSONException, IOException {
        String sb="{\"messages\":[{\"role\": \"user\",\"content\":\"你是一个专业医生，现在我完成了体温测量的体检项目,结果为:性别:男年龄:21体温: 38.5摄氏度请你分析我的体检结果,并给出对应的的建议。50-100字\"},{\"role\":\"assistant\",\"content\":\"根据您的体温测量结果，您可能处于轻度发热状态。这种情况可能与多种因素有关，如感染、炎症、环境温度等。为了进一步评估您的健康状况，建议您咨询医生进行进一步的检查和评估."+
                "如果您感到不适，或者认为您的健康状况有进一步恶化，建议您尽快就医并告诉医生您的症状和病史。在接受治疗之前，请务必确保您提供给医生准确的健康信息。同时，如果您觉得您可能需要额外的照顾或者咨询，请不要犹豫寻求帮助。" +
                "此外，建议您保持良好的生活习惯，如保持充足的睡眠、多喝水、避免过度劳累和压力等。这些措施有助于提高您的免疫力，促进身体的恢复。同时，请注意观察自己的身体状况，如有异常症状或体征，请及时就医。\"},{\"role\": \"user\",\"content\":\"我该怎么降温呢\"},{\"role\":\"assistant\",\"content\":\"您可以洗个热水澡,吃感冒药\"},{\"role\":\"user\",\"content\":\"体温多少为正常范围\"}]}    ";
        String result =  wenXinYiYan(sb);
        System.out.println(result);
    }


    public String wenXinYiYan(String payload) throws IOException, JSONException {
        String accessToken = getAccessToken();
        MediaType mediaType = MediaType.parse("application/json");
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
