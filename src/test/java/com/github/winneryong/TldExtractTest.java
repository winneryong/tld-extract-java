package com.github.winneryong;

import org.junit.Assert;
import org.junit.Test;

public class TldExtractTest {

    @Test
    public void extractFromUrl() {
        String url = "http://user:123@www.baidu.com:1001/12312/123?name=123#fs";
        TldExtract tldExtract = new TldExtract();
        DomainName domainName = tldExtract.extractFromUrl(url);
        Assert.assertNotNull(domainName);
        Assert.assertEquals("baidu", domainName.getDomain());
        Assert.assertEquals("baidu.com", domainName.getSecondaryDomainName());
        Assert.assertEquals("com", domainName.getSuffix());

        url = "http://user:123@192.168.100.1:1001/12312/123?name=123#fs";
        domainName = tldExtract.extractFromUrl(url);
        Assert.assertEquals("192.168.100.1", domainName.getDomain());
        Assert.assertEquals("192.168.100.1", domainName.getSecondaryDomainName());
        Assert.assertEquals("", domainName.getSuffix());
    }
}