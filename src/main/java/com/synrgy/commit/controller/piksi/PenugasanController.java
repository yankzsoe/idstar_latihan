package com.synrgy.commit.controller.piksi;

import com.synrgy.commit.model.piksi.Penugasan;
import com.synrgy.commit.model.piksi.ReportAbsensi;
import com.synrgy.commit.repository.piksi.PenugasanRepo;
import com.synrgy.commit.service.piksi.AbsensiService;
import com.synrgy.commit.util.Response;
import com.synrgy.commit.util.SimpleStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/penugasan")
public class PenugasanController {
    @Autowired
    PenugasanRepo penugasanRepo;

    @Autowired
    Response response;

    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();

    @GetMapping("/list")
    public ResponseEntity<Map> penugasanList(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<Penugasan> list = null;
        if (nama != null ) {
            list = penugasanRepo.findByDeskripsiLike("%" + nama + "%", show_data);
        } else {
            list = penugasanRepo.getAllData(show_data);
        }
        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
    }
}
