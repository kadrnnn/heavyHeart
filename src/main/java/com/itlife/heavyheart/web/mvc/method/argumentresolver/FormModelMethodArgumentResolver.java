package com.itlife.heavyheart.web.mvc.method.argumentresolver;

import com.itlife.heavyheart.springUtils.SpringContextUtils;
import com.itlife.heavyheart.web.mvc.annotation.FormModel;
import com.itlife.heavyheart.web.mvc.annotation.RequestModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author kex
 * @Create 2020/8/14 10:26
 * @Description
 */
public class FormModelMethodArgumentResolver extends BaseMethodArgumentResolver {
    private final Pattern INDEX_PATTERN = Pattern.compile("\\[(\\d+)\\]\\.?");
    private int autoGrowCollectionLimit = 2147483647;
    private String pagePrefix;
    private String sortPrefix;
    private PageableHandlerMethodArgumentResolver pageResolver = (PageableHandlerMethodArgumentResolver) SpringContextUtils.getBean(PageableHandlerMethodArgumentResolver.class);
    private SortHandlerMethodArgumentResolver sortResolver = (SortHandlerMethodArgumentResolver)SpringContextUtils.getBean(SortHandlerMethodArgumentResolver.class);

    public FormModelMethodArgumentResolver() {
        SpringDataWebProperties springDataWebProperties = (SpringDataWebProperties)SpringContextUtils.getBean(SpringDataWebProperties.class);
        SpringDataWebProperties.Pageable pageable = springDataWebProperties.getPageable();
        SpringDataWebProperties.Sort sort = springDataWebProperties.getSort();
        this.pagePrefix = pageable.getPrefix() == null ? "" : pageable.getPrefix().substring(0, pageable.getPrefix().length() - 1);
        this.sortPrefix = sort.getSortParameter();
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FormModel.class);
    }

    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String name = this.getPrefixNameFromAnnotation(methodParameter);
        Object target = modelAndViewContainer.containsAttribute(name) ? modelAndViewContainer.getModel().get(name) : this.createAttribute(name, methodParameter, webDataBinderFactory, nativeWebRequest);
        WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, target, name);
        target = binder.getTarget();
        Object obj = null;
        if (target != null) {
            obj = this.bindRequestParameters(modelAndViewContainer, webDataBinderFactory, binder, nativeWebRequest, methodParameter);
            this.validateIfApplicable(binder, methodParameter);
            if (binder.getBindingResult().hasErrors() && this.isBindExceptionRequired(binder, methodParameter)) {
                throw new BindException(binder.getBindingResult());
            }
        }

        if (!this.pagePrefix.equals(name) && !this.sortPrefix.equals(name)) {
            target = binder.convertIfNecessary(binder.getTarget(), methodParameter.getParameterType());
        } else {
            target = obj;
        }

        modelAndViewContainer.addAttribute(name, target);
        return target;
    }

    private String getPrefixNameFromAnnotation(MethodParameter methodParameter) {
        FormModel formModel = (FormModel)methodParameter.getParameterAnnotation(FormModel.class);
        String name = formModel == null ? ((RequestModel)methodParameter.getParameterAnnotation(RequestModel.class)).value() : formModel.value();
        if (StringUtils.isEmpty(name)) {
            if (org.springframework.data.domain.Pageable.class.isAssignableFrom(methodParameter.getParameterType())) {
                name = this.pagePrefix;
            } else if (org.springframework.data.domain.Sort.class.isAssignableFrom(methodParameter.getParameterType())) {
                name = this.sortPrefix;
            } else {
                name = methodParameter.getParameterName();
            }
        }

        return name;
    }

    protected Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory webDataBinderFactory, NativeWebRequest nativeWebRequest) throws Exception {
        String value = this.getRequestValueForAttribute(attributeName, nativeWebRequest);
        if (value != null) {
            Object attribute = this.createAttributeFromRequestValue(value, attributeName, parameter, webDataBinderFactory, nativeWebRequest);
            if (attribute != null) {
                return attribute;
            }
        }

        Class<?> parameterType = parameter.getParameterType();
        if (!parameterType.isArray() && !List.class.isAssignableFrom(parameterType)) {
            if (Set.class.isAssignableFrom(parameterType)) {
                return HashSet.class.newInstance();
            } else if (MapWapper.class.isAssignableFrom(parameterType)) {
                return MapWapper.class.newInstance();
            } else if (org.springframework.data.domain.Pageable.class.isAssignableFrom(parameterType)) {
                return PageRequest.of(0, 1);
            } else if (org.springframework.data.domain.Sort.class.isAssignableFrom(parameterType)) {
                return org.springframework.data.domain.Sort.unsorted();
            } else {
                return ClassUtils.hasConstructor(parameter.getParameterType(), new Class[0]) ? BeanUtils.instantiateClass(parameter.getParameterType()) : null;
            }
        } else {
            return ArrayList.class.newInstance();
        }
    }

    protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
        Map<String, String> variables = this.getUriTemplateVariables(request);
        if (StringUtils.hasText((String)variables.get(attributeName))) {
            return (String)variables.get(attributeName);
        } else {
            return StringUtils.hasText(request.getParameter(attributeName)) ? request.getParameter(attributeName) : null;
        }
    }

    protected Object createAttributeFromRequestValue(String sourceValue, String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {
        DataBinder binder = binderFactory.createBinder(request, (Object)null, attributeName);
        ConversionService conversionService = binder.getConversionService();
        if (conversionService != null) {
            TypeDescriptor source = TypeDescriptor.valueOf(String.class);
            TypeDescriptor target = new TypeDescriptor(parameter);
            if (conversionService.canConvert(source, target)) {
                return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
            }
        }

        return null;
    }

    protected Object bindRequestParameters(ModelAndViewContainer mavContainer, WebDataBinderFactory binderFactory, WebDataBinder binder, NativeWebRequest request, MethodParameter parameter) throws Exception {
        Map<String, Boolean> hasProcessedPrefixMap = new HashMap();
        Class<?> targetType = binder.getTarget().getClass();
        ServletRequest servletRequest = this.prepareServletRequest(binder.getTarget(), request, parameter);
        WebDataBinder simpleBinder = binderFactory.createBinder(request, (Object)null, (String)null);
        Type type;
        Class componentType;
        if (Collection.class.isAssignableFrom(targetType)) {
            type = parameter.getGenericParameterType();
            componentType = Object.class;
            Collection target = (Collection)binder.getTarget();
            target.clear();
            List targetList = new ArrayList(target);
            if (type instanceof ParameterizedType) {
                Type actualType = ((ParameterizedType)type).getActualTypeArguments()[0];
                componentType = (Class)((Class)(actualType instanceof Class ? actualType : com.itlife.heavyheart.utils.ClassUtils.getGenericType(parameter.getContainingClass(), actualType)));
            }

            if (parameter.getParameterType().isArray()) {
                componentType = parameter.getParameterType().getComponentType();
            }

            Iterator var30 = servletRequest.getParameterMap().keySet().iterator();

            while(true) {
                Object key;
                String prefixName;
                do {
                    if (!var30.hasNext()) {
                        return null;
                    }

                    key = var30.next();
                    prefixName = this.getPrefixName((String)key);
                } while(hasProcessedPrefixMap.containsKey(prefixName));

                hasProcessedPrefixMap.put(prefixName, Boolean.TRUE);
                Matcher matcher;
                int index;
                if (this.isSimpleComponent(prefixName)) {
                    Map<String, Object> paramValues = WebUtils.getParametersStartingWith(servletRequest, prefixName);
                    matcher = this.INDEX_PATTERN.matcher(prefixName);
                    if (matcher.matches()) {
                        index = Integer.valueOf(matcher.group(1));
                        if (targetList.size() <= index) {
                            this.growCollectionIfNecessary(targetList, index);
                        }

                        targetList.set(index, simpleBinder.convertIfNecessary(paramValues.values(), componentType));
                    } else {
                        Iterator var37 = paramValues.values().iterator();

                        label152:
                        while(true) {
                            while(true) {
                                if (!var37.hasNext()) {
                                    break label152;
                                }

                                Object value = var37.next();
                                if (value.getClass().isArray()) {
                                    String[] values = (String[])((String[])value);
                                    String[] var44 = values;
                                    int var46 = values.length;

                                    for(int var47 = 0; var47 < var46; ++var47) {
                                        String valueStr = var44[var47];
                                        targetList.add(simpleBinder.convertIfNecessary(valueStr, componentType));
                                    }
                                } else {
                                    targetList.add(simpleBinder.convertIfNecessary(value, componentType));
                                }
                            }
                        }
                    }
                } else {
                    Object component = null;
                    matcher = this.INDEX_PATTERN.matcher(prefixName);
                    if (!matcher.matches()) {
                        throw new IllegalArgumentException("bind collection error, need integer index, key:" + key);
                    }

                    index = Integer.valueOf(matcher.group(1));
                    if (targetList.size() <= index) {
                        this.growCollectionIfNecessary(targetList, index);
                    }

                    Iterator iterator = targetList.iterator();

                    for(int i = 0; i < index; ++i) {
                        iterator.next();
                    }

                    component = iterator.next();
                    if (component == null) {
                        component = componentType.newInstance();
                    }

                    WebDataBinder componentBinder = binderFactory.createBinder(request, component, (String)null);
                    component = componentBinder.getTarget();
                    if (component != null) {
                        ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(servletRequest, prefixName, "");
                        MutablePropertyValues mpvs = pvs instanceof MutablePropertyValues ? pvs : new MutablePropertyValues(pvs);
                        HttpServletRequest tempRequest = (HttpServletRequest)request.getNativeRequest();
                        MultipartRequest multipartRequest = (MultipartRequest)WebUtils.getNativeRequest(tempRequest, MultipartRequest.class);
                        if (multipartRequest != null) {
                            multipartRequest.getMultiFileMap().forEach((ikey, valuesx) -> {
                                int pos;
                                if ((pos = ikey.indexOf("[" + index + "]")) > -1) {
                                    String name = ikey.substring(pos + ("[" + index + "]").length() + 1);
                                    if (valuesx.size() == 1) {
                                        MultipartFile value = (MultipartFile)valuesx.get(0);
                                        if (componentBinder.isBindEmptyMultipartFiles() || !value.isEmpty()) {
                                            mpvs.add(name, value);
                                        }
                                    } else {
                                        mpvs.add(name, valuesx);
                                    }
                                }

                            });
                        }

                        componentBinder.bind((PropertyValues)mpvs);
                        this.validateIfApplicable(componentBinder, parameter);
                        if (componentBinder.getBindingResult().hasErrors() && this.isBindExceptionRequired(componentBinder, parameter)) {
                            throw new BindException(componentBinder.getBindingResult());
                        }

                        targetList.set(index, component);
                    }
                }

                target.clear();
                target.addAll(targetList);
            }
        } else if (MapWapper.class.isAssignableFrom(targetType)) {
            type = parameter.getGenericParameterType();
            componentType = Object.class;
            Class<?> valueType = Object.class;
            if (type instanceof ParameterizedType) {
                Type actualType = ((ParameterizedType)type).getActualTypeArguments()[0];
                componentType = (Class)((Class)(actualType instanceof Class ? actualType : com.itlife.heavyheart.utils.ClassUtils.getGenericType(parameter.getContainingClass(), actualType)));
                actualType = ((ParameterizedType)type).getActualTypeArguments()[1];
                valueType = (Class)((Class)(actualType instanceof Class ? actualType : com.itlife.heavyheart.utils.ClassUtils.getGenericType(parameter.getContainingClass(), actualType)));
            }

            MapWapper mapWapper = (MapWapper)binder.getTarget();
            Map target = mapWapper.getInnerMap();
            if (target == null) {
                target = new HashMap();
                mapWapper.setInnerMap((Map)target);
            }

            Iterator var32 = servletRequest.getParameterMap().keySet().iterator();

            while(var32.hasNext()) {
                Object key = var32.next();
                String prefixName = this.getPrefixName((String)key);
                if ("".equals(prefixName)) {
                    return null;
                }

                if (!hasProcessedPrefixMap.containsKey(prefixName)) {
                    hasProcessedPrefixMap.put(prefixName, Boolean.TRUE);
                    Object keyValue = simpleBinder.convertIfNecessary(this.getMapKey(prefixName), componentType);
                    if (this.isSimpleComponent(prefixName)) {
                        Map<String, Object> paramValues = WebUtils.getParametersStartingWith(servletRequest, prefixName);
                        ((Map)target).put(keyValue, simpleBinder.convertIfNecessary(paramValues.values().stream().findFirst().orElse((Object)null), valueType));
                    } else {
                        Object component = ((Map)target).get(keyValue);
                        if (component == null) {
                            component = valueType.newInstance();
                        }

                        WebDataBinder componentBinder = binderFactory.createBinder(request, component, (String)null);
                        component = componentBinder.getTarget();
                        if (component != null) {
                            ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(servletRequest, prefixName, "");
                            componentBinder.bind(pvs);
                            this.validateComponent(componentBinder, parameter);
                            ((Map)target).put(keyValue, component);
                        }
                    }
                }
            }
        } else {
            if (org.springframework.data.domain.Pageable.class.isAssignableFrom(targetType)) {
                return this.pageResolver.resolveArgument(parameter, mavContainer, new ServletWebRequest((HttpServletRequest)servletRequest), binderFactory);
            }

            if (org.springframework.data.domain.Sort.class.isAssignableFrom(targetType)) {
                return this.sortResolver.resolveArgument(parameter, mavContainer, new ServletWebRequest((HttpServletRequest)servletRequest), binderFactory);
            }

            ServletRequestDataBinder servletBinder = (ServletRequestDataBinder)binder;
            servletBinder.bind(servletRequest);
        }

        return null;
    }

    private void growCollectionIfNecessary(Collection collection, int index) {
        if (index >= collection.size() && index < this.autoGrowCollectionLimit) {
            for(int i = collection.size(); i <= index; ++i) {
                collection.add((Object)null);
            }
        }

    }

    private String getPrefixName(String name) {
        int begin = 0;
        int end = name.indexOf("]") + 1;
        if (0 == end && -1 == name.indexOf(".")) {
            return name;
        } else {
            if (name.indexOf("].") >= 0) {
                ++end;
            }

            return name.substring(begin, end);
        }
    }

    protected void validateComponent(WebDataBinder binder, MethodParameter parameter) throws BindException {
        boolean validateParameter = this.validateParameter(parameter);
        Annotation[] annotations = binder.getTarget().getClass().getAnnotations();
        Annotation[] var5 = annotations;
        int var6 = annotations.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Annotation annot = var5[var7];
            if (annot.annotationType().getSimpleName().startsWith("Valid") && validateParameter) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[])((Object[])hints) : new Object[]{hints});
            }
        }

        if (binder.getBindingResult().hasErrors() && this.isBindExceptionRequired(binder, parameter)) {
            throw new BindException(binder.getBindingResult());
        }
    }

    private boolean validateParameter(MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        Annotation[] var3 = annotations;
        int var4 = annotations.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Annotation annot = var3[var5];
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                return true;
            }
        }

        return false;
    }

    private Object getMapKey(String prefixName) {
        String key = prefixName;
        if (prefixName.startsWith("['")) {
            key = prefixName.replaceAll("\\['", "").replaceAll("'\\]", "");
        }

        if (key.startsWith("[\"")) {
            key = key.replaceAll("\\[\"", "").replaceAll("\"\\]", "");
        }

        if (key.endsWith(".")) {
            key = key.substring(0, key.length() - 1);
        }

        return key;
    }

    private boolean isSimpleComponent(String prefixName) {
        return !prefixName.endsWith(".");
    }

    private ServletRequest prepareServletRequest(Object target, NativeWebRequest request, MethodParameter parameter) {
        String modelPrefixName = this.getPrefixNameFromAnnotation(parameter);
        HttpServletRequest nativeRequest = (HttpServletRequest)request.getNativeRequest();
        MultipartRequest multipartRequest = (MultipartRequest)WebUtils.getNativeRequest(nativeRequest, MultipartRequest.class);
        MockHttpServletRequest mockRequest = null;
        String fileName;
        if (multipartRequest != null) {
            MockMultipartHttpServletRequest mockMultipartRequest = new MockMultipartHttpServletRequest();
            Iterator fileNameIterator = multipartRequest.getFileNames();

            label76:
            while(true) {
                List files;
                do {
                    do {
                        if (!fileNameIterator.hasNext()) {
                            mockRequest = mockMultipartRequest;
                            break label76;
                        }

                        fileName = (String)fileNameIterator.next();
                        files = multipartRequest.getFiles(fileName);
                    } while(null == files);
                } while(files.size() == 0);

                Iterator var12 = files.iterator();

                while(var12.hasNext()) {
                    MultipartFile file = (MultipartFile)var12.next();
                    if (fileName.indexOf(modelPrefixName) != -1) {
                        mockMultipartRequest.addFile(new MultipartFileWrapper(this.getNewParameterName(fileName, modelPrefixName), file));
                    } else {
                        mockMultipartRequest.addFile(new MultipartFileWrapper(fileName, file));
                    }
                }
            }
        } else {
            mockRequest = new MockHttpServletRequest();
        }

        Iterator var14 = this.getUriTemplateVariables(request).entrySet().iterator();

        String parameterName;
        while(var14.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var14.next();
            fileName = (String)entry.getKey();
            parameterName = (String)entry.getValue();
            if (this.isFormModelAttribute(fileName, modelPrefixName)) {
                ((MockHttpServletRequest)mockRequest).setParameter(this.getNewParameterName(fileName, modelPrefixName), parameterName);
            }
        }

        var14 = nativeRequest.getParameterMap().entrySet().iterator();

        while(true) {
            while(true) {
                String[] value;
                do {
                    if (!var14.hasNext()) {
                        if (null != ((MockHttpServletRequest)mockRequest).getParameterMap() && ((MockHttpServletRequest)mockRequest).getParameterMap().size() != 0) {
                            return (ServletRequest)mockRequest;
                        }

                        return (ServletRequest)request.getNativeRequest();
                    }

                    Object parameterEntry = var14.next();
                    Map.Entry<String, String[]> entry = (Map.Entry)parameterEntry;
                    parameterName = (String)entry.getKey();
                    value = (String[])entry.getValue();
                } while(!this.isFormModelAttribute(parameterName, modelPrefixName));

                if (!this.pagePrefix.equals(modelPrefixName) && !this.sortPrefix.equals(modelPrefixName)) {
                    ((MockHttpServletRequest)mockRequest).setParameter(this.getNewParameterName(parameterName, modelPrefixName), value);
                } else {
                    ((MockHttpServletRequest)mockRequest).setParameter(parameterName, value);
                }
            }
        }
    }

    private String getNewParameterName(String parameterName, String modelPrefixName) {
        int modelPrefixNameLength = modelPrefixName.length();
        if (modelPrefixName.equals(parameterName)) {
            return UUID.randomUUID().toString();
        } else if (parameterName.charAt(modelPrefixNameLength) == '.') {
            return parameterName.substring(modelPrefixNameLength + 1);
        } else if (parameterName.charAt(modelPrefixNameLength) == '[') {
            return parameterName.substring(modelPrefixNameLength);
        } else {
            throw new IllegalArgumentException("illegal request parameter, can not binding to @FormBean(" + modelPrefixName + ")");
        }
    }

    private boolean isFormModelAttribute(String parameterName, String modelPrefixName) {
        int modelPrefixNameLength = modelPrefixName.length();
        if (modelPrefixName.equals(parameterName)) {
            return true;
        } else if (!parameterName.startsWith(modelPrefixName)) {
            return false;
        } else {
            char ch = parameterName.charAt(modelPrefixNameLength);
            return ch == '.' || ch == '[';
        }
    }

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        Annotation[] var4 = annotations;
        int var5 = annotations.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Annotation annot = var4[var6];
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[])((Object[])hints) : new Object[]{hints});
            }
        }

    }

    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
        boolean hasBindingResult = paramTypes.length > i + 1 && Errors.class.isAssignableFrom(paramTypes[i + 1]);
        return !hasBindingResult;
    }

    private static class MultipartFileWrapper implements MultipartFile {
        private String name;
        private MultipartFile delegate;

        private MultipartFileWrapper(String name, MultipartFile delegate) {
            this.name = name;
            this.delegate = delegate;
        }

        public String getName() {
            return this.name;
        }

        public String getOriginalFilename() {
            return this.delegate.getOriginalFilename();
        }

        public String getContentType() {
            return this.delegate.getContentType();
        }

        public boolean isEmpty() {
            return this.delegate.isEmpty();
        }

        public long getSize() {
            return this.delegate.getSize();
        }

        public byte[] getBytes() throws IOException {
            return this.delegate.getBytes();
        }

        public InputStream getInputStream() throws IOException {
            return this.delegate.getInputStream();
        }

        public void transferTo(File dest) throws IOException, IllegalStateException {
            this.delegate.transferTo(dest);
        }
    }
}
