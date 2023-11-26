//package com.synrgy.commit.service.seeder;
//
//import com.synrgy.commit.model.oauth.Client;
//import com.synrgy.commit.model.oauth.Role;
//import com.synrgy.commit.model.oauth.RolePath;
//import com.synrgy.commit.model.oauth.User;
//import com.synrgy.commit.model.piksi.*;
//import com.synrgy.commit.repository.oauth.ClientRepository;
//import com.synrgy.commit.repository.oauth.RolePathRepository;
//import com.synrgy.commit.repository.oauth.RoleRepository;
//import com.synrgy.commit.repository.oauth.UserRepository;
//import com.synrgy.commit.repository.piksi.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@Service
//public class DatabaseSeederAbsensi implements ApplicationRunner {
//
//    private static final String TAG = "DatabaseSeederAbsensi {}";
//
//    private Logger logger = LoggerFactory.getLogger(DatabaseSeederAbsensi.class);
//
//
//    @Autowired
//    private PenugasanRepo penugasanRepo;
//
//    @Autowired
//    private LookupRepository lookupRepository;
//
//    @Autowired
//    private SosialisasiSekolahRepo sosialisasiSekolahRepo;
//
//    @Autowired
//    private JenisSekolahRepo jenisSekolahRepo;
//
//    @Autowired
//    private SekolahRepo sekolahRepo;
//
//
//    private String[] penugasanList = new String[]{
//            "Work From Office(WFO)",
//            "Work From Home",
//            "Work From Anywhere(WFA)"
//    };
//
//
//
//    @Override
//    @Transactional
//    public void run(ApplicationArguments applicationArguments) {
//        this.insertPenugasan();
//
//    }
//
//
//    @Transactional(rollbackOn = Exception.class)// jika terjadi eror,maka rollback
//    public void insertPenugasan() {
//        for (String obj : penugasanList) {
//            System.out.println(obj);
//            Penugasan old = penugasanRepo.findOneByDesc(obj);
//            if (null == old) {
//                Penugasan old2 = new Penugasan();
//                old2.setDeskripsi(obj);
//                penugasanRepo.save(old2);
//            }
//
//        }
//    }
//
//
//}
//
