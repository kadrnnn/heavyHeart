package com.itlife.heavyheart.web.mvc.method.returnhandler;

import com.itlife.heavyheart.web.result.ResultFileBean;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author kex
 * @Create 2020/8/14 10:35
 * @Description
 */
public class ResultFileBeanReturnValueHandler implements HandlerMethodReturnValueHandler {
    private static final Logger log = LoggerFactory.getLogger(ResultFileBeanReturnValueHandler.class);
    private static final int DEFUALT_BUFFER_SIZE = 1024;
    private int bufferSize;

    public ResultFileBeanReturnValueHandler() {
        this((Integer)null);
    }

    public ResultFileBeanReturnValueHandler(Integer bufferSize) {
        this.bufferSize = 1024;
        if (bufferSize == null) {
            this.bufferSize = 1024;
        } else {
            this.bufferSize = bufferSize;
        }

    }

    public boolean supportsReturnType(MethodParameter methodParameter) {
        return ResultFileBean.class.isAssignableFrom(methodParameter.getParameterType());
    }

    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {
        ResultFileBean resultFileBean = (ResultFileBean)o;
        HttpServletResponse response = (HttpServletResponse)nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        response.setContentType(resultFileBean.getContentType());
        Map<String, Object> files = resultFileBean.getFiles();
        if (null != files && files.size() != 0) {
            if (files.size() == 1) {
                this.generatorFileName(resultFileBean, response);
                this.singleFile(files, response);
            } else {
                this.generatorFileName(resultFileBean, response);
                this.multiFile(files, response);
            }
        }

    }

    public void generatorFileName(ResultFileBean resultFileBean, HttpServletResponse response) throws UnsupportedEncodingException {
        String fileName = URLEncoder.encode(resultFileBean.getFileName(), StandardCharsets.UTF_8.toString());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
    }

    private void singleFile(Map<String, Object> files, HttpServletResponse response) throws Exception {
        Map.Entry<String, Object> entry = (Map.Entry)files.entrySet().iterator().next();
        Object obj = entry.getValue();
        InputStream inputStream = null;
        ServletOutputStream outputStream = null;

        try {
            inputStream = this.validateInputStream(obj);
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[this.bufferSize];

            int len;
            while((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.flush();
        } catch (IOException var12) {
            log.error("download file error", var12);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }

        log.debug("generate singleFile: success!");
    }

    private void multiFile(Map<String, Object> files, HttpServletResponse response) throws Exception {
        ZipOutputStream zipOutputStream = null;
        InputStream inputStream = null;

        try {
            zipOutputStream = new ZipOutputStream(response.getOutputStream());
            Iterator var5 = files.entrySet().iterator();

            while(var5.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var5.next();
                Object obj = entry.getValue();
                inputStream = this.validateInputStream(obj);
                ZipEntry zipEntry = new ZipEntry((String)entry.getKey());
                zipOutputStream.putNextEntry(zipEntry);
                byte[] buffer = new byte[this.bufferSize];

                int len;
                while((len = inputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
            }

            zipOutputStream.flush();
        } catch (IOException var14) {
            log.error("download file error", var14);
        } finally {
            IOUtils.closeQuietly(zipOutputStream);
            IOUtils.closeQuietly(inputStream);
        }

        log.info("generate MultiFile: success!");
    }

    private InputStream validateInputStream(Object obj) throws Exception {
        if (obj instanceof String) {
            return new FileInputStream(obj.toString());
        } else if (obj instanceof File) {
            return new FileInputStream((File)obj);
        } else if (obj instanceof InputStream) {
            return (InputStream)obj;
        } else if (obj instanceof byte[]) {
            return new ByteArrayInputStream((byte[])((byte[])obj));
        } else {
            throw new IllegalArgumentException("格式非法！");
        }
    }
}
