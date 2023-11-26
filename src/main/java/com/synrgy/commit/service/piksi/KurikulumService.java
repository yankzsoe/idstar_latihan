package com.synrgy.commit.service.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.Kurikulum;
import com.synrgy.commit.model.piksi.KurikulumMataKuliah;

import java.security.Principal;
import java.util.Map;

public interface KurikulumService {
    Map insert(Principal principal, Kurikulum obj);

    Map update(Principal principal, Kurikulum obj);

    Map delete(Principal principal, Kurikulum obj);

    Map insertMataKuliah(Principal principal, KurikulumMataKuliah obj);

    Map updateMataKuliah(Principal principal, KurikulumMataKuliah obj);

    Map deleteMataKuliah(Principal principal, KurikulumMataKuliah obj);
}
