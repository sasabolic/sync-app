package com.example.adapter.secondary.salesforce.client.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DLSequence;
import org.springframework.stereotype.Component;

/**
 * Used to build JWT for the Salesforce OAuth JWT token flow.
 *
 * @author Saša Bolić
 */
@RequiredArgsConstructor
@Component
public class JwtBuilder {

  private final JwtProperties properties;

  /**
   * Builds a token for the given issuer and subject.
   *
   * @param issuer  the client_id of the connected app i.e. sync-app
   * @param subject the username of the Salesforce account owner
   * @return the string
   */
  public String build(String issuer, String subject) {
    return JWT.create()
        .withIssuer(issuer)
        .withAudience(properties.getAudience())
        .withSubject(subject)
        .withExpiresAt(
            Date.from(LocalDateTime.now().plusSeconds(properties.getExpiration().toSeconds())
                .atZone(ZoneId.systemDefault()).toInstant()))
        .sign(Algorithm.RSA256(null, getPrivateKey()));
  }

  private RSAPrivateKey getPrivateKey() {
    try {
      String privateKeyContent = properties.getPrivateKey()
          .replace("-----BEGIN RSA PRIVATE KEY-----", "")
          .replace("-----END RSA PRIVATE KEY-----", "")
          .replace("\\n", "");

      byte[] encoded = Base64.decodeBase64(privateKeyContent);

      final DLSequence seq = (DLSequence) ASN1Primitive.fromByteArray(encoded);

      final BigInteger modulus = ((ASN1Integer) seq.getObjectAt(1)).getValue();
      final BigInteger privateExponent = ((ASN1Integer) seq.getObjectAt(3)).getValue();

      final RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, privateExponent);
      final PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

      return (RSAPrivateKey) privateKey;
    } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
      throw new JwtBuilderException("Error creating JWT token", e);
    }
  }
}
