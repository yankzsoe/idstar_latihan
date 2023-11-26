package com.synrgy.commit.service.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.ReportAbsensi;

import java.security.Principal;
import java.util.Map;

public interface DosenService {
    Map insert(Principal principal, Dosen obj);

    Map update(Principal principal, Dosen obj);

    Map delete(Principal principal, Dosen obj);
}
