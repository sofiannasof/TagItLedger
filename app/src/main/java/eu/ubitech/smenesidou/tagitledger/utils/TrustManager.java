package eu.ubitech.smenesidou.tagitledger.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] certificates, String arg) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certificates, String arg) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] x509Certificates = new X509Certificate[0];
        return x509Certificates;
    }

}
