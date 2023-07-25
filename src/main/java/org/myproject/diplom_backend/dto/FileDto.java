package org.myproject.diplom_backend.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class FileDto {
    private String filename;
    private Date dateOfChange;
    private Long size;
}
