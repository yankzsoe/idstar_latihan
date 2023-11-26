package com.synrgy.commit.service.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.MataKuliah;

import java.security.Principal;
import java.util.Map;

public interface MatkulService {
    Map insert(Principal principal, MataKuliah obj);

    Map update(Principal principal, MataKuliah obj);

    Map delete(Principal principal, MataKuliah obj);
}
