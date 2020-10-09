package com.itlife.heavyheart.web.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author kex
 * @Create 2020/8/14 10:00
 * @Description
 */
public class TreeBean extends DynaBean<TreeBean> {
    private static final String ID = "id";
    private static final String ICON = "icon";
    private static final String OPENICON = "openIcon";
    private static final String AUTOEXPAND = "autoExpand";
    private static final String BEANTYPE = "beanType";
    private static final String TARGET = "target";
    private static final String URL = "url";
    private static final String NAME = "name";
    private static final String PARENTID = "parentId";
    private static final String PATH = "path";
    private static final String SORTNUM = "sortNum";
    private static final String LEAF = "leaf";
    private static final String CHILDLIST = "childList";
    private static final String VALIDFLAG = "validFlag";
    private static final String EXT = "ext";
    private static final String SPELL = "spell";

    public TreeBean() {
        this.setChildList(new ArrayList());
    }

    public <PK extends Serializable> PK getId() {
        return (PK) this.getProperty("id");
    }

    public <PK extends Serializable> TreeBean setId(PK id) {
        return (TreeBean) this.setProperty("id", id);
    }

    public String getIcon() {
        return (String) this.getProperty("icon");
    }

    public TreeBean setIcon(String icon) {
        return (TreeBean) this.setProperty("icon", icon);
    }

    public String getOpenIcon() {
        return (String) this.getProperty("openIcon");
    }

    public TreeBean setOpenIcon(String openIcon) {
        return (TreeBean) this.setProperty("openIcon", openIcon);
    }

    public Integer getAutoExpand() {
        return (Integer) this.getProperty("autoExpand");
    }

    public TreeBean setAutoExpand(Integer autoExpand) {
        return (TreeBean) this.setProperty("autoExpand", autoExpand);
    }

    public String getBeanType() {
        return (String) this.getProperty("beanType");
    }

    public TreeBean setBeanType(String beanType) {
        return (TreeBean) this.setProperty("beanType", beanType);
    }

    public String getTarget() {
        return (String) this.getProperty("target");
    }

    public TreeBean setTarget(String target) {
        return (TreeBean) this.setProperty("target", target);
    }

    public String getUrl() {
        return (String) this.getProperty("url");
    }

    public TreeBean setUrl(String url) {
        return (TreeBean) this.setProperty("url", url);
    }

    public String getName() {
        return (String) this.getProperty("name");
    }

    public TreeBean setName(String name) {
        return (TreeBean) this.setProperty("name", name);
    }

    public <PK extends Serializable> PK getParentId() {
        return (PK) this.getProperty("parentId");
    }

    public <PK extends Serializable> TreeBean setParentId(PK parentId) {
        return (TreeBean) this.setProperty("parentId", parentId);
    }

    public String getPath() {
        return (String) this.getProperty("path");
    }

    public TreeBean setPath(String path) {
        return (TreeBean) this.setProperty("path", path);
    }

    public Integer getSortNum() {
        return (Integer) this.getProperty("sortNum");
    }

    public TreeBean setSortNum(Integer sortNum) {
        return (TreeBean) this.setProperty("sortNum", sortNum);
    }

    public Integer getLeaf() {
        return (Integer) this.getProperty("leaf");
    }

    public TreeBean setLeaf(Integer leaf) {
        return (TreeBean) this.setProperty("leaf", leaf);
    }

    public List<TreeBean> getChildList() {
        return (List) this.getProperty("childList");
    }

    public TreeBean setChildList(List<TreeBean> childList) {
        return (TreeBean) this.setProperty("childList", childList);
    }

    public Integer getValidFlag() {
        return (Integer) this.getProperty("validFlag");
    }

    public TreeBean setValidFlag(Integer validFlag) {
        return (TreeBean) this.setProperty("validFlag", validFlag);
    }

    public String getExt() {
        return (String) this.getProperty("ext");
    }

    public TreeBean setExt(Object ext) {
        return (TreeBean) this.setProperty("ext", ext);
    }

    public String getSpell() {
        return (String) this.getProperty("spell");
    }

    public TreeBean setSpell(String ext) {
        return (TreeBean) this.setProperty("spell", ext);
    }
}
