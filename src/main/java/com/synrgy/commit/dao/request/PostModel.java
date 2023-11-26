package com.synrgy.commit.dao.request;

import com.synrgy.commit.model.FilePost;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PostModel {
    @NotEmpty(message = "status is mandatory")
    public boolean status;
    @NotEmpty(message = "tags is mandatory")
    public String tags;
    public String desc;
    public Boolean getStatus() {
        return status;
    }
}
