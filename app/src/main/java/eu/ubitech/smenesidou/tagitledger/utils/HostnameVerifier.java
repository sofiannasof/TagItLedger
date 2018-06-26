package eu.ubitech.smenesidou.tagitledger.utils;

import javax.net.ssl.SSLSession;

public class HostnameVerifier implements javax.net.ssl.HostnameVerifier {

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return false;
    }
}

