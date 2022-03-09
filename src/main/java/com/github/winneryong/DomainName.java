package com.github.winneryong;

import cn.hutool.core.util.StrUtil;

/**
 * @author zhangyong
 */
public class DomainName {

    private final String subDomain;
    private final String domain;
    private final String suffix;

    public DomainName(String subDomain, String domain, String suffix) {
        this.subDomain = subDomain;
        this.domain = domain;
        this.suffix = suffix;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public String getDomain() {
        return domain;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getSecondaryDomainName() {
        return StrUtil.isNotBlank(getSuffix()) ? (getDomain() + "." + getSuffix()) : getDomain();
    }

    @Override
    public String toString() {
        return "DomainName{" +
                "subDomain='" + subDomain + '\'' +
                ", domain='" + domain + '\'' +
                ", suffix='" + suffix + '\'' +
                ", secondaryDomainName='" + getSecondaryDomainName() + '\'' +
                '}';
    }
}
