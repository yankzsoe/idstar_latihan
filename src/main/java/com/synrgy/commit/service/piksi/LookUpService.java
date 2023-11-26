package com.synrgy.commit.service.piksi;

import com.synrgy.commit.model.piksi.Kelas;
import com.synrgy.commit.model.piksi.Lookup;

import java.security.Principal;
import java.util.Map;

public interface LookUpService {
    Map insert(Principal principal, Lookup obj);

    Map update(Principal principal, Lookup obj);

    Map delete(Principal principal, Lookup obj);
}
