package com.github.winneryong;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.net.IDN;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 从URL中提取顶级域和二级域名
 *
 * @author zhangyong
 */
public class TldExtract {

    private static final String SCHEME_CHARS = "^([abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+-.]+:)?//";
    private static final Pattern IP_PATTERN = Pattern.compile("\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z");

    /**
     * 从Url中提取域名信息
     *
     * @param url
     * @return
     */
    public DomainName extractFromUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return null;
        }

        url = url.toLowerCase();
        //提出scheme
        String processUrl = url.replaceFirst(SCHEME_CHARS, "");
        processUrl = processUrl.split("/")[0];
        processUrl = processUrl.split("\\?")[0];
        processUrl = processUrl.split("#")[0];

        String[] parts = processUrl.split("@");
        processUrl = parts[parts.length - 1];

        processUrl = processUrl.split("#")[0];
        processUrl = processUrl.split(":")[0];
        processUrl = processUrl.trim();
        if (processUrl.endsWith(".")) {
            processUrl = processUrl.substring(0, processUrl.length() - 1);
        }
        String[] labels = processUrl.split("\\.");
        String[] translationLabels = translation(labels);
        int suffixIndex = indexOfTld(translationLabels);

        String suffix = String.join(".", ArrayUtil.sub(labels, suffixIndex, labels.length));
        //如果是IPv4
        if (isIpAddress(processUrl)) {
            return new DomainName("", processUrl, "");
        }

        String subDomain = "";
        String domain = "";
        if (suffixIndex > 0) {
            subDomain = String.join(".", ArrayUtil.sub(labels, 0, suffixIndex - 1));
            domain = labels[suffixIndex - 1];
        }

        return new DomainName(subDomain, domain, suffix);
    }


    private int indexOfTld(String[] labels) {

        List<String> suffixList = SuffixList.getSuffixList();

        for (int i = 0; i < labels.length; i++) {
            String maybeTld = String.join(".", ArrayUtil.sub(labels, i, labels.length));
            String exceptionTld = "!" + maybeTld;

            if (suffixList.contains(exceptionTld)) {
                return i + 1;
            }

            if (suffixList.contains(maybeTld)) {
                return i;
            }

            String wildcardTld = "*." + String.join(".", ArrayUtil.sub(labels, i + 1, labels.length));
            if (suffixList.contains(wildcardTld)) {
                return i;
            }
        }

        return labels.length;
    }


    /**
     * 将punycode的域名变成中文放到数组中
     *
     * @param arr
     * @return
     */
    private String[] translation(String[] arr) {
        String[] translations = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            translations[i] = decodePunyCode(arr[i]);
        }
        return translations;
    }

    /**
     * 如果域名是punycode码将域名信息转换成中文
     *
     * @param label
     * @return
     */
    private String decodePunyCode(String label) {
        if (label.startsWith("xn--")) {
            return IDN.toUnicode(label);
        }
        return label;
    }

    private boolean isIpAddress(String str) {
        return IP_PATTERN.matcher(str).matches();
    }
}
