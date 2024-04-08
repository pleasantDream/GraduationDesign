package org.example.service.impl;

import okhttp3.*;
import org.example.enums.ChatGptConstant;
import org.example.service.RecordService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;
import org.example.pojo.Record;

/**
 * @author TZH
 */
@Service
public class RecordServiceImpl implements RecordService {

    private final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Override
    public String physicalMeasurement(Record record) throws JSONException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("你是一个专业医生，现在有一个用户完成了").append(record.getName()).append("体检项目,结果为:\n性别:");
        sb.append(record.getGender()).append("\n年龄:").append(record.getAge());
        if (record.getHeight() != 0){
            sb.append("\n身高: ").append(record.getHeight()).append("米,");
        }
        if (record.getWeight() != 0){
            sb.append("\n体重: ").append(record.getWeight()).append("千克,");
        }
        if (record.getBmi() != 0){
            sb.append("\nBMI指数: ").append(record.getBmi()).append(",");
        }
        if (record.getHighPressure() != 0){
            sb.append("\n高压: ").append(record.getHighPressure()).append("mmHg,");
        }
        if (record.getLowPressure() != 0){
            sb.append("\n低压: ").append(record.getLowPressure()).append("mmHg,");
        }
        if (record.getTemperature() != 0){
            sb.append("\n体温: ").append(record.getTemperature()).append("摄氏度,");
        }
        if (record.getHb() != 0){
            sb.append("\n血红蛋白: ").append(record.getHb()).append("g/l,");
        }
        if (record.getWbc() != 0){
            sb.append("\n血细胞计数: ").append(record.getWbc()).append("*10^9/l,");
        }
        if (record.getPlt() != 0){
            sb.append("\n血小板计数: ").append(record.getPlt()).append("*10^9/l,");
        }
        if (record.getGlucose() != 0){
            sb.append("\n血糖: ").append(record.getGlucose()).append("mg/dl,");
        }
        if (record.getSg() != 0){
            sb.append("\n尿液比重: ").append(record.getSg()).append(",");
        }
        if (record.getPh() != 0){
            sb.append("\n尿液PH值: ").append(record.getPh()).append(",");
        }
        if (record.getProtein() != 0){
            sb.append("\n蛋白质: ").append(record.getProtein()).append("mg/dl,");
        }
        if (record.getKet() != null){
            sb.append("\n酮体: ").append(record.getKet()).append(",");
        }
        if (record.getBld() != null){
            sb.append("\n潜血: ").append(record.getBld()).append(",");
        }
        if (record.getLe() != 0){
            sb.append("\n白细胞酯酶: ").append(record.getLe()).append("U/l,");
        }
        if (record.getCc() != 0){
            sb.append("\n细胞计数: ").append(record.getCc()).append("cells/ml,");
        }
        // 删除末尾多余的逗号
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
        }
        sb.append("请你当面分析其体检结果,并给出对应的的建议。在100字以内");
        String content = sb.toString();
        String result = wenXinYiYan(content);
        System.out.println("结果是:"+result);
        return null;
    }

    public String wenXinYiYan(String content) throws IOException, JSONException {
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
