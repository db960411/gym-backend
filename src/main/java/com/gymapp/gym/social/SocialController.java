package com.gymapp.gym.social;

import com.gymapp.gym.progress.ProgressDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/social")
public class SocialController {
    @Autowired
    private SocialService socialService;

    @GetMapping
    public ResponseEntity<SocialDto> getOrCreateSocialForUser(HttpServletRequest request) {
        return ResponseEntity.ok(socialService.getOrCreateSocialForUser(request));
    }

    @PostMapping("/add-friend")
    public ResponseEntity<SocialDto> addFriendForUser(HttpServletRequest request, @RequestBody int friendSocialId) {
        return ResponseEntity.ok(socialService.addFriendForUser(request, friendSocialId));
    }

    @PostMapping("/accept-friend")
    public ResponseEntity<SocialDto> acceptFriendForUser(HttpServletRequest request, @RequestBody int friendSocialId) {
        return ResponseEntity.ok(socialService.acceptFriendshipRequestForUser(request, friendSocialId));
    }

    @GetMapping("getProgressOfFriend/{friendSocialId}")
    public ResponseEntity<List<ProgressDto>> getProgressOfFriend(HttpServletRequest request, @PathVariable int friendSocialId) {
        return ResponseEntity.ok(socialService.getProgressOfFriend(request, friendSocialId));
    }

}
