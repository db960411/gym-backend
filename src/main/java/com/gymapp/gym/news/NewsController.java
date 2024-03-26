package com.gymapp.gym.news;


import com.gymapp.gym.notes.PaginatedNotesResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class NewsController {
    private final NewsService service;

    @GetMapping("/news")
    public ResponseEntity<NewsResponse> getNews(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String category) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(service.getNews(pageable, category));
    }

    @GetMapping("/getNewsInformationForSpecificBlog/{blogId}")
    public ResponseEntity<NewsResponse> getSpecificNews(@PathVariable String blogId) {
        return ResponseEntity.ok(service.getSpecificNews(blogId));
    }

    @PostMapping("/new-blogpost")
    public ResponseEntity<NewsResponse> createBlogPost(HttpServletRequest request, @RequestBody NewsResponse newsResponse) {
        return ResponseEntity.ok(service.createBlogPost(request, newsResponse));
    }
}
