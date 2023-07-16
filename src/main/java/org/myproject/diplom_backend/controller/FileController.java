package org.myproject.diplom_backend.controller;


import lombok.RequiredArgsConstructor;
import org.myproject.diplom_backend.service.FileService;
import org.myproject.diplom_backend.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final UserService userService;

//    @GetMapping("/")
//    public String mainPage(){
//        return "Hello";
//    }

//    @PreAuthorize("#files == authentication.principal.username")
    @GetMapping("/list")
    public List<String> listMapping(){
//        return userService.findUserByLogin(username).getFiles()
//                .stream()
//                .map(UserFile::getName)
//                .collect(Collectors.toList());
        return new ArrayList<>();
    }

    @PostMapping("/file")
    public List<String> uploadFile(@RequestParam String filename, @RequestParam MultipartFile file) throws IOException {
        System.out.println(filename);
        return fileService.upload(file);
    }
}
