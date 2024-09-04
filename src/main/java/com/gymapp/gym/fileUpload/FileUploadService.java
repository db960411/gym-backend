package com.gymapp.gym.fileUpload;

import com.gymapp.gym.user.User;
import com.gymapp.gym.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public Image uploadFile(HttpServletRequest request, MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        final String email = request.getHeader("Email");
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User can't be null when inserting image");
        }

        Image image = new Image();
        image.setName(multipartFile.getOriginalFilename());
        image.setType(multipartFile.getContentType());
        image.setData(multipartFile.getBytes());
        imageRepository.save(image);

        user.setImage(image);
        userService.saveUserImage(user);

        return image;
    }
}
