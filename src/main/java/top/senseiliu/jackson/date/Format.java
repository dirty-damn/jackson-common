package top.senseiliu.jackson.date;

import java.text.SimpleDateFormat;

public final class Format {
    public static final SimpleDateFormat SDF_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SDF_2 = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat SDF_3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final SimpleDateFormat SDF_4 = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SDF_5 = new SimpleDateFormat("yyyyMMdd");

    private Format() {
    }

}
