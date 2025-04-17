package com.g2forge.alexandria.java.io.net;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.g2forge.alexandria.java.core.marker.ISingleton;

import lombok.AccessLevel;
import lombok.Getter;

@Deprecated
public class UnsafeX509TrustManager implements X509TrustManager, ISingleton {
	protected static final UnsafeX509TrustManager INSTANCE = new UnsafeX509TrustManager();

	@Getter(lazy = true, value = AccessLevel.PUBLIC)
	@Deprecated
	private static final SSLContext unsafeSSLContext = createUnsafeSSLContext();

	@Deprecated
	public static UnsafeX509TrustManager create() {
		return INSTANCE;
	}

	@Deprecated
	protected static SSLContext createUnsafeSSLContext() {
		try {
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, new TrustManager[] { UnsafeX509TrustManager.create() }, new java.security.SecureRandom());
			return sslContext;
		} catch (Throwable throwable) {
			throw new RuntimeException("Failed to create unsafe SSL context!  Don't use this class in production, please verify SSL certificates.", throwable);
		}
	}

	@Deprecated
	protected UnsafeX509TrustManager() {}

	@Override
	public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

	@Override
	public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[] {};
	}
}