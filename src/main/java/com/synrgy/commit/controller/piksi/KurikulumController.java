package com.synrgy.commit.controller.piksi;

import com.synrgy.commit.model.piksi.Kurikulum;
import com.synrgy.commit.model.piksi.KurikulumMataKuliah;
import com.synrgy.commit.model.piksi.MataKuliah;
import com.synrgy.commit.repository.piksi.KurikulumMatkulRepository;
import com.synrgy.commit.repository.piksi.KurikulumRepository;
import com.synrgy.commit.repository.piksi.MatkulRepository;
import com.synrgy.commit.service.piksi.KurikulumService;
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
@RequestMapping("/v1/kurikulum")
public class KurikulumController {
    @Autowired
    KurikulumRepository kurikulumRepository;

    @Autowired
    KurikulumService kurikulumService;

    @Autowired
    KurikulumMatkulRepository kurikulumMatkulRepository;

    @Autowired
    Response response;

    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();

    @GetMapping("/list")
    public ResponseEntity<Map> list(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<Kurikulum> list = null;
       if (nama != null ) {
            list = kurikulumRepository.findByNamaLike("%" + nama + "%", show_data);
        }else {
            list = kurikulumRepository.getAllData(show_data);
        }
        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> getId(
            @PathVariable(value = "id") Long id) {
        Kurikulum data = kurikulumRepository.getbyID(id);
        return new ResponseEntity<Map>(response.Sukses(data), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> save(@RequestBody() Kurikulum req,
                                    Principal principal) {
        Map map = kurikulumService.insert(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<Map> update(@RequestBody() Kurikulum req,
                                      Principal principal) {
        Map map = kurikulumService.update(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete", "/delete/"})
    public ResponseEntity<Map> delete(@RequestBody() Kurikulum req,
                                      Principal principal) {
        Map map = kurikulumService.delete(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    //=== Kurikulum mata kuliah
    @GetMapping("/mata-kuliah/list")
    public ResponseEntity<Map> list2(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String kode,
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String semester,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype) {
        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<KurikulumMataKuliah> list = null;
        if (kode != null ) {
            list = kurikulumMatkulRepository.getAllDataByMatkulKode("%" + kode + "%", show_data);
        }else  if (nama != null ) {
            list = kurikulumMatkulRepository.getAllDataByMatkulNama("%" + nama + "%", show_data);
        } if (semester != null ){
            list = kurikulumMatkulRepository.getAllDataBySemester("%" + semester + "%", show_data);
        }else {
            list = kurikulumMatkulRepository.getAllData(  show_data);
        }
        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/mata-kuliah/{id}")
    public ResponseEntity<Map> getId2(
            @PathVariable(value = "id") Long id) {
        KurikulumMataKuliah data = kurikulumMatkulRepository.getbyID(id);
        return new ResponseEntity<Map>(response.Sukses(data), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = {"/mata-kuliah/save", "/mata-kuliah/save/"})
    public ResponseEntity<Map> save2(@RequestBody() KurikulumMataKuliah req,
                                    Principal principal) {
        Map map = kurikulumService.insertMataKuliah(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PutMapping(value = {"/mata-kuliah/update", "/mata-kuliah/update/"})
    public ResponseEntity<Map> update(@RequestBody() KurikulumMataKuliah req,
                                      Principal principal) {
        Map map = kurikulumService.updateMataKuliah(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/mata-kuliah/delete", "/mata-kuliah/delete/"})
    public ResponseEntity<Map> delete(@RequestBody() KurikulumMataKuliah req,
                                      Principal principal) {
        Map map = kurikulumService.deleteMataKuliah(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

}
