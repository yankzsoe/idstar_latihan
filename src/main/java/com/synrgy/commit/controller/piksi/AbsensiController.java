package com.synrgy.commit.controller.piksi;

import com.synrgy.commit.config.Config;
//import com.synrgy.commit.controller.BookmarkController;
import com.synrgy.commit.controller.BookmarkController;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Penugasan;
import com.synrgy.commit.model.piksi.ReportAbsensi;
import com.synrgy.commit.pdf.PdfGeneratorRunner;
import com.synrgy.commit.repository.piksi.AbsensiRepository;
import com.synrgy.commit.repository.piksi.PenugasanRepo;
import com.synrgy.commit.service.FollowService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.AbsensiService;
import com.synrgy.commit.util.Response;
import com.synrgy.commit.util.SimpleStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/absensi")
public class AbsensiController {

    @Autowired
    AbsensiService absensiService;
    Config config = new Config();

    @Autowired
    AbsensiRepository absensiRepository;

    @Autowired
    BookmarkController bookmarkControllerl;

    @Value("${BASEURL:}")//FILE_SHOW_RUL
    private String BASEURL;

    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> save(@RequestBody() ReportAbsensi reportAbsensi,
                                    Principal principal) {
        Map map = absensiService.insert(principal, reportAbsensi);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @Autowired
    Response response;

    @Autowired
    PdfGeneratorRunner pdfGeneratorRunner;

    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();

    @Autowired
    public BookmarkController bookmarkController;

    @GetMapping("/list")
    public ResponseEntity<Map> absensiList(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype,
            Principal principal) {
        User idUser = bookmarkController.getUserIdToken(principal, userDetailsService);
        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<ReportAbsensi> list = absensiRepository.getAllDataByUser(idUser,show_data);
        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> penugasanRepo(
            @PathVariable(value = "id") Long id) {
        ReportAbsensi data = absensiRepository.getbyID(id);
        return new ResponseEntity<Map>(response.Sukses(data), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/reporting")
    public ResponseEntity<Map> reporting(
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateto,
            Principal principal) throws ParseException {
        User idUser = bookmarkControllerl.getUserIdToken(principal, userDetailsService);

        List<ReportAbsensi> list = absensiRepository.getAllDataListRangeDate(
                config.convertStringToDateFrom(dateFrom),config.convertStringToDateTo(dateto),idUser);
       String fileName = pdfGeneratorRunner.generateReport(list,idUser);

        return new ResponseEntity<Map>(response.Sukses(BASEURL+"/showFile/report/"+fileName), new HttpHeaders(), HttpStatus.OK);
    }
}
