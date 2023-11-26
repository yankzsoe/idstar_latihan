package com.synrgy.commit.dao.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class EmailSenderModel {
    @NotBlank(message = "Pengirim Wajib diisi")
    public String pengirim;
    @NotBlank(message = "Subjek Wajib diisi")
    public String subjek;
    @NotBlank(message = "Kontent Wajib diisi")
    public String kontent;


    public Long sosialisasiSekolahId;
}