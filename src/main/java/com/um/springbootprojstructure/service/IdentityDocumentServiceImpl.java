package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.StatusResponse;
import com.um.springbootprojstructure.entity.IdentityDocument;
import com.um.springbootprojstructure.entity.User;
import com.um.springbootprojstructure.repository.IdentityDocumentRepository;
import com.um.springbootprojstructure.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class IdentityDocumentServiceImpl implements IdentityDocumentService {

    private final IdentityDocumentRepository identityDocumentRepository;
    private final UserRepository userRepository;

    public IdentityDocumentServiceImpl(IdentityDocumentRepository identityDocumentRepository,
                                       UserRepository userRepository) {
        this.identityDocumentRepository = identityDocumentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public IdentityDocument getByUserPublicRef(String publicRef) {
        if (publicRef == null || publicRef.isBlank()) {
            throw new IllegalArgumentException("publicRef is required");
        }

        return identityDocumentRepository.findByUser_PublicRef(publicRef)
                .orElseThrow(() -> new IllegalArgumentException(
                        "identity document not found for user publicRef=" + publicRef));
    }

    @Override
    @Transactional
    public StatusResponse uploadOrReplace(String publicRef, MultipartFile file) {
        if (publicRef == null || publicRef.isBlank()) {
            throw new IllegalArgumentException("publicRef is required");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file is required");
        }

        User user = userRepository.findByPublicRef(publicRef)
                .orElseThrow(() -> new IllegalArgumentException("user not found for publicRef=" + publicRef));

        IdentityDocument doc = identityDocumentRepository.findByUser_Id(user.getId())
                .orElseGet(IdentityDocument::new);

        try {
            doc.setUser(user);
            doc.setFileName(safeFileName(file.getOriginalFilename()));
            doc.setContentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType());
            doc.setData(file.getBytes());

            identityDocumentRepository.save(doc);
            return new StatusResponse("DOCUMENT_UPDATED");
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to read uploaded file");
        }
    }

    private String safeFileName(String original) {
        if (original == null || original.isBlank()) return "document";
        // minimal sanitization
        return original.replaceAll("[\\r\\n\\\\/]+", "_");
    }
}
