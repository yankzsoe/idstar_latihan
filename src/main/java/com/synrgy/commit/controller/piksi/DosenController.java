package com.synrgy.commit.controller.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.Penugasan;
import com.synrgy.commit.model.piksi.SosialisasiSekolah;
import com.synrgy.commit.repository.piksi.DosenRepository;
import com.synrgy.commit.repository.piksi.PenugasanRepo;
import com.synrgy.commit.service.piksi.DosenService;
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
@RequestMapping("/v1/dosen")
public class DosenController {
    @Autowired
    DosenRepository dosenRepository;

    @Autowired
    DosenService dosenService;


    @Autowired
    Response response;

    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();

    @GetMapping("/list")
    public ResponseEntity<Map> list(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String nidn,
            @RequestParam(required = false) String nik,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<Dosen> list = null;
        if (nidn != null ) {
            list = dosenRepository.findByNidnLike("%" + nidn + "%", show_data);
        } else if (nik != null ) {
            list = dosenRepository.findByNikLike("%" + nik + "%", show_data);
        }else {
            list = dosenRepository.getAllData(show_data);
        }
        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Map> getId(
            @PathVariable(value = "id") Long id) {
        Dosen data = dosenRepository.getbyID(id);
        return new ResponseEntity<Map>(response.Sukses(data), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> save(@RequestBody() Dosen req,
                                    Principal principal) {
        Map map = dosenService.insert(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<Map> update(@RequestBody() Dosen req,
                                      Principal principal) {
        Map map = dosenService.update(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete", "/delete/"})
    public ResponseEntity<Map> delete(@RequestBody() Dosen req,
                                      Principal principal) {
        Map map = dosenService.delete(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

}
