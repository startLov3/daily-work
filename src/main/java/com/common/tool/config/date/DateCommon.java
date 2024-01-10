package com.common.tool.config.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/*获取日期*/
public class DateCommon {

    public static void main(String[] args) {
        //获取昨天的日期
        System.out.println("获取昨天的日期:" + yesterdayDateStr("yyyy年MM月dd日"));
    }

    public static String yesterdayDateStr(String format) {
        Calendar calendar = Calendar.getInstance();
        // 将日期调整到昨天
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        // 定义 SimpleDateFormat 对象，指定输出格式
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // 根据 Calendar 对象获取昨天的日期字符串
        return sdf.format(calendar.getTime());
    }

}
