package com.spaceshare.backend.dtos;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

	private Long id;

    private String comment;
    
    private List<CommentDTO> replies;
    
    private UUID userId;
    
    private String userFirstName;
    
    private String userLastName;
    
    private String userPhotoUrl;
}
