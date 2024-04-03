package com.openclassrooms.starterjwt;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionServiceTests {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateSession() {
        Session session = new Session();
        Mockito.when(sessionRepository.save(Mockito.any(Session.class))).thenReturn(session);

        Session createdSession = sessionService.create(new Session());

        Assertions.assertNotNull(createdSession);
    }

    @Test
    public void testDeleteSession() {
        Long sessionId = 1L;
        Mockito.doNothing().when(sessionRepository).deleteById(sessionId);

        Assertions.assertDoesNotThrow(() -> sessionService.delete(sessionId));
    }

}
