package ru.otus.hw.helpers;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@UtilityClass
public class PasswordHelpers {
  private final int SALT_LENGTH = 512;
  private final SecureRandom RAND = new SecureRandom();

  public String generateSalt() {
    var salt = new byte[SALT_LENGTH];
    RAND.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  private final int ITERATIONS = 65536;
  private final int KEY_LENGTH = 512;
  private final String ALGORITHM = "PBKDF2WithHmacSHA512";

  @SneakyThrows
  public String hashPassword(String salt, String password) {
    var spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
    try {
      var hash = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
      return Base64.getEncoder().encodeToString(hash);
    } finally {
      spec.clearPassword();
    }
  }
}
