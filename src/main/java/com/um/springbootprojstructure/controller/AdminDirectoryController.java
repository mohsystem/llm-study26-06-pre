package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.LdapUserResponse;
import com.um.springbootprojstructure.service.DirectoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/directory")
public class AdminDirectoryController {

    private final DirectoryService directoryService;

    public AdminDirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping("/user-search")
    public ResponseEntity<List<LdapUserResponse>> userSearch(
            @RequestParam String dc,
            @RequestParam String username
    ) {
        return ResponseEntity.ok(directoryService.searchUser(dc, username));
    }
}
