package com.example.readingcomicwebsite.controller;

import com.example.readingcomicwebsite.model.Chapter;
import com.example.readingcomicwebsite.model.Image;
import com.example.readingcomicwebsite.service.impl.ChapterService;
import com.example.readingcomicwebsite.service.impl.CloudinaryService;
import com.example.readingcomicwebsite.service.impl.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/cloudinary")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CloudinaryController {
    private CloudinaryService cloudinaryService;
    private ChapterService chapterService;
    private ImageService imageService;

    @GetMapping("/list")
    public ResponseEntity<List<Image>> list(
            @RequestParam("chapterId") Long chapterId
    ) {
        List<Image> images = imageService.list(chapterService.findById(chapterId));
        return ResponseEntity.ok().body(images);
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<String> upload(
            @RequestParam("chapterId") Long chapterId,
            @RequestParam MultipartFile multipartFile
            ) throws IOException {
        Chapter chapter = chapterService.findById(chapterId);
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if (bi == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid image file");
        }
        Map result = cloudinaryService.upload(multipartFile);
        Image image = new Image(
                (String) result.get("original_filename"),
                (String) result.get("url"),
                (String) result.get("public_id"),
                chapter
        );
        imageService.save(image);
        return ResponseEntity.ok().body("Upload successfully");
    }

    @DeleteMapping("/delete/{id}")
public ResponseEntity<String> delete(
            @PathVariable("id") Long id
    ) throws IOException {
        Optional<Image> imageOptional = imageService.getOne(id);
        if (imageOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
        }
        Image image = imageOptional.get();
        String cloudinaryImageId = image.getImageId();
        try {
            cloudinaryService.delete(cloudinaryImageId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail to delete image on cloudinary");
        }
        imageService.delete(id);
        return ResponseEntity.ok().body("Delete successfully");
    }
}
