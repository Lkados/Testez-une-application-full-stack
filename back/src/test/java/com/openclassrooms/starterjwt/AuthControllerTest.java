package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthControllerTest {


    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Autowired
    private AuthController authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);

    @Test
    public void testRegisterUser_Success() {
        // Mock user data
        SignupRequest request = new SignupRequest();
        request.setEmail("testddz@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("test46546");

        // Mock repository behavior
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encoded_password");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());

        // Call the controller method
        ResponseEntity<?> response = authController.registerUser(request);

        // Assert the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        Assertions.assertEquals("User registered successfully!", messageResponse.getMessage());

        // Assert repository interaction
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testRegisterUser_Failure_Missing_Field() {
        // Mock user data
        SignupRequest request = new SignupRequest();
        request.setEmail("testddz@example.com");
        request.setFirstName("");
        request.setLastName("Doe");
        request.setPassword("test456");

        // Mock repository behavior
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encoded_password");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());

        // Call the controller method
        ResponseEntity<?> response = authController.registerUser(request);

        // Assert the response
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        Assertions.assertEquals("Error: Missing required fields!", messageResponse.getMessage());
    }
    @Test
    public void testRegisterUser_Failure() {
        // Mock user data
        SignupRequest request = new SignupRequest();
        request.setEmail("testddz@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("test5465");

        // Mock repository behavior
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Call the controller method
        ResponseEntity<?> response = authController.registerUser(request);

        // Assert the response
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        Assertions.assertEquals("Error: Email is already taken!", messageResponse.getMessage());

        // Assert repository interaction
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(User.class));
    }
    @Test
    public void testAuthenticateUser_Success() {
        String email = "yoga@studio.com";
        String password = "test!1234";
        // Mock user data
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        // Mock repository behavior
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // Mocking the behavior of authenticationManager.authenticate()
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        when(authenticationManager.authenticate(authentication)).thenReturn(authentication);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // Call the controller method
        ResponseEntity<?> response = authController.authenticateUser(request);

        // Assert the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(response.getBody() instanceof MessageResponse);
        MessageResponse messageResponse = (MessageResponse) response.getBody();
        Assertions.assertEquals("User authenticated successfully!", messageResponse.getMessage());

        // Assert repository interaction
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(request.getEmail());
    }
}