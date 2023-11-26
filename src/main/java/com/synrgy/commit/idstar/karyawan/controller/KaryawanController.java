package com.synrgy.commit.idstar.karyawan.controller;

import com.synrgy.commit.idstar.karyawan.model.Karyawan;
import com.synrgy.commit.idstar.karyawan.service.KaryawanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/v1/karyawan")
public class KaryawanController {

    @Autowired
    KaryawanService karyawanService;

    @PostMapping("/save")
    public ResponseEntity<Map> addKaryawan(@Valid @RequestBody Karyawan karyawan) {
        Map obj = karyawanService.insert(karyawan);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Map> updateKaryawan(@Valid @RequestBody Karyawan karyawan) {
        Map obj = karyawanService.update(karyawan);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> getById(@PathVariable(value = "id") Long id) {
        Map obj = karyawanService.getById(id);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Map> listKaryawan(@RequestParam() Integer page,
                                            @RequestParam() Integer size) {

        Map list = karyawanService.getAll(size,page);
        return new ResponseEntity<Map>(list, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map> deleteKaryawan(@PathVariable(value = "id") Long id) {
        Map obj = karyawanService.delete(id);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }
}
