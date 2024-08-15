package web_diggers.abc_backend.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/visitor")
@RequiredArgsConstructor
@Tag(name="User Access Test")
public class TestVisitorController {
    @GetMapping
    public ResponseEntity<String> testController(){
        return new ResponseEntity<>("User has access to this resource.", HttpStatus.OK);
    }
}
