package jp.co.iris.global.utils;

import jp.co.iris.global.aop.handler.CPException;
import jp.co.iris.global.constant.Const;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class CsvExportUtils {
    private CsvExportUtils() {
        throw new IllegalStateException("Utility class");
    }
    public static void writeCsvToResponse(HttpServletResponse response, String csvNullMessage, Supplier<File> contentBytesSupplier, Supplier<String> fileNameSupplier) {
        try {
            File csvFile = contentBytesSupplier.get();
            String fileName = fileNameSupplier.get();
            response.setHeader(Const.CONTENT_DISPOSITION, String.format(Const.CONTENT_FORMAT, fileName));
            try (OutputStream out = response.getOutputStream()) {
                try (InputStream in = new FileInputStream(csvFile)) {
                    byte[] buffer = new byte[10240];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                }
            }
        } catch (CPException e) {
            LogUtils.error(e);
            CsvExportUtils.writeErrorMessageToResponse(response, e.getMessage());
        } catch (Exception e) {
            LogUtils.error(e);
            CsvExportUtils.writeErrorMessageToResponse(response, csvNullMessage);
        }
    }

    public static void writeErrorMessageToResponse(HttpServletResponse response, String errorMessage) {
        response.setContentType(Const.CONTENT_TYPE);
        try {
            response.getOutputStream().write(errorMessage.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            LogUtils.error(ex);
        }
    }
}
