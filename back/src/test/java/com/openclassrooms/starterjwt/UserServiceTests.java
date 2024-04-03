package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testFindById() {
        // Mock user data
        String email = "yoga@studio.com";
        String firstName = "Admin";
        String lastName = "Admin";
        String password = "somepassword"; // Initialize the password field
        User user = User.builder()
                .id(1L)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .build();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call the service method
        User foundUser = userService.findById(1L);

        // Assert the result
        Assertions.assertEquals(user, foundUser);
    }
}
