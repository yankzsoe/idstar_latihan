package com.synrgy.commit.idstar.karyawan.controller;

import com.synrgy.commit.idstar.karyawan.model.Rekening;
import com.synrgy.commit.idstar.karyawan.service.RekeningService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/v1/rekening")
public class RekeningController {
    @Autowired
    RekeningService rekeningService;

    @PostMapping("/save")
    public ResponseEntity<Map> addRekening(@Valid @RequestBody Rekening rekening) {
        Map obj = rekeningService.insert(rekening);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Map> updateRekening(@Valid @RequestBody Rekening rekening) {
        Map obj = rekeningService.update(rekening);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> getById(@PathVariable(value = "id") Long id) {
        Map obj = rekeningService.getById(id);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Map> listRekening(@RequestParam() Integer page,
                                            @RequestParam() Integer size) {

        Map list = rekeningService.getAll(size,page);
        return new ResponseEntity<Map>(list, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map> deleteRekening(@PathVariable(value = "id") Long id) {
        Map obj = rekeningService.delete(id);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }
}
