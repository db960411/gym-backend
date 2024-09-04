package com.gymapp.gym.fileUpload;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/fileUpload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/uploadFile")
    public ResponseEntity<Image> uploadFile (HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileUploadService.uploadFile(request, file));
    }
}
