package com.synrgy.commit.service.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.Kelas;

import java.security.Principal;
import java.util.Map;

public interface KelasService {
    Map insert(Principal principal, Kelas obj);

    Map update(Principal principal, Kelas obj);

    Map delete(Principal principal, Kelas obj);
}
