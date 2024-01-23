package com.blueberry.model.request;

import com.blueberry.model.dto.AppUserDTO;
import com.blueberry.model.dto.StatusDTO;
import lombok.Data;

@Data
public class SavedArticleRequest {
    private Long id;
    private AppUserDTO appUser;
    private StatusDTO status;
}
