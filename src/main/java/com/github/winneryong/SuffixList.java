package com.github.winneryong;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * TLD 列表
 * @author zhangyong
 */
public class SuffixList {

    private static final String SUFFIX_LIST_FILE_NAME = "public_suffix_list.dat.txt";

    private static final List<String> PUBLIC_SUFFIX_LIST = new ArrayList<>();

    private static final List<String> PRIVATE_SUFFIX_LIST = new ArrayList<>();

    static {
        readSuffixList();
    }


    private static void readSuffixList() {
        InputStream inputStream = SuffixList.class.getClassLoader().getResourceAsStream(SUFFIX_LIST_FILE_NAME);
        if (Objects.isNull(inputStream)) {
            throw new RuntimeException("not found " + SUFFIX_LIST_FILE_NAME +" in classpath");
        }

        List<String> lineList = new ArrayList<>();
        IoUtil.readLines(new InputStreamReader(inputStream), lineList);

        for (String line : lineList) {
            if (StrUtil.isBlank(line)) {
                continue;
            }

            if ("// ===BEGIN PRIVATE DOMAINS===".equals(line)) {

                if (isSuffix(line)) {
                    PRIVATE_SUFFIX_LIST.add(StrUtil.trim(line));
                }

            } else {
                if (isSuffix(line)) {
                    PUBLIC_SUFFIX_LIST.add(StrUtil.trim(line));
                }
            }
        }
    }

    private static boolean isSuffix(String line) {
        return !line.startsWith("//");
    }

    public static List<String> getPublicSuffixList(){
        return Collections.unmodifiableList(PUBLIC_SUFFIX_LIST);
    }

    public static List<String> getPrivateSuffixList(){
        return Collections.unmodifiableList(PRIVATE_SUFFIX_LIST);
    }

    public static List<String> getSuffixList(){
        List<String> suffixList = new ArrayList<>();
        suffixList.addAll(PUBLIC_SUFFIX_LIST);
        suffixList.addAll(PRIVATE_SUFFIX_LIST);

        return Collections.unmodifiableList(suffixList);
    }
}
