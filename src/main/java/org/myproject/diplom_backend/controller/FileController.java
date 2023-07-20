package org.myproject.diplom_backend.controller;


import lombok.RequiredArgsConstructor;
import org.myproject.diplom_backend.dto.FileDto;
import org.myproject.diplom_backend.service.FileService;
import org.myproject.diplom_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

//    @GetMapping("/")
//    public String mainPage() {
//        return "Hello";
//    }

    @GetMapping("/list")
    public ResponseEntity<?> listMapping(@RequestParam Optional<Integer> limit) {

        return new ResponseEntity<>(fileService.listFiles(limit), HttpStatus.OK);
    }


    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam String filename,
                                        @RequestParam MultipartFile file) {
        System.out.println(filename);
        try {
            fileService.upload(file, filename);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(String.format(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam String filename) {
        try {
            fileService.deleteFile(filename);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(String.format(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/file")
    public ResponseEntity<?> putFile(@RequestParam String filename, @RequestBody HashMap<String, Object> focusFileName) {
        try {
            fileService.putFile(filename, focusFileName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(String.format(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/file")
    public ResponseEntity<?> getFile(@RequestParam String filename) {
        try {
            return new ResponseEntity<>(fileService.downloadFile(filename), HttpStatus.OK) ;
        } catch (FileNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
