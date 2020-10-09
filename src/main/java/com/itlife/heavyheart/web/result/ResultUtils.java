package com.itlife.heavyheart.web.result;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author kex
 * @Create 2020/8/14 9:56
 * @Description
 */
public final class ResultUtils {
    private ResultUtils() {
    }

    public static ResultBean success(Object data) {
        return new ResultBean(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), data);
    }

    public static ResultBean success(String code, Object data) {
        return new ResultBean(code, ResultEnum.SUCCESS.getMsg(), data);
    }

    public static ResultPageBean success(List data, Pageable pageable, long totalSize) {
        return success((Page)(new PageImpl(data, pageable, totalSize)));
    }

    public static ResultPageBean successPage(List data) {
        return success((Page)(new PageImpl(data)));
    }

    public static <T> ResultPageBean success(Page<T> page) {
        return (new ResultPageBean(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg())).bindPage(page);
    }

    public static ResultFileBean successFile(String fileName) {
        return (new ResultFileBean()).fileName(fileName);
    }

    public static ResultFileBean successFile(String fileName, String contentType) {
        return (new ResultFileBean()).contentType(contentType).fileName(fileName);
    }

    public static ResultFileBean successFile(String fileName, Object fileOrInputstream) {
        return (new ResultFileBean()).fileName(fileName).addFile(fileName, fileOrInputstream);
    }

    public static ResultFileBean successFile(String fileName, String contentType, Object fileOrInputstream) {
        return (new ResultFileBean()).contentType(contentType).fileName(fileName).addFile(fileName, fileOrInputstream);
    }

    public static ResultFileBean successFile(String fileName, Map<String, Object> map) {
        return (new ResultFileBean()).fileName(fileName).initFiles(map);
    }

    public static ResultBean success() {
        return success((Object)null);
    }

    public static ResultBean error(String code, String msg) {
        ResultBean result = new ResultBean();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    public static ResultRedirectBean redirect(String code, String redirectUrl) {
        ResultRedirectBean redirectBean = new ResultRedirectBean();
        redirectBean.setCode(code);
        redirectBean.setRedirectUrl(redirectUrl);
        return redirectBean;
    }

    public static ResultRedirectBean redirect(String redirectUrl) {
        ResultRedirectBean redirectBean = new ResultRedirectBean();
        redirectBean.setCode(ResultEnum.REDIRECT.getCode());
        redirectBean.setRedirectUrl(redirectUrl);
        return redirectBean;
    }

    public static ResultBean error(String code, String msg, Object data) {
        ResultBean result = new ResultBean();
        result.setCode(code);
        result.setMessage(msg);
        result.setData(data);
        return result;
    }

    public static ResultBean error(ResultEnum codeEnum, String msg) {
        ResultBean result = new ResultBean();
        result.setCode(codeEnum.getCode());
        result.setMessage(msg);
        return result;
    }

    public static ResultBean<List<TreeBean>> successTree(List<TreeBean> childens) {
        return successTree(ResultEnum.SUCCESS.getCode(), childens);
    }

    public static ResultBean<List<TreeBean>> successTree(String code, List<TreeBean> children) {
        List<TreeBean> list = formatTree(children);
        ResultBean resultBean = new ResultBean(code, ResultEnum.SUCCESS.getMsg());
        resultBean.setData(list);
        return resultBean;
    }

    private static <T extends TreeBean> List<T> formatTree(List<T> list) {
        List<T> nodeList = new ArrayList();
        Iterator var2 = list.iterator();

        while(var2.hasNext()) {
            T node1 = (T) var2.next();
            boolean mark = false;
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                T node2 = (T) var5.next();
                if (node1.getParentId() != null && node1.getParentId().equals(node2.getId())) {
                    mark = true;
                    if (node2.getChildList() == null) {
                        node2.setChildList(new ArrayList());
                    }

                    node2.getChildList().add(node1);
                    break;
                }
            }

            if (!mark) {
                node1.setLeaf(0);
                if (node1.getChildList() == null) {
                    node1.setChildList(new ArrayList());
                }

                nodeList.add(node1);
            }
        }

        return nodeList;
    }
}
