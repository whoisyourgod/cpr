package jp.co.iris.global.utils;

import com.ibm.icu.text.SimpleDateFormat;
import jp.co.iris.global.constant.Const;
import org.springframework.util.Assert;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

public class FileNameUtils {
    private FileNameUtils() {
        throw new IllegalStateException("Utility class");
    }
    public static String formatFileName(String prefix, String suffix, Supplier<Map<String, Object>> viewsInfoSupplier) {
        Object labName = viewsInfoSupplier.get().get(Const.LAB_NM);
        Assert.notNull(prefix, "Prefix is null");
        Assert.notNull(labName, "LabName is null");
        Assert.notNull(suffix, "Suffix is null");
        SimpleDateFormat fromat = new SimpleDateFormat(DateUtils.FRM_YMD);
        Date date = new Date();
        String today = fromat.format(date);
        return prefix.concat(Const.UN_BAR).concat(URLEncoder.encode(labName.toString(), StandardCharsets.UTF_8)).concat(Const.UN_BAR).concat(today).concat(suffix);
    }
}
