package web_diggers.abc_backend;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("secrets")
public record SecretConfigProperties(
        String changePasswordSecretKey,
        String registerSecretKey,
        String confirmationTokenKey
) {}
