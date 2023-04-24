package org.opsli.plugins.pagehelper;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * Configuration properties for PageHelper.
 *
 * @author liuzh
 */
@ConfigurationProperties(prefix = PageHelperProperties.PAGEHELPER_PREFIX)
public class PageHelperProperties extends Properties {

    public static final String PAGEHELPER_PREFIX = "pagehelper";

    public Boolean getOffsetAsPageNum() {
        return Boolean.valueOf(getProperty("offsetAsPageNum"));
    }

    public void setOffsetAsPageNum(Boolean offsetAsPageNum) {
        setProperty("offsetAsPageNum", offsetAsPageNum.toString());
    }

    public Boolean getRowBoundsWithCount() {
        return Boolean.valueOf(getProperty("rowBoundsWithCount"));
    }

    public void setRowBoundsWithCount(Boolean rowBoundsWithCount) {
        setProperty("rowBoundsWithCount", rowBoundsWithCount.toString());
    }

    public Boolean getPageSizeZero() {
        return Boolean.valueOf(getProperty("pageSizeZero"));
    }

    public void setPageSizeZero(Boolean pageSizeZero) {
        setProperty("pageSizeZero", pageSizeZero.toString());
    }

    public Boolean getReasonable() {
        return Boolean.valueOf(getProperty("reasonable"));
    }

    public void setReasonable(Boolean reasonable) {
        setProperty("reasonable", reasonable.toString());
    }

    public Boolean getSupportMethodsArguments() {
        return Boolean.valueOf(getProperty("supportMethodsArguments"));
    }

    public void setSupportMethodsArguments(Boolean supportMethodsArguments) {
        setProperty("supportMethodsArguments", supportMethodsArguments.toString());
    }

    public String getDialect() {
        return getProperty("dialect");
    }

    public void setDialect(String dialect) {
        setProperty("dialect", dialect);
    }

    public String getHelperDialect() {
        return getProperty("helperDialect");
    }

    public void setHelperDialect(String helperDialect) {
        setProperty("helperDialect", helperDialect);
    }

    public Boolean getAutoRuntimeDialect() {
        return Boolean.valueOf(getProperty("autoRuntimeDialect"));
    }

    public void setAutoRuntimeDialect(Boolean autoRuntimeDialect) {
        setProperty("autoRuntimeDialect", autoRuntimeDialect.toString());
    }

    public Boolean getAutoDialect() {
        return Boolean.valueOf(getProperty("autoDialect"));
    }

    public void setAutoDialect(Boolean autoDialect) {
        setProperty("autoDialect", autoDialect.toString());
    }

    public Boolean getCloseConn() {
        return Boolean.valueOf(getProperty("closeConn"));
    }

    public void setCloseConn(Boolean closeConn) {
        setProperty("closeConn", closeConn.toString());
    }

    public String getParams() {
        return getProperty("params");
    }

    public void setParams(String params) {
        setProperty("params", params);
    }

    public Boolean getDefaultCount() {
        return Boolean.valueOf(getProperty("defaultCount"));
    }

    public void setDefaultCount(Boolean defaultCount) {
        setProperty("defaultCount", defaultCount.toString());
    }

    public String getDialectAlias() {
        return getProperty("dialectAlias");
    }

    public void setDialectAlias(String dialectAlias) {
        setProperty("dialectAlias", dialectAlias);
    }

    public String getAutoDialectClass() {
        return getProperty("autoDialectClass");
    }

    public void setAutoDialectClass(String autoDialectClass) {
        setProperty("autoDialectClass", autoDialectClass);
    }

    public Boolean getBanner() {
        return Boolean.valueOf(getProperty("banner"));
    }
    public void setBanner(Boolean banner) {
        setProperty("banner", banner.toString());
    }
}
