//package com.synrgy.commit.service.seeder;//package com.synrgy.commit.service.seeder;
//
//import com.synrgy.commit.model.piksi.Lookup;
//import com.synrgy.commit.repository.piksi.LookupRepository;
//import com.synrgy.commit.util.ConfigurasiRestTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.util.MultiValueMap;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.atomic.AtomicReference;
//
//@Component
//@Configuration
//@Service
//public class WilayahSeeder implements ApplicationRunner {
//
//
//    private Logger logger = LoggerFactory.getLogger(WilayahSeeder.class);
//
//    @Autowired
//    private ConfigurasiRestTemplate restTemplate;
//
//    @Autowired
//    private LookupRepository lookupRepository;
//
//
//    @Override
//    @Transactional
//    public void run(ApplicationArguments applicationArguments) throws Exception {
//        System.out.println("masuk wllayah insert");
//        this.insertNegara();
//        this.insertWilayah();
//    }
//
//    @Transactional(rollbackOn = Exception.class)// jika terjadi eror,maka rollback
//    public void insertWilayah() throws Exception {
//        try {
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(null, headers);
//
//            Lookup chekProvinsi = lookupRepository.findFirstByType("provinsi");
//
//            if (chekProvinsi == null) {
////            if (true) {
//                ResponseEntity<Map> responseProvinsi = restTemplate.exchange("https://dev.farizdotid.com/api/daerahindonesia/provinsi", HttpMethod.GET, request, Map.class);
//                responseProvinsi.getBody().forEach((keyProvinsi, valueProvinsi) -> {
//
//                    List<Map> dataProvinsi = (List<Map>) valueProvinsi;
//                    dataProvinsi.forEach((objekProvinsi) -> {
//                        AtomicInteger idProvinsiInternet= new AtomicInteger(1);
////                                        System.out.println("objekDesa=" + objekDesa);
//                        objekProvinsi.forEach((keyProvinsi1, valueProvinsi1) -> {
////                            AtomicReference<Integer> idProvinsiInternet = new AtomicReference<>(1);
//                            System.out.println("key2: "+keyProvinsi1 + " : "+valueProvinsi1.toString());
//                            if (keyProvinsi1.equals("id")) {
//                                idProvinsiInternet.set((Integer) valueProvinsi1);
////                                System.out.println("idProvinsiInternet="+idProvinsiInternet);
//                            }
//
//                            if (keyProvinsi1.equals("nama")) {
////                                    System.out.println("nama=" + valueDesa1);
//                                Lookup reqProvinsi = new Lookup();
//                                reqProvinsi.setNama((String) valueProvinsi1);
//                                reqProvinsi.setType("provinsi");
////                                    reqDesa.setIdWilayah(saveKecamatan.getId());
//                                Lookup saveProvinsi = lookupRepository.save(reqProvinsi);
//
//                                //dosave kabupaten
//                                AtomicInteger idKabupatenInternet= new AtomicInteger(1);
//                                ResponseEntity<Map> responseKabupaten = restTemplate.exchange("http://dev.farizdotid.com/api/daerahindonesia/kota?id_provinsi=" + idProvinsiInternet, HttpMethod.GET, request, Map.class);
//                                responseKabupaten.getBody().forEach((keyKabupaten, valueKabupaten) -> {
//                                    System.out.println("objekKabupaten2=" + idProvinsiInternet);
//                                    List<Map> dataKabupaten = (List<Map>) valueKabupaten;
//                                    dataKabupaten.forEach((objekKabupaten) -> {
//                                        System.out.println("objekKabupaten=" + objekKabupaten);
//                                        objekKabupaten.forEach((keyKabupaten1, valueKabupaten1) -> {
//                                            if (keyKabupaten1.equals("id")) {
//                                                idKabupatenInternet.set((Integer) valueKabupaten1);
////                                                System.out.println("idProvinsiInternet="+idProvinsiInternet);
//                                            }
//                                            if (keyKabupaten1.equals("nama")) {
////                                    System.out.println("nama=" + valueDesa1);
//                                                Lookup reqKabupaten = new Lookup();
//                                                reqKabupaten.setNama((String) valueKabupaten1);
//                                                reqKabupaten.setType("kabupaten");
//                                                reqKabupaten.setIdParent(saveProvinsi.getId());
//                                                Lookup saveKabupaten = lookupRepository.save(reqKabupaten);
//                                                AtomicReference<Integer> idKece = new AtomicReference<>(1);
//                                                //dosave kabupaten
//                                                ResponseEntity<Map> responseKecamatan = restTemplate.exchange("http://dev.farizdotid.com/api/daerahindonesia/kecamatan?id_kota=" + idKabupatenInternet, HttpMethod.GET, request, Map.class);
////                System.out.println("responseKecamatan=" + responseKecamatan.getBody());
//                                                responseKecamatan.getBody().forEach((keyKecamatan, valueKecamatan) -> {
////                    System.out.println("valueKecamatan=" + valueKecamatan);
//                                                    //simpan kecamatan value
//                                                    List<Map> data = (List<Map>) valueKecamatan;
//                                                    data.forEach((objekKecamatan) -> {
//
////                        System.out.println("objekKecamatan=" + objekKecamatan);
//                                                        objekKecamatan.forEach((keyKece1, valueKece1) -> {
//
//                                                            if (keyKece1.equals("id")) {
//                                                                idKece.set((Integer) valueKece1);
//                                                            }
//                                                            if (keyKece1.equals("nama")) {
////                                System.out.println("nama=" + valueKece1);
//                                                                Lookup reqKecamatan = new Lookup();
//                                                                reqKecamatan.setNama((String) valueKece1);
//                                                                reqKecamatan.setType("kecamatan");
//                                                                reqKecamatan.setIdParent(saveKabupaten.getId());
//                                                                Lookup saveKecamatan = lookupRepository.save(reqKecamatan);
//
//
//                                                                //insert kelurahan
//                                                                ResponseEntity<Map> responseKelurahan = restTemplate.exchange("http://dev.farizdotid.com/api/daerahindonesia/kelurahan?id_kecamatan=" + idKece, HttpMethod.GET, request, Map.class);
////                                System.out.println("responseKelurahan=" + responseKelurahan.getBody());
//                                                                responseKelurahan.getBody().forEach((keyDesa, valueDesa) -> {
////                                    System.out.println("valueDesa=" + valueDesa);
//                                                                    //simpan Desa value
//                                                                    List<Map> dataDesa = (List<Map>) valueDesa;
//                                                                    dataDesa.forEach((objekDesa) -> {
////                                        System.out.println("objekDesa=" + objekDesa);
//                                                                        objekDesa.forEach((keyDesa1, valueDesa1) -> {
//                                                                            if (keyDesa1.equals("nama")) {
////                                                                                System.out.println("nama=" + valueDesa1);
//                                                                                Lookup reqDesa = new Lookup();
//                                                                                reqDesa.setNama((String) valueDesa1);
//                                                                                reqDesa.setType("kelurahan");
//                                                                                reqDesa.setIdParent(saveKecamatan.getId());
//                                                                                Lookup saveDesa = lookupRepository.save(reqDesa);
//                                                                            }
//                                                                        });
//
//                                                                    });
//
//                                                                });
//                                                            }
//                                                        });
//
//                                                    });
//
//                                                });
//
//                                            }
//                                        });
//
//                                    });
//
//                                });
//
//                            }
//                        });
//
//                    });
//
//                });
//
//
//            }
//
//        } catch (Exception e) {
//            logger.error("wilayah seder: {}", e);
//        } finally {
//            logger.info("wilayah seder sukses generate");
//        }
//    }
//
//    @Transactional(rollbackOn = Exception.class)// jika terjadi eror,maka rollback
//    public void insertNegara() throws Exception {
//        try {
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(null, headers);
//
//            Lookup chekNegara = lookupRepository.findByNamaAndType("Indonesia", "negara");
//            if (chekNegara == null) {
//                ResponseEntity<Object[]> response = restTemplate.exchange("https://restcountries.com/v3.1/all", HttpMethod.GET, request, Object[].class);
//
//                Object[] s = response.getBody();
//                for (Object abc : s) {
//                    Map data = (Map) abc;
////                    System.out.println("datadatadatadata=" + data);
//                    List<Lookup> listSimpan = new ArrayList<>();
//                    data.forEach((key, value) -> {
//
////                        System.out.println("masuk 1="+data);
//                        if (key.equals("name")) {
////                            System.out.println("masuk 2="+value);
//                            Map data2 = (Map) value;
//                            data2.forEach((key2, value2) -> {
////                                System.out.println("masuk 3="+key2);
//                                if (key2.equals("common")) {
//                                    Lookup simpan = new Lookup();
////                                    System.out.println("masuk 4="+value2.toString());
//                                    simpan.setType("negara");
//                                    simpan.setNama(value2.toString());
//                                    listSimpan.add(simpan);
////                                    lookupRepository.save(simpan);
//                                }
//                            });
//                        }
//                    });
//                    lookupRepository.saveAll(listSimpan);
//                }
//
//
//            }
//
//        } catch (Exception e) {
//            logger.error("negara seder: {}", e);
//        } finally {
//            logger.info("negara seder sukses generate");
//        }
//    }
//}
//
