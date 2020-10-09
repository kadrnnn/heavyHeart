package com.itlife.heavyheart.utils;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kex
 * @date 2019/6/6
 * @Description api服务调用
 */
public class RestTemplateUtil {
    private static final Logger log = LoggerFactory.getLogger(RestTemplateUtil.class);
    private static RestTemplate REST = null;

    static {
        try {
            HttpClientBuilder b = HttpClientBuilder.create();
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
            b.setSSLContext(sslContext);
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslSocketFactory)
                    .build();
            PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connMgr.setMaxTotal(200);
            connMgr.setDefaultMaxPerRoute(100);
            b.setConnectionManager(connMgr);
            CloseableHttpClient client = b.build();
            HttpComponentsClientHttpRequestFactory httpsFactory = new HttpComponentsClientHttpRequestFactory(client);
            REST = new RestTemplate(httpsFactory);
            REST.setErrorHandler(new ResponseErrorHandler() {
                @Override
                public boolean hasError(ClientHttpResponse clientHttpResponse) {
                    return false;
                }

                @Override
                public void handleError(ClientHttpResponse clientHttpResponse) {
                    //默认处理非200的返回，会抛异常
                }
            });
        } catch (Exception e) {
            log.error("api服务调用异常", e);
        }
    }

    /**
     * postRequest请求
     *
     * @param apiUrl 地址
     * @param map    参数
     * @return 返回数据
     */
    public static ResponseEntity<String> postRequest(String apiUrl, Map map) {
        String url = apiUrl + spliceUrl(map);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/plain;charset=UTF-8"));
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<String> stringResponseEntity = REST.postForEntity(url, httpEntity, String.class);
        return stringResponseEntity;
    }

    /**
     * postRequest请求
     *
     * @param apiUrl 地址
     * @param map    参数
     * @return 返回数据
     */
    public static ResponseEntity<Object> postRequestObject(String apiUrl, Map map) {
        String url = apiUrl + spliceUrl(map);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/plain;charset=UTF-8"));
        HttpEntity httpEntity = new HttpEntity(headers);
        ResponseEntity<Object> stringResponseEntity = REST.postForEntity(url, httpEntity, Object.class);
        return stringResponseEntity;
    }

    /**
     * form-data请求
     *
     * @param url url
     * @param map 参数集合
     * @return 回复结果
     */
    public static ResponseEntity<String> sendRequest(String url, Map<String, Object> map) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(list);
        HttpEntity httpEntity = new HttpEntity(getMultiValueMap(map), headers);
        ResponseEntity<String> responseEntity = REST.postForEntity(url, httpEntity, String.class);
        return responseEntity;
    }


    /**
     * json请求
     *
     * @param url url
     * @param map 参数集合
     * @return 回复结果
     */
    public static ResponseEntity<String> sendRequestMap(String url, Map<String, Object> map) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(list);
        HttpEntity httpEntity = new HttpEntity(map, headers);
        ResponseEntity<String> responseEntity = REST.postForEntity(url, httpEntity, String.class);
        return responseEntity;
    }

    /**
     * json请求
     *
     * @param url    url
     * @param header 头部信息
     * @return 回复结果
     */
    public static String sendRequestMapWithHead(String url, Map<String, String> header) {
        try {
            StringBuffer sb = new StringBuffer();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            for (Map.Entry<String, String> entry : header.entrySet()) {
                headers.add(entry.getKey(), entry.getValue());
            }
            List<MediaType> list = new ArrayList<>();
            list.add(MediaType.APPLICATION_JSON_UTF8);
            headers.setAccept(list);
            HttpEntity httpEntity = new HttpEntity(null, headers);
            ResponseEntity<String> exchange = REST.exchange(new URI(url), HttpMethod.GET, httpEntity, String.class);
            return exchange.getBody();
        } catch (URISyntaxException e) {
            log.error("api服务调用异常", e);
        }
        return null;
    }

    /**
     * json请求
     *
     * @param url    url
     * @param header 头部信息
     * @return 回复结果
     */
    public static String sendRequestWithHead(String url, Map<String, String> body, Map<String, String> header) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            headers.add(entry.getKey(), entry.getValue());
        }
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(list);
        HttpEntity httpEntity = new HttpEntity(body, headers);
        ResponseEntity<String> exchange = REST.postForEntity(url, httpEntity, String.class);
        String bodyData = exchange.getBody();
        return bodyData;
    }

    /**
     * form-data请求
     *
     * @param url url
     * @param map 参数集合
     * @return 回复结果
     */
    public static ResponseEntity<Object> sendRequestObject(String url, Map<String, Object> map) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity httpEntity = new HttpEntity(getMultiValueMap(map), headers);
        ResponseEntity<Object> responseEntity = REST.postForEntity(url, httpEntity, Object.class);
        return responseEntity;
    }

    /**
     * 把map转为MultiValueMap
     *
     * @param map 参数Map
     * @return MultiValueMap
     */
    private static MultiValueMap<String, Object> getMultiValueMap(Map<String, Object> map) {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<String, Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            multiValueMap.add(entry.getKey(), entry.getValue());
        }
        return multiValueMap;
    }

    /**
     * 拼接url
     *
     * @param mapUrls 参数拼接
     * @return 返回数据
     */
    public static String spliceUrl(Map<String, Object> mapUrls) {
        if (mapUrls == null || mapUrls.size() <= 0) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");
        int flag = 0;
        for (Map.Entry<String, Object> entry : mapUrls.entrySet()) {
            if (flag == 1) {
                stringBuffer.append("&");
            }
            stringBuffer.append(entry.getKey() + "=" + entry.getValue());
            flag = 1;
        }
        return String.valueOf(stringBuffer);
    }

    /**
     * 拼接参数
     *
     * @param param     参数
     * @param mapParams 参数拼接
     * @return 返回数据
     */
    public static Map<String, Object> spliceParam(Map<String, Object> mapParams, String param) {
        if (mapParams == null || mapParams.size() <= 0) {
            return null;
        }
        Map<String, Object> maps = new HashMap<>();
        for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
            if ("uuid".equals(entry.getKey())) {
                continue;
            }
            String newParam = param + "." + entry.getKey();
            maps.put(newParam, entry.getValue());
        }
        return maps;
    }
}
