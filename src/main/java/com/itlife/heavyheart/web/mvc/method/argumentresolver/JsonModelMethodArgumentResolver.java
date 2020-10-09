package com.itlife.heavyheart.web.mvc.method.argumentresolver;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itlife.heavyheart.springUtils.SpringContextUtils;
import com.itlife.heavyheart.utils.ClassUtils;
import com.itlife.heavyheart.utils.JsonUtils;
import com.itlife.heavyheart.web.mvc.annotation.JsonModel;
import com.itlife.heavyheart.web.mvc.annotation.RequestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Author kex
 * @Create 2020/8/14 10:33
 * @Description
 */
public class JsonModelMethodArgumentResolver extends BaseMethodArgumentResolver {
    private static final Logger log = LoggerFactory.getLogger(JsonModelMethodArgumentResolver.class);
    private static final String REQUEST_ATTRIBUTE_JSONMODEL_NAMESPACE = "REQUEST_ATTRIBUTE_JSONMODEL_NAMESPACE";
    private Charset defaultCharset = Charset.forName("UTF-8");
    private static final String JSONTEMPLATE = "{\"innerMap\":%s}";
    private static final String DEFAULT_PAGE_PREFIX = "";
    private String pagePrefix;
    private String sortPrefix;
    private PageableHandlerMethodArgumentResolver pageResolver = (PageableHandlerMethodArgumentResolver) SpringContextUtils.getBean(PageableHandlerMethodArgumentResolver.class);
    private SortHandlerMethodArgumentResolver sortResolver = (SortHandlerMethodArgumentResolver)SpringContextUtils.getBean(SortHandlerMethodArgumentResolver.class);
    private String pageTemplate;
    private Validator validator;

