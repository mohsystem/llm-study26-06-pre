package com.um.springbootprojstructure.controller;

import com.um.springbootprojstructure.dto.StatusResponse;
import com.um.springbootprojstructure.entity.IdentityDocument;
import com.um.springbootprojstructure.service.IdentityDocumentService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserDocumentController {

    private final IdentityDocumentService identityDocumentService;

    public UserDocumentController(IdentityDocumentService identityDocumentService) {
        this.identityDocumentService = identityDocumentService;
    }

    @GetMapping("/{publicRef}/document")
    public ResponseEntity<byte[]> getIdentityDocument(@PathVariable String publicRef) {
        IdentityDocument doc = identityDocumentService.getByUserPublicRef(publicRef);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(doc.getContentType()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(doc.getFileName()).build());
        headers.setContentLength(doc.getData().length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(doc.getData());
    }

    @PutMapping(
            value = "/{publicRef}/document",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<StatusResponse> uploadOrReplace(
            @PathVariable String publicRef,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(identityDocumentService.uploadOrReplace(publicRef, file));
    }
}
