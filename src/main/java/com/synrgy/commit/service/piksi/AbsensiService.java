package com.synrgy.commit.service.piksi;

import com.synrgy.commit.model.ReportComment;
import com.synrgy.commit.model.ReportPost;
import com.synrgy.commit.model.ReportUser;
import com.synrgy.commit.model.piksi.ReportAbsensi;

import java.security.Principal;
import java.util.Map;

public interface AbsensiService {
    Map insert(Principal principal,ReportAbsensi obj);
}
