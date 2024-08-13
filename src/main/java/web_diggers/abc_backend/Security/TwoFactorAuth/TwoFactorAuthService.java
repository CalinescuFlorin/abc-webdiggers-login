package web_diggers.abc_backend.Security.TwoFactorAuth;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@Slf4j

public class TwoFactorAuthService {
    public String generateNewCode(){
        return new DefaultSecretGenerator().generate();
    }

    public String generateQrCodeImageUrl(String code,String email){
        QrData data = new QrData.Builder()
                .label(email)
                .secret(code)
                .issuer("ABC")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build()
                ;

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageData= new byte[0];
        try{
            imageData = generator.generate(data);
        }catch (QrGenerationException e){
            e.printStackTrace();
            log.error("Error while generating Qr-Code");
        }
        return getDataUriForImage(imageData, generator.getImageMimeType());
    }

    public boolean isOtpValid(String secret, String code){
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        return verifier.isValidCode(secret, code);
    }

    public boolean isOtpNotValid(String secret, String code){
        return !this.isOtpValid(secret, code);
    }
}
