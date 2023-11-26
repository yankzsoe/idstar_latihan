package com.synrgy.commit.service.piksi;

import com.synrgy.commit.model.piksi.ReportAbsensi;
import com.synrgy.commit.model.piksi.Sekolah;
import com.synrgy.commit.model.piksi.SosialisasiSekolah;

import java.security.Principal;
import java.util.Map;

public interface SosialisasiSekolahService {
    Map insert(Principal principal,SosialisasiSekolah req);

    Map update(Principal principal,SosialisasiSekolah req);

    Map delete(Principal principal,SosialisasiSekolah req);

    Map insertSekolah(Principal principal, Sekolah req);

    Map updateSekolah(Principal principal, Sekolah req);

    Map deleteSekolah(Principal principal, Sekolah req);
}
