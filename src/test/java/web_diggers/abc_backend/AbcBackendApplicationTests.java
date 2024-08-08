package web_diggers.abc_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import web_diggers.abc_backend.security.auth.AuthenticationController;
import web_diggers.abc_backend.security.auth.model.AuthenticationRequest;
import web_diggers.abc_backend.security.auth.model.AuthenticationResponse;
import web_diggers.abc_backend.security.auth.model.RegisterRequest;
import web_diggers.abc_backend.security.user.UserController;
import web_diggers.abc_backend.security.user.UserRepository;
import web_diggers.abc_backend.security.user.model.Role;
import web_diggers.abc_backend.security.user.model.User;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.DEFINED_PORT)
class AbcBackendApplicationTests {
	@Autowired
	private AuthenticationController authenticationController;
	@Autowired
	private UserController userController;
	@Autowired
	private UserRepository userRepository;

	private final String siteLink = "http://localhost:8080/api/v1/";
	private final TestRestTemplate restTemplate = new TestRestTemplate();


	@Test
	void contextLoads(){
		assertThat(authenticationController).isNotNull();
		assertThat(userController).isNotNull();
		assertThat(userRepository).isNotNull();
	}

	@Test
	void runTests(){
		authenticationTests();
		authorizationTests();
	}

	void authenticationTests(){
		registerShouldCreateAccount();
		registerWithUsedAddressShouldThrowError();
		loginWithExistingAccountIsSuccessful();
		loginWithNonExistingAccountIsUnsuccessful();
		loginWithIncorrectCredentialsIsUnsuccessful();
		cleanAuthTests();
	}

	void authorizationTests(){
		testNormalUserAuthorizationLevel();
		testBiologistAuthorizationLevel();
		testArchaeologistAuthorizationLevel();
		testAdminAuthorizationLevel();
	}

	void registerShouldCreateAccount(){
		RegisterRequest request = RegisterRequest.builder()
				.email("auth_test_account@abc_test.test")
				.password("auth_test")
				.firstName("auth")
				.lastName("test")
				.build();

		// Send register request to endpoint
		ParameterizedTypeReference<AuthenticationResponse> responseType = new ParameterizedTypeReference<AuthenticationResponse>() { };
		ResponseEntity<AuthenticationResponse> result = restTemplate.exchange(
				siteLink + "auth/register", HttpMethod.POST,
				new HttpEntity<>(request), responseType);

		// Verify response
		AuthenticationResponse response = result.getBody();
		assertThat(response).isNotNull();

		assertThat(response.getStatus()).isEqualTo("success");
		assertThat(response.getMessage()).isEqualTo("Account created.");
		assertThat(response.getRole()).isEqualTo("USER");
		assertThat(response.getFirstName()).isEqualTo("auth");
		assertThat(response.getLastName()).isEqualTo("test");
		assertThat(response.getToken()).isNotNull();

		// Verify that user was added into the database
		Optional<User> query = userRepository.findUserByEmail("auth_test_account@abc_test.test");
		assertThat(query).isPresent();
		User testUser = query.get();

		assertThat(testUser.getRole().getValue()).isEqualTo("ROLE_USER");
		assertThat(testUser.getFirstName()).isEqualTo("auth");
		assertThat(testUser.getLastName()).isEqualTo("test");
		assertThat(testUser.getPassword()).isNotNull();
	}

	void registerWithUsedAddressShouldThrowError(){
		RegisterRequest request = RegisterRequest.builder()
				.email("auth_test_account@abc_test.test")
				.password("auth_test2")
				.firstName("auth2")
				.lastName("test2")
				.build();

		// Send register request to endpoint
		ParameterizedTypeReference<AuthenticationResponse> responseType = new ParameterizedTypeReference<AuthenticationResponse>() { };
		ResponseEntity<AuthenticationResponse> result = restTemplate.exchange(
				siteLink + "auth/register", HttpMethod.POST,
				new HttpEntity<>(request), responseType);

		// Verify response
		AuthenticationResponse response = result.getBody();
		assertThat(response).isNotNull();

		assertThat(response.getStatus()).isEqualTo("fail");
		assertThat(response.getMessage()).isEqualTo("Email address is already taken.");
		assertThat(response.getRole()).isEqualTo("");
		assertThat(response.getFirstName()).isEqualTo("");
		assertThat(response.getFirstName()).isEqualTo("");
		assertThat(response.getToken()).isEqualTo("");
	}

