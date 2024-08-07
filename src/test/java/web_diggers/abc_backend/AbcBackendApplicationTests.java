package web_diggers.abc_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class AbcBackendApplicationTests {
	String test = "test";
	@Test
	void contextLoads(){
		assertThat(test).isNotNull();
	}

	@Test
	void runTests(){
		simpleTest();
	}

	void simpleTest(){
		assertThat(test).isEqualTo("test");
	}
}