    public JsonModelMethodArgumentResolver() {
        SpringDataWebProperties springDataWebProperties = (SpringDataWebProperties)SpringContextUtils.getBean(SpringDataWebProperties.class);
        SpringDataWebProperties.Pageable pageable = springDataWebProperties.getPageable();
        SpringDataWebProperties.Sort sort = springDataWebProperties.getSort();
        this.pagePrefix = pageable.getPrefix() == null ? "" : pageable.getPrefix().substring(0, pageable.getPrefix().length() - 1);
        this.sortPrefix = sort.getSortParameter();
        this.pageTemplate = pageable.getPrefix().concat("%s");
        this.validator = (Validator)SpringContextUtils.getBean(Validator.class);
    }

    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(JsonModel.class);
    }

    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        ObjectMapper objectMapper = (ObjectMapper)SpringContextUtils.getBean(ObjectMapper.class);
        HttpServletRequest request = (HttpServletRequest)nativeWebRequest.getNativeRequest();
        String contentType = request.getContentType();
        HttpInputMessage message = new ServletServerHttpRequest(request);
        Class parameterType = methodParameter.getParameterType();
        String prefixName = this.getPrefixName(methodParameter, parameterType);
        String parameterValue;
        if (null != contentType && contentType.contains("application/json")) {
            Charset charset = this.getContentCharset(message.getHeaders().getContentType());
            JsonNode tree = this.getAllParameters(request, charset, objectMapper);
            parameterValue = this.getParameterValue(tree, prefixName, parameterType);
        } else {
            parameterValue = request.getParameter(prefixName);
        }

        Object obj = this.bindData(parameterValue, parameterType, methodParameter, modelAndViewContainer, webDataBinderFactory, request, objectMapper);
        this.validateIfApplicable(obj, methodParameter);
        return obj;
    }

    public String getPrefixName(MethodParameter methodParameter, Class parameterType) {
        JsonModel jsonModel = (JsonModel)methodParameter.getParameterAnnotation(JsonModel.class);
        String prefixName = jsonModel == null ? ((RequestModel)methodParameter.getParameterAnnotation(RequestModel.class)).value() : jsonModel.value();
        if (StringUtils.isEmpty(prefixName)) {
            prefixName = org.springframework.data.domain.Pageable.class.isAssignableFrom(methodParameter.getParameterType()) ? this.pagePrefix : (org.springframework.data.domain.Sort.class.isAssignableFrom(methodParameter.getParameterType()) ? this.sortPrefix : methodParameter.getParameterName());
        }

        return prefixName;
    }

    public JsonNode getAllParameters(HttpServletRequest request, Charset charset, ObjectMapper objectMapper) throws IOException {
        JsonNode tree = (JsonNode)request.getAttribute("REQUEST_ATTRIBUTE_JSONMODEL_NAMESPACE");
        if (null == tree) {
            String body = FileCopyUtils.copyToString(new InputStreamReader(request.getInputStream(), charset));
            if (!StringUtils.hasText(body)) {
                request.setAttribute("REQUEST_ATTRIBUTE_JSONMODEL_NAMESPACE", objectMapper.readTree("{}"));
                return null;
            }

            tree = objectMapper.readTree(body);
            request.setAttribute("REQUEST_ATTRIBUTE_JSONMODEL_NAMESPACE", tree);
        }

        return tree;
    }

    public String getParameterValue(JsonNode tree, String prefixName, Class parameterType) {
        JsonNode targetNode = tree.get(prefixName);
        String parameterValue;
        if (null == targetNode) {
            parameterValue = !String.class.isAssignableFrom(parameterType) && !org.springframework.data.domain.Sort.class.isAssignableFrom(parameterType) ? tree.toString() : null;
        } else {
            parameterValue = targetNode.toString();
        }

        return parameterValue;
    }

    public Object bindData(String parameterValue, Class parameterType, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, WebDataBinderFactory webDataBinderFactory, HttpServletRequest request, ObjectMapper objectMapper) throws Exception {
        Object obj = null;
        if (MapWapper.class.isAssignableFrom(parameterType)) {
            parameterValue = String.format("{\"innerMap\":%s}", parameterValue);
        } else if (Collection.class.isAssignableFrom(parameterType)) {
            Type type = ((ParameterizedType)methodParameter.getGenericParameterType()).getActualTypeArguments()[0];
            Class<?> actualType = (Class)((Class)(type instanceof Class ? type : ClassUtils.getGenericType(methodParameter.getContainingClass(), type)));
            JavaType javaType = JsonUtils.getCollectionType(objectMapper, parameterType, new Class[]{actualType});
            obj = objectMapper.readValue(parameterValue, javaType);
        } else {
            if (org.springframework.data.domain.Pageable.class.isAssignableFrom(parameterType)) {
                JavaType javaType = JsonUtils.getCollectionType(objectMapper, HashMap.class, new Class[]{String.class, Object.class});
                MockHttpServletRequest mockRequest = new MockHttpServletRequest();
                Map<String, Object> map = (Map)objectMapper.readValue(parameterValue, javaType);
                if (null != map) {
                    Object sort = map.get(this.sortPrefix);
                    if (null != sort) {
                        if (sort instanceof List) {
                            List<String> sortList = (ArrayList)sort;
                            mockRequest.addParameter(this.sortPrefix, (String[])sortList.toArray(new String[sortList.size()]));
                        } else {
                            mockRequest.addParameter(this.sortPrefix, sort.toString());
                        }

                        map.remove(this.sortPrefix);
                    } else {
                        JsonNode tree = (JsonNode)request.getAttribute("REQUEST_ATTRIBUTE_JSONMODEL_NAMESPACE");
                        if (null != tree) {
                            String sortStr = tree.get(this.sortPrefix) == null ? null : tree.get(this.sortPrefix).toString();
                            if (StringUtils.hasText(sortStr)) {
                                mockRequest.addParameter(this.sortPrefix, this.loadSort(sortStr, objectMapper));
                            }
                        }
                    }

                    map.forEach((k, v) -> {
                        mockRequest.addParameter(String.format(this.pageTemplate, k), v.toString());
                    });
                }

                return this.pageResolver.resolveArgument(methodParameter, modelAndViewContainer, new ServletWebRequest(mockRequest), webDataBinderFactory);
            }

            if (org.springframework.data.domain.Sort.class.isAssignableFrom(parameterType)) {
                MockHttpServletRequest mockRequest = new MockHttpServletRequest();
                if (StringUtils.hasText(parameterValue)) {
                    mockRequest.addParameter(this.sortPrefix, this.loadSort(parameterValue, objectMapper));
                }

                return this.sortResolver.resolveArgument(methodParameter, modelAndViewContainer, new ServletWebRequest(mockRequest), webDataBinderFactory);
            }

            if (String.class.isAssignableFrom(parameterType) && null == parameterValue) {
                return null;
            }
        }

        if (null == obj) {
            obj = objectMapper.readValue(parameterValue, methodParameter.getParameterType());
        }

        return obj;
    }

    public String[] loadSort(String sortStr, ObjectMapper objectMapper) throws IOException {
        if (sortStr.startsWith("[") && sortStr.endsWith("]")) {
            List<String> list = (List)objectMapper.readValue(sortStr, List.class);
            return (String[])list.toArray(new String[list.size()]);
        } else {
            return new String[]{(String)objectMapper.readValue(sortStr, String.class)};
        }
    }

    public Charset getContentCharset(MediaType mediaType) {
        return mediaType != null && mediaType.getCharset() != null ? mediaType.getCharset() : this.defaultCharset;
    }

    protected <T> void validateIfApplicable(T object, MethodParameter parameter) throws Exception {
        Annotation[] annotations = parameter.getParameterAnnotations();
        Annotation[] var4 = annotations;
        int var5 = annotations.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Annotation annot = var4[var6];
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                Class[] hints = (Class[])((Class[]) AnnotationUtils.getValue(annot));
                Set constraintViolations;
                if (hints != null && hints.length != 0) {
                    constraintViolations = this.validator.validate(object, hints);
                } else {
                    constraintViolations = this.validator.validate(object, new Class[0]);
                }

                if (null != constraintViolations && constraintViolations.size() != 0) {
                    String ObjectName = parameter.getParameterName();
                    BindException bindException = new BindException(object, ObjectName);
                    Iterator iter = constraintViolations.iterator();

                    while(iter.hasNext()) {
                        ConstraintViolation<T> error = (ConstraintViolation)iter.next();
                        bindException.addError(new FieldError(ObjectName, error.getPropertyPath().toString(), error.getMessage()));
                    }

                    throw bindException;
                }
            }
        }

    }
}
