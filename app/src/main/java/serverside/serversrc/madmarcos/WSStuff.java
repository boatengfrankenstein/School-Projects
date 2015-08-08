package serverside.serversrc.madmarcos;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/*
 * Used to ferry requests and responses between a client and a single REST WS provider (URL)
 * 
 * Each WS call packages the request in a "json" field and sends to the URL
 * 
 * Caller must decide to use GET or POST request method
 * 
 * If URL is https URL then SSLContext must not be null
 * 	i.e., caller must call initSSLContext with a cert file
 */
public class WSStuff {
	private String wsAddress;
	private URL wsURL;
	//for SSL connections
	private TrustManagerFactory tmf = null;
	private SSLContext context = null;

	public WSStuff(String addr) {
		if(addr == null || addr.length() == 0)
			throw new IllegalArgumentException("WS URL must be valid HTTP or HTTPS web resource");
		wsAddress = addr;
		try {
			wsURL = new URL(wsAddress);
			String protocol = wsURL.getProtocol();
			if(!protocol.equalsIgnoreCase("https") && !protocol.equalsIgnoreCase("http"))
				throw new IllegalArgumentException("WS URL must be valid HTTP or HTTPS web resource");
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("WS URL must be valid HTTP or HTTPS web resource");
		}
	}
	
	public String sendRequest(HTTPMethod httpMethod, String parms) {
		//parms are the addtl parameters to send the WS
		String response = null;
		
		if(wsURL.getProtocol().equalsIgnoreCase("https") && context == null) 
			throw new IllegalArgumentException("SSL context must be valid before using https request!");

		//if GET with parameters then we need to rebuild the URL
		URL tempURL = wsURL;
		if(parms.length() > 0) {
			if(httpMethod == HTTPMethod.GET)
				try {
					tempURL = new URL(wsAddress + "?json=" + URLEncoder.encode(parms, "UTF-8"));
				} catch (MalformedURLException | UnsupportedEncodingException e) {
				//} catch(Exception e) {
					throw new RuntimeException(e);
				}
		}
		
		try {
			
			HttpURLConnection connection = null;

			if(tempURL.getProtocol().equalsIgnoreCase("https")) { 
				// Create an SSL connection that that uses our SSL context
				connection = (HttpsURLConnection) tempURL.openConnection();
				((HttpsURLConnection) connection).setSSLSocketFactory(context.getSocketFactory());
			} else {
				connection = (HttpURLConnection) tempURL.openConnection();
			}
			//connection.setRequestMethod("POST");
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
	 
			// Send request
			connection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			String urlParameters = "";
			if(parms.length() > 0) {
				if(httpMethod == HTTPMethod.POST) {
					urlParameters += "json=" + parms;
					wr.writeBytes(urlParameters);
				}
			}
			wr.flush();
			wr.close();
	        
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = connection.getInputStream();
			if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
			int bytesRead = 0;
			byte [] buffer = new byte[1024];
			while((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
			response = new String(out.toByteArray());

		} catch (FileNotFoundException e) {
			throw new RuntimeException("Web resource not found!");
		} catch (Exception e) {
			//chain the causing exception to a new RuntimeException
			throw new RuntimeException(e);
		}
		return response;
	}
	
	public void initSSLContext(File certFile) {
		InputStream caInput;
		if(certFile == null)
			throw new IllegalArgumentException("Invalid cert file!");
		
		try {
			caInput = new FileInputStream(certFile);
			if(!initKeyStore(caInput)) {
				throw new RuntimeException("initKeyStore returned false");
			}
			caInput.close();
		} catch(Exception e2) {
			//e2.printStackTrace();
			throw new RuntimeException("Cannot create SSLContext: " + e2.getMessage());
		}
	}
	
	public boolean initKeyStore(InputStream caInput) {
		//caInput is the cert file to trust
		CertificateFactory cf;
		try {
			cf = CertificateFactory.getInstance("X.509");
			Certificate ca;
			ca = cf.generateCertificate(caInput);

			// Create a KeyStore containing our trusted CAs
			String keyStoreType =KeyStore.getDefaultType();
			KeyStore keyStore =KeyStore.getInstance(keyStoreType);
			keyStore.load(null,null);
			keyStore.setCertificateEntry("ca", ca);

			// Create a TrustManager that trusts the CAs in our KeyStore
			String tmfAlgorithm =TrustManagerFactory.getDefaultAlgorithm();
			tmf =TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);
			context =SSLContext.getInstance("TLS");
			context.init(null, tmf.getTrustManagers(),null);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
