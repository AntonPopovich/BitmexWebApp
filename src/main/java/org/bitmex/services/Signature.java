package org.example.framework.services;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import org.example.framework.model.constants.URL.URL;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static org.example.framework.model.constants.URL.UtilURL.REALTIME;
import static org.example.framework.model.constants.Verb.GET;

public class Signature {
  private String verb;
  private URL url;
  private String data;
  private String expires;
  private String apiSecret;
  private String signature;

  public Signature(URL url, String verb, String data, String apiSecret){
    this.url = url;
    this.verb = verb;
    this.data = data;
    this.apiSecret = apiSecret;
    this.expires = createExpires();
    //String path = url.getApiPath() + url.getResourcePath();
    this.signature = createSignature();
    while (signature.length() != 64) {
      createExpires();
      createSignature();
    }
  }
  public Signature(String apiSecret) {  // ("GET" + /realtime + expires) for WebSocket
    this.apiSecret = apiSecret;
    do {
      this.expires = createExpires();
      this.signature = signatureToString(calcHmacSha256(apiSecret.getBytes(StandardCharsets.UTF_8),
            (GET + REALTIME + expires).getBytes(StandardCharsets.UTF_8)));
    } while(signature.length() != 64);

  }

  private String createSignature() {
    String path = url.getApiPath() + url.getResourcePath();
    String sign = signatureToString(calcHmacSha256(apiSecret.getBytes(StandardCharsets.UTF_8),
            (verb + path + expires + data).getBytes(StandardCharsets.UTF_8)));
    return sign;

//    while(signature.length() != 64) {
//      signature = new Signature(url, Verb.POST.toString(), orderJsonStr, apiSecret);
//      expires = sign.getExpires();
//      signature = sign.getSignature();
      //createSignature(url, Verb.POST.toString(), orderJsonStr, expires);
  }
  public String getSignature() {
    return this.signature;
  }
  public String getExpires(){
    return this.expires;
  }
  private String createExpires() {
    return (Instant.now().getEpochSecond() + 10) + "";
  }

  private byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
    byte[] hmacSha256;
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
      mac.init(secretKeySpec);
      hmacSha256 = mac.doFinal(message);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
    return hmacSha256;
  }

  private String signatureToString(byte[] signature) {
    String signatureStr = "";
    signatureStr = String.format("%032x", new BigInteger(1, signature));
    return signatureStr;
  }
}