	void loginWithExistingAccountIsSuccessful(){
		AuthenticationRequest request = AuthenticationRequest.builder()
				.email("auth_test_account@abc_test.test")
				.password("auth_test")
				.build();

		// Send login request to endpoint
		ParameterizedTypeReference<AuthenticationResponse> responseType = new ParameterizedTypeReference<AuthenticationResponse>() { };
		ResponseEntity<AuthenticationResponse> result = restTemplate.exchange(
				siteLink + "auth/authenticate", HttpMethod.POST,
				new HttpEntity<>(request), responseType);

		// Verify response
		AuthenticationResponse response = result.getBody();
		assertThat(response).isNotNull();

		assertThat(response.getStatus()).isEqualTo("success");
		assertThat(response.getMessage()).isEqualTo("Login successful.");
		assertThat(response.getRole()).isEqualTo("USER");
		assertThat(response.getFirstName()).isEqualTo("auth");
		assertThat(response.getLastName()).isEqualTo("test");
		assertThat(response.getToken()).isNotNull();
	}

	void loginWithNonExistingAccountIsUnsuccessful(){
		AuthenticationRequest request = AuthenticationRequest.builder()
				.email("fictional_auth_test_account@abc_test.test")
				.password("auth_test")
				.build();

		// Send login request to endpoint
		ParameterizedTypeReference<AuthenticationResponse> responseType = new ParameterizedTypeReference<AuthenticationResponse>() { };
		ResponseEntity<AuthenticationResponse> result = restTemplate.exchange(
				siteLink + "auth/authenticate", HttpMethod.POST,
				new HttpEntity<>(request), responseType);

		// Verify response
		AuthenticationResponse response = result.getBody();
		assertThat(response).isNotNull();

		assertThat(response.getStatus()).isEqualTo("fail");
		assertThat(response.getMessage()).isEqualTo("Bad credentials");
		assertThat(response.getRole()).isEqualTo("");
		assertThat(response.getFirstName()).isEqualTo("");
		assertThat(response.getLastName()).isEqualTo("");
		assertThat(response.getToken()).isEqualTo("");
	}

	void loginWithIncorrectCredentialsIsUnsuccessful(){
		AuthenticationRequest request = AuthenticationRequest.builder()
				.email("auth_test_account@abc_test.test")
				.password("auth_test_wrong")
				.build();

		// Send login request with wrong password to endpoint
		ParameterizedTypeReference<AuthenticationResponse> responseType = new ParameterizedTypeReference<AuthenticationResponse>() { };
		ResponseEntity<AuthenticationResponse> result = restTemplate.exchange(
				siteLink + "auth/authenticate", HttpMethod.POST,
				new HttpEntity<>(request), responseType);

		// Verify response
		AuthenticationResponse response = result.getBody();
		assertThat(response).isNotNull();

		assertThat(response.getStatus()).isEqualTo("fail");
		assertThat(response.getMessage()).isEqualTo("Bad credentials");
		assertThat(response.getRole()).isEqualTo("");
		assertThat(response.getFirstName()).isEqualTo("");
		assertThat(response.getLastName()).isEqualTo("");
		assertThat(response.getToken()).isEqualTo("");
	}

	void testNormalUserAuthorizationLevel(){
		RegisterRequest request = RegisterRequest.builder()
				.email("normal_auth_test_account@abc_test.test")
				.password("auth_test")
				.firstName("auth")
				.lastName("test")
				.build();

		ParameterizedTypeReference<AuthenticationResponse> responseType = new ParameterizedTypeReference<AuthenticationResponse>() { };
		ResponseEntity<AuthenticationResponse> result = restTemplate.exchange(
				siteLink + "auth/register", HttpMethod.POST,
				new HttpEntity<>(request), responseType);

		AuthenticationResponse response = result.getBody();
		assertThat(response).isNotNull();

		String token = response.getToken();

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);

		// Normal User Authorization
		ParameterizedTypeReference<String> accessResponseType = new ParameterizedTypeReference<>() { };
		ResponseEntity<String> accessResult = restTemplate.exchange(
				siteLink + "visitor", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getBody()).isEqualTo("User has access to this resource.");

		// Archaeologist Authorization
		accessResult = restTemplate.exchange(
				siteLink + "arheo", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getStatusCode().is4xxClientError()).isTrue();

		// Biologist Authorization
		accessResult = restTemplate.exchange(
				siteLink + "bio", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getStatusCode().is4xxClientError()).isTrue();

		// Admin Authorization
		accessResult = restTemplate.exchange(
				siteLink + "user/users", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getStatusCode().is4xxClientError()).isTrue();

