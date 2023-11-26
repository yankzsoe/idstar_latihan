//package com.synrgy.commit.service.seeder;
//
//import com.synrgy.commit.model.piksi.Lookup;
//import com.synrgy.commit.model.piksi.Penugasan;
//import com.synrgy.commit.repository.piksi.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@Service
//public class DatabaseSeederKelas implements ApplicationRunner {
//
//    private static final String TAG = "DatabaseSeederAbsensi {}";
//
//    private Logger logger = LoggerFactory.getLogger(DatabaseSeederKelas.class);
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
//    private String[] lingkupKelas = new String[]{
//            "intenal",
//            "exsternal",
//            "campuran"
//    };
//
//    private String[] modeKelas = new String[]{
//            "online",
//            "offline",
//            "campuran"
//    };
//
//
//
//    @Override
//    @Transactional
//    public void run(ApplicationArguments applicationArguments) {
//        this.insertLingkupKelas();
//        this.generateSemester();
//        this.insertModeKelas();
//        this.insertTotalSendEmail();
//
//    }
//
//
//    @Transactional(rollbackOn = Exception.class)// jika terjadi eror,maka rollback
//    public void insertLingkupKelas() {
//        try {
//            for (String obj : lingkupKelas) {
//                String type = "lingkup_kelas";
//                Lookup old = lookupRepository.findByNamaAndType(obj, type);
//                if (null == old) {
//                    Lookup old2 = new Lookup();
//                    old2.setNama(obj);
//                    old2.setType(type);
//                    lookupRepository.save(old2);
//                } else {
//                    break;
//                }
//
//            }
//        } catch (Exception e) {
//            logger.error("insertLingkupKelas seder: {}", e);
//        } finally {
//            logger.info("insertLingkupKelas seder sukses generate");
//        }
//    }
//
//    @Transactional(rollbackOn = Exception.class)// jika terjadi eror,maka rollback
//    public void insertModeKelas() {
//        try {
//            for (String obj : modeKelas) {
//                String type = "mode_kelas";
//                Lookup old = lookupRepository.findByNamaAndType(obj, type);
//                if (null == old) {
//                    Lookup old2 = new Lookup();
//                    old2.setNama(obj);
//                    old2.setType(type);
//                    lookupRepository.save(old2);
//                } else {
//                    break;
//                }
//
//            }
//        } catch (Exception e) {
//            logger.error("insertModeKelas seder: {}", e);
//        } finally {
//            logger.info("insertModeKelas seder sukses generate");
//        }
//    }
//
//    @Transactional(rollbackOn = Exception.class)
//    public void generateSemester() {
//        try {
//            String type = "semester";
//            String ganjilText ="ganjil";
//            String genapText ="genap";
//            List<Lookup> list = new ArrayList<>();
//            for (int i=2020; i<=2026; i++) {
//
//                String namaGanjil =i+""+(i+1);
//                Lookup old = lookupRepository.findByNamaAndType(namaGanjil+ganjilText, type);
//                if (null == old) {
//
//                    Lookup ganjil = new Lookup();
//                    ganjil.setNama( namaGanjil+ganjilText);
//                    ganjil.setType(type);
//
//                    Lookup genap = new Lookup();
//                    ganjil.setNama( namaGanjil+genapText);
//                    ganjil.setType(type);
//
//                    list.add(ganjil);
//                    list.add(genap);
//                } else {
//                    break;
//                }
//
//
//            }
//            lookupRepository.saveAll(list);
//        } catch (Exception e) {
//            logger.error("generateSemester seder: {}", e);
//        } finally {
//            logger.info("generateSemester seder sukses generate");
//        }
//    }
//
//    @Transactional(rollbackOn = Exception.class)// jika terjadi eror,maka rollback
//    public void insertTotalSendEmail() {
//        try {
//
//                String type = "total_send_email";
//                Lookup old = lookupRepository.findByNamaAndType("500", type);
//                if (null == old) {
//                    Lookup old2 = new Lookup();
//                    old2.setNama("500");
//                    old2.setType(type);
//                    lookupRepository.save(old2);
//                }
//
//
//        } catch (Exception e) {
//            logger.error("insertTotalSendEmail seder: {}", e);
//        } finally {
//            logger.info("insertTotalSendEmail seder sukses generate");
//        }
//    }
//}
//
