package org.myproject.diplom_backend.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.myproject.diplom_backend.dto.FileDto;
import org.myproject.diplom_backend.entity.User;
import org.myproject.diplom_backend.entity.UserFile;
import org.myproject.diplom_backend.repositories.FileRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class FileServiceTest {

    private final FileRepository fileRepository = mock(FileRepository.class);
    private final UserService userService = mock(UserService.class);
    private final FileService fileService = new FileService(fileRepository, userService);

    private static User user;
    private static UserFile testFile1;
    private static FileDto testFile2;

    private final static String filename  = "testFile1";

    @BeforeAll
    public static void setUp() {
        testFile1 = UserFile.builder()
                .name(filename)
                .size(10L)
                .dateOfChange(new Date())
                .build();
        testFile2 = FileDto.builder()
                .filename(filename)
                .size(10L)
                .dateOfChange(new Date())
                .build();
        user = mock(User.class);
    }

    @Test
    void listFiles() {
        String username = "admin";
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(username);
        when(userService.findUserByLogin(username)).thenReturn(Optional.of(user));

        when(user.getFiles()).thenReturn(Collections.singletonList(testFile1));


        assertEquals(fileService.listFiles(Optional.of(1)).get(0).getFilename(), testFile2.getFilename());
        assertEquals(fileService.listFiles(Optional.of(1)).get(0).getSize(), testFile2.getSize());
    }
}