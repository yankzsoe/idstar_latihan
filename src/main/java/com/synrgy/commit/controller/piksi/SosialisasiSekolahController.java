package com.synrgy.commit.controller.piksi;

import com.synrgy.commit.config.Config;
//import com.synrgy.commit.controller.BookmarkController;
import com.synrgy.commit.dao.request.EmailSenderModel;
import com.synrgy.commit.dao.request.ResetPasswordModel;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.*;
import com.synrgy.commit.report.SosialisasiSekolahExcelExporter;
import com.synrgy.commit.repository.piksi.*;
import com.synrgy.commit.service.email.EmailSender;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.SosialisasiSekolahService;
import com.synrgy.commit.util.Response;
import com.synrgy.commit.util.SimpleStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/v1/sosialisasi-sekolah")
public class SosialisasiSekolahController {

    @Autowired
    SosialisasiSekolahService sosialisasiSekolahService;
    Config config = new Config();

    @Autowired
    SosialisasiSekolahRepo sosialisasiSekolahRepo;

    @Value("${BASEURLSHOW}")//FILE_SHOW_RUL
    private String BASEURLSHOW;

    @Autowired
    public EmailSender emailSender;

    @Value("${app.uploadto.cdn}")//FILE_SHOW_RUL
    private String UPLOADED_FOLDER;


    @Autowired
    SekolahRepo sekolahRepo;

    @Autowired
    public LookupRepository lookupRepository;


    @Autowired
    JenisSekolahRepo jenisSekolahRepo;


