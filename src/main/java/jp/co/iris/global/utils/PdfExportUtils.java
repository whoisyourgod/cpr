package jp.co.iris.global.utils;

import jp.co.iris.global.aop.handler.CPException;
import jp.co.iris.global.constant.Const;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class PdfExportUtils {
    private PdfExportUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void writePdfToResponse(HttpServletResponse response, String pdfNullMessage, Supplier<byte[]> contentBytesSupplier, Supplier<String> fileNameSupplier) {
        try {
            byte[] contentBytes = contentBytesSupplier.get();
            String fileName = fileNameSupplier.get();
            String contentDisposition = String.format(Const.CONTENT_FORMAT, fileName);
            // PDFダウンロードの処理
            if (contentBytes == null || contentBytes.length == 0) {
                PdfExportUtils.writeErrorMessageToResponse(response, pdfNullMessage);
                return;
            }
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.setHeader(Const.CONTENT_DISPOSITION, contentDisposition);
            OutputStream out = response.getOutputStream();
            out.write(contentBytes);
            out.flush();
            out.close();
        } catch (CPException e) {
            LogUtils.error(e);
            PdfExportUtils.writeErrorMessageToResponse(response, e.getMessage());
        } catch (Exception e) {
            LogUtils.error(e);
            PdfExportUtils.writeErrorMessageToResponse(response, pdfNullMessage);
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
