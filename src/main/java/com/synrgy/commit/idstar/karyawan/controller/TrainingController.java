package com.synrgy.commit.idstar.karyawan.controller;

import com.synrgy.commit.idstar.karyawan.model.Training;
import com.synrgy.commit.idstar.karyawan.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/training")
public class TrainingController {
    @Autowired
    TrainingService trainingService;
    @PostMapping("/save")
    public ResponseEntity<Map> save(@RequestBody Training objModel) {
        Map obj = trainingService.insert(objModel);
        return new ResponseEntity<Map>(obj, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Map> update(@RequestBody Training objModel) {
        Map obj = trainingService.update(objModel);
        return new ResponseEntity<Map>(obj,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map> delete(@PathVariable(value = "id") Long id) {
        Map obj = trainingService.delete(id);
        return new ResponseEntity<Map>(obj,HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Map>list(@RequestParam() Integer page, @RequestParam Integer size) {
        Map list = trainingService.getAll(size,page);
        return new ResponseEntity<Map>(list,new HttpHeaders(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<Map>getById(@PathVariable(value = "id")Long id) {
        Map obj = trainingService.getById(id);
        return new ResponseEntity<Map>(obj,HttpStatus.OK);
    }
}