		userRepository.deleteUserByEmail("normal_auth_test_account@abc_test.test");
	}

	void testBiologistAuthorizationLevel(){
		RegisterRequest request = RegisterRequest.builder()
				.email("bio_auth_test_account@abc_test.test")
				.password("auth_test")
				.firstName("auth")
				.lastName("test")
				.build();

		ParameterizedTypeReference<AuthenticationResponse> responseType = new ParameterizedTypeReference<AuthenticationResponse>() { };
		ResponseEntity<AuthenticationResponse> result = restTemplate.exchange(
				siteLink + "auth/register", HttpMethod.POST,
				new HttpEntity<>(request), responseType);

		AuthenticationResponse response = result.getBody();
		assertThat(response).isNotNull();

		String token = response.getToken();

		Optional<User> query = userRepository.findUserByEmail("bio_auth_test_account@abc_test.test");
		assertThat(query).isPresent();
		User testUser = query.get();
		testUser.setRole(Role.BIOLOGIST);
		userRepository.save(testUser);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);

		// Normal User Authorization
		ParameterizedTypeReference<String> accessResponseType = new ParameterizedTypeReference<>() { };
		ResponseEntity<String> accessResult = restTemplate.exchange(
				siteLink + "visitor", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getBody()).isEqualTo("User has access to this resource.");

		// Archaeologist Authorization
		accessResult = restTemplate.exchange(
				siteLink + "arheo", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getStatusCode().is4xxClientError()).isTrue();

		// Biologist Authorization
		accessResult = restTemplate.exchange(
				siteLink + "bio", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getBody()).isEqualTo("User has access to this resource.");

		// Admin Authorization
		accessResult = restTemplate.exchange(
				siteLink + "user/users", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getStatusCode().is4xxClientError()).isTrue();

		userRepository.deleteUserByEmail("bio_auth_test_account@abc_test.test");
	}

	void testArchaeologistAuthorizationLevel(){
		RegisterRequest request = RegisterRequest.builder()
				.email("arheo_auth_test_account@abc_test.test")
				.password("auth_test")
				.firstName("auth")
				.lastName("test")
				.build();

		ParameterizedTypeReference<AuthenticationResponse> responseType = new ParameterizedTypeReference<AuthenticationResponse>() { };
		ResponseEntity<AuthenticationResponse> result = restTemplate.exchange(
				siteLink + "auth/register", HttpMethod.POST,
				new HttpEntity<>(request), responseType);

		AuthenticationResponse response = result.getBody();
		assertThat(response).isNotNull();

		String token = response.getToken();

		Optional<User> query = userRepository.findUserByEmail("arheo_auth_test_account@abc_test.test");
		assertThat(query).isPresent();
		User testUser = query.get();
		testUser.setRole(Role.ARCHAEOLOGIST);
		userRepository.save(testUser);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);

		// Normal User Authorization
		ParameterizedTypeReference<String> accessResponseType = new ParameterizedTypeReference<>() { };
		ResponseEntity<String> accessResult = restTemplate.exchange(
				siteLink + "visitor", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getBody()).isEqualTo("User has access to this resource.");

		// Archaeologist Authorization
		accessResult = restTemplate.exchange(
				siteLink + "arheo", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getBody()).isEqualTo("User has access to this resource.");

		// Biologist Authorization
		accessResult = restTemplate.exchange(
				siteLink + "bio", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getStatusCode().is4xxClientError()).isTrue();

		// Admin Authorization
		accessResult = restTemplate.exchange(
				siteLink + "user/users", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getStatusCode().is4xxClientError()).isTrue();

		userRepository.deleteUserByEmail("arheo_auth_test_account@abc_test.test");
	}

	void testAdminAuthorizationLevel(){
		RegisterRequest request = RegisterRequest.builder()
				.email("admin_auth_test_account@abc_test.test")
				.password("auth_test")
				.firstName("auth")
				.lastName("test")
				.build();

		ParameterizedTypeReference<AuthenticationResponse> responseType = new ParameterizedTypeReference<AuthenticationResponse>() { };
		ResponseEntity<AuthenticationResponse> result = restTemplate.exchange(
				siteLink + "auth/register", HttpMethod.POST,
				new HttpEntity<>(request), responseType);

		AuthenticationResponse response = result.getBody();
		assertThat(response).isNotNull();

		String token = response.getToken();

		Optional<User> query = userRepository.findUserByEmail("admin_auth_test_account@abc_test.test");
		assertThat(query).isPresent();
		User testUser = query.get();
		testUser.setRole(Role.ADMIN);
		userRepository.save(testUser);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);

		// Normal User Authorization
		ParameterizedTypeReference<String> accessResponseType = new ParameterizedTypeReference<>() { };
		ResponseEntity<String> accessResult = restTemplate.exchange(
				siteLink + "visitor", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getBody()).isEqualTo("User has access to this resource.");

		// Archaeologist Authorization
		accessResult = restTemplate.exchange(
				siteLink + "arheo", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getBody()).isEqualTo("User has access to this resource.");

		// Biologist Authorization
		accessResult = restTemplate.exchange(
				siteLink + "bio", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getBody()).isEqualTo("User has access to this resource.");

		// Admin Authorization
		accessResult = restTemplate.exchange(
				siteLink + "user/users", HttpMethod.GET, new HttpEntity<>("", headers), accessResponseType);

		assertThat(accessResult.getStatusCode().is4xxClientError()).isFalse();
		assertThat(accessResult.getStatusCode().is2xxSuccessful()).isTrue();

		userRepository.deleteUserByEmail("admin_auth_test_account@abc_test.test");
	}

	void cleanAuthTests(){
		userRepository.deleteUserByEmail("auth_test_account@abc_test.test");
	}
}