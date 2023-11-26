package com.synrgy.commit.idstar.karyawan.controller;

import com.synrgy.commit.idstar.karyawan.model.KaryawanTraining;
import com.synrgy.commit.idstar.karyawan.service.KaryawanTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/karyawan-training")
public class KaryawanTrainingController {
    @Autowired
    KaryawanTrainingService karyawanTrainingService;
    @PostMapping("/save")
    public ResponseEntity<Map> save(@RequestBody KaryawanTraining karyawanTraining) {
        Map obj = karyawanTrainingService.insert(karyawanTraining);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Map> update(@RequestBody KaryawanTraining karyawanTraining) {
        Map obj = karyawanTrainingService.update(karyawanTraining);
        return new ResponseEntity<Map>(obj,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id) {
        Map obj = karyawanTrainingService.delete(id);
        return new ResponseEntity<Map>(obj,HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Map>list(@RequestParam() Integer page, @RequestParam Integer size) {
        Map list = karyawanTrainingService.getAll(size,page);
        return new ResponseEntity<Map>(list,new HttpHeaders(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Map>getById(@PathVariable(value = "id")Long id) {
        Map obj = karyawanTrainingService.getById(id);
        return new ResponseEntity<Map>(obj,HttpStatus.OK);
    }
}
