package org.example.enums;

import lombok.Data;

/**
 * 用常量保存我文心一言账号的key和token
 * @author TZH
 */
@Data
public class ChatGptConstant {
    public static final String SECRET_KEY = "y4KeIGGnVAQ0nDBRjy6p3HuBGdRHk0Dv";
    public static final String API_KEY = "8n7iZg2LgIZiSoFsycaGkb2z";
    public static final String URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id="
            +API_KEY+"&client_secret="+SECRET_KEY;
}
