package org.myproject.diplom_backend.service;


import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.myproject.diplom_backend.dto.FileDto;
import org.myproject.diplom_backend.entity.User;
import org.myproject.diplom_backend.entity.UserFile;
import org.myproject.diplom_backend.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional(rollbackOn = {IOException.class})
    public void upload(MultipartFile file,
                       String filename) throws IOException {
        User user = getUser();
        if (file != null) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String userPath = uploadDir.getAbsolutePath() + File.separator + user.getId();
            File userDir = new File(userPath);
            if (!userDir.exists()) {
                userDir.mkdir();
            }
            String resultFilename = userDir.getAbsolutePath() + File.separator + filename;
            file.transferTo(new File(resultFilename));
            if (fileIsPresentInDb(user, filename)) {
                throw new FileAlreadyExistsException("Файл с таким именем уже существует");
            }
            fileRepository.save(UserFile.builder()
                    .name(filename)
                    .dateOfChange(new Date())
                    .size(file.getSize())
                    .user(user)
                    .build());
        }
    }

    public List<FileDto> listFiles(Optional<Integer> limit) {
        int lim;
        User user = getUser();
        lim = limit.orElse(7);
        if (!user.getFiles().isEmpty()) {
            return user.getFiles().stream()
                    .map(userFile -> FileDto.builder()
                            .filename(userFile.getName())
                            .dateOfChange(userFile.getDateOfChange())
                            .size(userFile.getSize())
                            .build())
                    .limit(lim)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(rollbackOn = FileNotFoundException.class)
    public void deleteFile(String filename) throws FileNotFoundException {
        User user = getUser();
        String path = getUserPath(user, filename);
        File focusFile = getFocusFile(path, filename);
        focusFile.delete();
        fileRepository.deleteById(fileRepository.findByNameAndUserId(filename, user.getId())
                .orElseThrow(FileNotFoundException::new)
                .getId());
    }

    @Transactional(rollbackOn = FileNotFoundException.class)
    public void putFile(String filename, HashMap<String, Object> newFileName) throws FileNotFoundException {
        String focusFileName = newFileName.get("filename").toString();
        User user = getUser();
        String focusDir = getUserPath(user, filename);
        File focusFile = getFocusFile(focusDir, filename);
        UserFile file = fileRepository.findByNameAndUserId(filename, user.getId()).orElseThrow(FileNotFoundException::new);
        file.setName(focusFileName);
        fileRepository.save(file);
        focusFile.renameTo(new File(focusDir + File.separator + focusFileName));
    }

    public File downloadFile(String filename) throws FileNotFoundException {
        User user = getUser();
        String path = getUserPath(user, filename);
        return getFocusFile(path, filename);
    }


    private @NotNull User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(username);
        return userService.findUserByLogin(username).orElseThrow(RuntimeException::new);
    }

    private boolean fileIsPresentInDb(User user, String filename) {
        return user.getFiles().stream()
                .map(UserFile::getName)
                .collect(Collectors.toList())
                .contains(filename);
    }

    private String getUserPath(User user, String filename) throws FileNotFoundException {
        if (!fileIsPresentInDb(user, filename)) {
            throw new FileNotFoundException("Файл не найден в БД");
        }
        return new File(uploadPath + File.separator + user.getId()).getAbsolutePath();
    }

    private File getFocusFile(String path, String filename) throws FileNotFoundException {
        File focusFile = new File(path + File.separator + filename);
        if (!focusFile.exists()) {
            throw new FileNotFoundException("Файл не найден");
        }
        return focusFile;
    }

}
