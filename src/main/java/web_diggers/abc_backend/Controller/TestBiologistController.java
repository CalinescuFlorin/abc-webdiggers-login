package web_diggers.abc_backend.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/bio")
@RequiredArgsConstructor
public class TestBiologistController {
    @RequestMapping
    public ResponseEntity<String> testController(){
        return new ResponseEntity<>("User has access to this resource.", HttpStatus.OK);
    }
}