    @Autowired
    private Oauth2UserDetailsService userDetailsService;


    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> save(@RequestBody() SosialisasiSekolah req,
                                    Principal principal) {
        Map map = sosialisasiSekolahService.insert(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<Map> update(@RequestBody() SosialisasiSekolah req,
                                      Principal principal) {
        Map map = sosialisasiSekolahService.update(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete", "/delete/"})
    public ResponseEntity<Map> delete(@RequestBody() SosialisasiSekolah req,
                                      Principal principal) {
        Map map = sosialisasiSekolahService.delete(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Autowired
    Response response;


    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();

//    @Autowired
//    public BookmarkController bookmarkController;

//    @GetMapping("/list")
//    public ResponseEntity<Map> listAllByByser(
//            @RequestParam() Integer page,
//            @RequestParam(required = true) Integer size,
//            @RequestParam(required = false) String orderby,
//            @RequestParam(required = false) String ordertype,
//            @RequestParam(required = false) String nama,
//            @RequestParam(required = false) String namaSekolah,
//            @RequestParam(required = false) String email,
//            @RequestParam(required = false) String sendEmail, //value y:nampilin list sudah terkirim or n: nampilin yang belum terkirim
//            Principal principal) {
//        User idUser = bookmarkController.getUserIdToken(principal, userDetailsService);
//        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
//        Page<SosialisasiSekolah> list = null;
//
//        if (nama != null && !nama.isEmpty()) {
//            list = sosialisasiSekolahRepo.findByNamaLike("%" + nama.toLowerCase() + "%", show_data);
//        } else if (!StringUtils.isEmpty(sendEmail)) {
//            if (sendEmail.equals("y")) {
//                list = sosialisasiSekolahRepo.showSendEmail(show_data);
//            } else {
//                //gagal
//                list = sosialisasiSekolahRepo.showNotSendEmail(show_data);//belum send email
////                list = sosialisasiSekolahRepo.findByStatusIsNull(show_data);//belum send email
//            }
//        } else if (namaSekolah != null && !namaSekolah.isEmpty()) {
//            list = sosialisasiSekolahRepo.findByNamaSekolahLike("%" + namaSekolah.toLowerCase() + "%", show_data);
//        } else if (email != null && !email.isEmpty()) {
//            list = sosialisasiSekolahRepo.findByEmailLike("%" + email.toLowerCase() + "%", show_data);
//        } else {
//            list = sosialisasiSekolahRepo.getAllData(show_data);
//        }
//        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
//    }

    @GetMapping("/list-jenis-sekolah")
    public ResponseEntity<Map> listJenisSekolah(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype,
            Principal principal) {
//        User idUser = bookmarkController.getUserIdToken(principal, userDetailsService);
        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<JenisSekolah> list = jenisSekolahRepo.getAllData(show_data);
        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> getId(
            @PathVariable(value = "id") Long id) {
        SosialisasiSekolah data = sosialisasiSekolahRepo.getbyID(id);
        return new ResponseEntity<Map>(response.Sukses(data), new HttpHeaders(), HttpStatus.OK);
    }

    @Autowired
    private DefaultTokenServices tokenServices = new DefaultTokenServices();

    @Autowired
    private TokenStore tokenStore;

    @GetMapping("/revoke/{id}")
    public ResponseEntity<Map> revoke(
            @PathVariable(value = "id") String id
    ) {
//        DefaultTokenServices services = new DefaultTokenServices();
//        tokenServices.revokeToken(id);
        OAuth2AccessToken oauth = tokenStore.readAccessToken(id);

        tokenStore.removeAccessToken(oauth);


        return new ResponseEntity<Map>(response.Sukses(oauth), new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/sekolah/{id}")
    public ResponseEntity<Map> getIdSekolah(
            @PathVariable(value = "id") Long id) {
        Sekolah data = sekolahRepo.getbyID(id);
        return new ResponseEntity<Map>(response.Sukses(data), new HttpHeaders(), HttpStatus.OK);
    }


//    @GetMapping("/list-sekolah")
//    public ResponseEntity<Map> listSekolah(
//            @RequestParam() Integer page,
//            @RequestParam(required = true) Integer size,
//            @RequestParam(required = false) String orderby,
//            @RequestParam(required = false) String ordertype,
//            @RequestParam(required = false) String nama,
//            Principal principal) {
//        User idUser = bookmarkController.getUserIdToken(principal, userDetailsService);
//        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
//        Page<Sekolah> list = null;
//
//        if (nama != null && !nama.isEmpty()) {
//            list = sekolahRepo.findLikeByNama("%" + nama.toLowerCase() + "%", show_data);
//        } else {
//            list = sekolahRepo.getAllData(show_data);
//        }
//        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
//    }

    @PostMapping(value = {"/save-sekolah", "/save-sekolah/"})
    public ResponseEntity<Map> save2(@RequestBody() Sekolah req,
                                     Principal principal) {
        Map map = sosialisasiSekolahService.insertSekolah(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PutMapping(value = {"/update-sekolah", "/update-sekolah/"})
    public ResponseEntity<Map> update(@RequestBody() Sekolah req,
                                      Principal principal) {
        Map map = sosialisasiSekolahService.updateSekolah(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete-sekolah", "/delete-sekolah/"})
    public ResponseEntity<Map> deleted(@RequestBody() Sekolah req,
                                       Principal principal) {
        Map map = sosialisasiSekolahService.deleteSekolah(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }


    @GetMapping("/export-excel")
    public ResponseEntity<Map> exportToExcel(
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) Long sekolah_id,
            @RequestParam(required = false) Long wilayah_id) throws IOException {


        List<SosialisasiSekolah> list;
        if (nama != null && !nama.isEmpty()) {
            list = sosialisasiSekolahRepo.listNama(nama.toLowerCase());
        } else if (sekolah_id != null) {
            Sekolah chek = sekolahRepo.getbyID(sekolah_id);
            list = sosialisasiSekolahRepo.listSekolah(chek);
        } else if (wilayah_id != null) {
            Lookup chek = lookupRepository.getbyID(wilayah_id);
            list = sosialisasiSekolahRepo.listWilayah(chek);
        } else {
            list = sosialisasiSekolahRepo.listAll();
        }
        SosialisasiSekolahExcelExporter sosialisasiSekolahExcelExporter = new SosialisasiSekolahExcelExporter(UPLOADED_FOLDER, BASEURLSHOW);

        return new ResponseEntity<Map>(response.Sukses(sosialisasiSekolahExcelExporter.export(list)), HttpStatus.OK);

    }

//    @PostMapping("/send-email")// SEND EMAIL TO BANYAK BELUM DIPAKE
//    @Transactional
//    public ResponseEntity<Map> sendEmailPassword(@Valid @RequestBody EmailSenderModel request) {
//        String message = "Thanks, please check your email";
//
//        Lookup chekProdOrDev = lookupRepository.findByNamaAndType("prod", "config_email_sosialisasi_sekolah");
//        List<String> dataEmail = new ArrayList<>();
//        if (chekProdOrDev != null) {
//            //prod
//            Pageable show_data = simpleStringUtils.getShort("asc", "id", 0, 500);
//            Page<SosialisasiSekolah> list = sosialisasiSekolahRepo.getAllDataNotInTerkirim(show_data);
//            List<SosialisasiSekolah> listSekolah = new ArrayList<>();
//
//            for (int i = 0; i <= list.getContent().size(); i++) {
//                if (!StringUtils.isEmpty(list.getContent().get(i).getEmail())) {
//                    SosialisasiSekolah data = list.getContent().get(i);
//                    data.setStatus("terkirim");
//                    listSekolah.add(data);
//                    dataEmail.add(list.getContent().get(i).getEmail().replaceAll("\\s+", ""));
//                }
//            }
//        } else {
//            //        testing :dev
//            dataEmail.add("rikialdipari96@gmail.com");
//            dataEmail.add("rikialdipari97@gmail.com");
//            dataEmail.add("rikialdipari98@gmail.com");
//            dataEmail.add("rikialdipari99@gmail.com");
//        }
//
//
//        byte[] decodedBytes = Base64.getDecoder().decode(request.getKontent());
//        String decodedString = new String(decodedBytes);
//
//        String[] from = new String[dataEmail.size()];
//        from = dataEmail.toArray(from);
//        Map cehk = emailSender.sendAsyncManyEmail(request.getPengirim(), from, request.getSubjek(), decodedString);
//
//
//        return new ResponseEntity<Map>(cehk, HttpStatus.OK);
//
//
//    }

    @PostMapping("/send-email")// SEND EMAIL TO BANYAK BELUM DIPAKE
    @Transactional
    public ResponseEntity<Map> sendEmailPassword(@Valid @RequestBody EmailSenderModel request) {
        String message = "Thanks, please check your email";

        Lookup chekProdOrDev = lookupRepository.findByNamaAndType("prod", "config_email_sosialisasi_sekolah");
//        List<String> dataEmail = new ArrayList<>();
        if (chekProdOrDev != null) {
            //prod
            Lookup chekTotalSendEmail = lookupRepository.findByType("total_send_email");
            Pageable show_data = simpleStringUtils.getShort(null, "id", 0, Integer.parseInt(chekTotalSendEmail.getNama()));
            Page<SosialisasiSekolah> list = sosialisasiSekolahRepo.getAllDataNotInTerkirim(show_data);

            List<SosialisasiSekolah> listData = new ArrayList<>();
            try {
                System.out.println("masuk list.getContent()=" + list.getContent().size());
                for (int i = 0; i < list.getContent().size(); i++) {
                    SosialisasiSekolah data = list.getContent().get(i);
                    if (!StringUtils.isEmpty(list.getContent().get(i).getEmail())) {


                        List<String> dataEmail = new ArrayList<>();
                        dataEmail.add(list.getContent().get(i).getEmail().replaceAll("\\s+", ""));

                        byte[] decodedBytes = Base64.getDecoder().decode(request.getKontent());
                        String decodedString = new String(decodedBytes);

                        String[] from = new String[dataEmail.size()];
                        from = dataEmail.toArray(from);
                        String content_1 = decodedString.replace("FORMAT_NAMA_DARI_BE", data.getNama());
                        String content_2 = content_1.replace("FORMAT_SEKOLAH_DARI_BE", (data.getSekolah() == null ? "" : data.getSekolah().getNama()));
                        Map chek = emailSender.sendAsyncManyEmail(request.getPengirim(), from, request.getSubjek(), content_2);
                        Boolean cehkSukses = Boolean.valueOf(String.valueOf(chek.get("status").equals("200")));
                        if (cehkSukses) {
                            data.setStatus("terkirim");
                            listData.add(data);
//                            sosialisasiSekolahRepo.save(data);
                        }
                    } else {
                        data.setStatus("gagal");
                        listData.add(data);
                    }
                }
                sosialisasiSekolahRepo.saveAll(listData);
            } catch (Exception e) {
                return new ResponseEntity<Map>(response.Error("Gagal=" + e), HttpStatus.OK);
            }
        } else {
            //        testing :dev
            byte[] decodedBytes = Base64.getDecoder().decode(request.getKontent());
            String decodedString = new String(decodedBytes);
            String content_1 = decodedString.replace("FORMAT_NAMA_DARI_BE", "NAMA TESTING");
            String content_2 = content_1.replace("FORMAT_SEKOLAH_DARI_BE", "SEKOLAH TESTING");
            System.out.println(decodedString);
            try {
                for (int i = 0; i <= 10; i++) {
                    List<String> dataEmail = new ArrayList<>();
                    dataEmail.add("rikialdipari96@gmail.com");
                    String[] from = new String[dataEmail.size()];
                    from = dataEmail.toArray(from);
                    emailSender.sendAsyncManyEmail(request.getPengirim(), from, request.getSubjek(), content_2);
                }
            } catch (Exception e) {
                return new ResponseEntity<Map>(response.Error("Gagal"), HttpStatus.OK);
            }
        }
        return new ResponseEntity<Map>(response.Sukses("Suksess"), HttpStatus.OK);


    }

    @PostMapping("/send-email/person")//send OTP
    @Transactional
    public ResponseEntity<Map> sendEmailPasswordPerson(@Valid @RequestBody EmailSenderModel request) {
        String message = "Thanks, please check your email";
        if (request.getSosialisasiSekolahId() == null) {
            return new ResponseEntity<Map>(response.Error("Sosialisasi Sekolah Id wajib diisi."), HttpStatus.OK);
        }
        Lookup chekProdOrDev = lookupRepository.findByNamaAndType("prod", "config_email_sosialisasi_sekolah");
        List<String> dataEmail = new ArrayList<>();
        if (chekProdOrDev != null) {
            //prod

            SosialisasiSekolah data = sosialisasiSekolahRepo.getbyID(request.getSosialisasiSekolahId());
            if (data == null) {
                return new ResponseEntity<Map>(response.Error("Sosialisasi Sekolah Id tidak ditemukan."), HttpStatus.OK);
            }
            data.setStatus("terkirim");
            dataEmail.add(data.getEmail().replaceAll("\\s+", ""));
        } else {
            //        testing :dev
            dataEmail.add("rikialdipari96@gmail.com");
            dataEmail.add("rikialdipari97@gmail.com");
            dataEmail.add("rikialdipari98@gmail.com");
            dataEmail.add("rikialdipari99@gmail.com");
        }


        byte[] decodedBytes = Base64.getDecoder().decode(request.getKontent());
        String decodedString = new String(decodedBytes);

        String[] from = new String[dataEmail.size()];
        from = dataEmail.toArray(from);
        Map cehk = emailSender.sendAsyncManyEmail(request.getPengirim(), from, request.getSubjek(), decodedString);


        return new ResponseEntity<Map>(cehk, HttpStatus.OK);


    }
}
