package top.senseiliu.jackson.date;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DateFormatExtend extends DateFormat {
    private static final long serialVersionUID = 1311621393045987323L;

    private static final List<DateFormat> DateFormats = Arrays.asList(
            Format.SDF_1,
            Format.SDF_2,
            Format.SDF_3,
            Format.SDF_4,
            Format.SDF_5
    );
    private static List<DateFormat> SDF_LIST = new ArrayList<>();

    static {
        SDF_LIST.addAll(DateFormats);
    }

    public DateFormatExtend(DateFormat dateFormat) {
        SDF_LIST.add(dateFormat);
    }

    /**
     * 序列化时使用的格式为sdf1
     */
    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo,
                               FieldPosition fieldPosition) {
        return SDF_LIST.get(0).format(date, toAppendTo, fieldPosition);
    }

    /**
     * 反序列化时，分别使用自定义格式化器，最后使用默认的
     */
    @Override
    public Date parse(String source, ParsePosition pos) {
        Date date = null;

        for (DateFormat df : SDF_LIST) {
            try {
                date = df.parse(source, pos);
            } catch (Exception ex) {
            }

            if (null != date) {
                break;
            }
        }

        return date;
    }

    // 此方法在objectMapper 默认的dateformat里边用到，这里也要重写
    @Override
    public Object clone() {
        DateFormat dateFormat = (DateFormat) SDF_LIST.get(SDF_LIST.size() - 1).clone();
        return new DateFormatExtend(dateFormat);
    }

}
