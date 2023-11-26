package com.synrgy.commit.controller.piksi;

import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.MataKuliah;
import com.synrgy.commit.repository.piksi.DosenRepository;
import com.synrgy.commit.repository.piksi.MatkulRepository;
import com.synrgy.commit.service.piksi.DosenService;
import com.synrgy.commit.service.piksi.MatkulService;
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
@RequestMapping("/v1/mata-kuliah")
public class MataKuliahController {
    @Autowired
    MatkulRepository matkulRepository;

    @Autowired
    MatkulService matkulService;
    @Autowired
    Response response;

    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();

    @GetMapping("/list")
    public ResponseEntity<Map> list(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String kode,
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<MataKuliah> list = null;
        if (kode != null ) {
            list = matkulRepository.findByKodeLike("%" + kode + "%", show_data);
        } else if (nama != null ) {
            list = matkulRepository.findByNamaLike("%" + nama + "%", show_data);
        }else {
            list = matkulRepository.getAllData(show_data);
        }
        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> getId(
            @PathVariable(value = "id") Long id) {
        MataKuliah data = matkulRepository.getbyID(id);
        return new ResponseEntity<Map>(response.Sukses(data), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> save(@RequestBody() MataKuliah req,
                                    Principal principal) {
        Map map = matkulService.insert(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<Map> update(@RequestBody() MataKuliah req,
                                      Principal principal) {
        Map map = matkulService.update(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete", "/delete/"})
    public ResponseEntity<Map> delete(@RequestBody() MataKuliah req,
                                      Principal principal) {
        Map map = matkulService.delete(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

}
