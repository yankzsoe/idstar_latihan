package com.synrgy.commit.idstar.karyawan.service.impl;

import com.synrgy.commit.idstar.karyawan.model.Karyawan;
import com.synrgy.commit.idstar.karyawan.model.KaryawanTraining;
import com.synrgy.commit.idstar.karyawan.model.Training;
import com.synrgy.commit.idstar.karyawan.repository.KaryawanRepository;
import com.synrgy.commit.idstar.karyawan.repository.KaryawanTrainingRepository;
import com.synrgy.commit.idstar.karyawan.repository.TrainingRepository;
import com.synrgy.commit.idstar.karyawan.service.KaryawanTrainingService;
import com.synrgy.commit.idstar.karyawan.util.TemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
@Service
@Slf4j
public class KaryawanTrainingServiceImpl implements KaryawanTrainingService {
    @Autowired
    KaryawanTrainingRepository karyawanTrainingRepository;
    @Autowired
    KaryawanRepository karyawanRepository;
    @Autowired
    TrainingRepository trainingRepository;
    @Autowired
    TemplateResponse templateResponse;

    @Override
    public Map insert(KaryawanTraining karyawanTraining) {
        try {
            Karyawan checkIdKaryawan = karyawanRepository.getbyID(karyawanTraining.getKaryawan().getId());
            if (checkIdKaryawan == null) {
                return templateResponse.Error("Id Karyawan Tidak ada di database","404");
            }
            Training checkIdTraining = trainingRepository.getbyID(karyawanTraining.getTraining().getId());
            if (checkIdTraining == null) {
                return templateResponse.Error("Id Training Tidak ada di database","404");
            }

            //insert
            KaryawanTraining karyawanTrainingData = new KaryawanTraining();
            karyawanTrainingData.setKaryawan(checkIdKaryawan);
            karyawanTrainingData.setTraining(checkIdTraining);
            karyawanTrainingData.setTraining_date(karyawanTraining.getTraining_date());
            karyawanTrainingData.setCreated_date(new Date());
            karyawanTrainingData.setUpdated_date(new Date());

            KaryawanTraining karyawanTrainingObj = karyawanTrainingRepository.save(karyawanTrainingData);
            log.info("{}","Suskes Menambahkan Karyawan Training");
            return templateResponse.Success(karyawanTrainingObj,"Success","200");
        } catch (Exception e) {
            log.error("{}","Error Menambahkan Karyawan Training : " + e);
            return templateResponse.Error(e.getMessage(),"400");
        }
    }

    @Override
    public Map update(KaryawanTraining karyawanTraining) {
        try {
            Karyawan checkIdKaryawan = karyawanRepository.getbyID(karyawanTraining.getKaryawan().getId());
            if (checkIdKaryawan == null) {
                return templateResponse.Error("Id Karyawan Tidak ada di database", "404");
            }
            Training checkIdTraining = trainingRepository.getbyID(karyawanTraining.getTraining().getId());
            if (checkIdTraining == null) {
                return templateResponse.Error("Id Training Tidak ada di database", "404");
            }
            KaryawanTraining checkIdKaryawanTraining = karyawanTrainingRepository.getbyID(karyawanTraining.getId());
            if (checkIdKaryawanTraining == null) {
                return templateResponse.Error("Id Training Tidak ada di database","404");
            }

            //insert
            KaryawanTraining karyawanTrainingData = new KaryawanTraining();
            karyawanTrainingData.setId(checkIdKaryawanTraining.getId());
            karyawanTrainingData.setKaryawan(checkIdKaryawan);
            karyawanTrainingData.setTraining(checkIdTraining);
            karyawanTrainingData.setTraining_date(karyawanTraining.getTraining_date());
            karyawanTrainingData.setCreated_date(checkIdKaryawanTraining.getCreated_date());
            karyawanTrainingData.setUpdated_date(new Date());

            KaryawanTraining karyawanTrainingObj = karyawanTrainingRepository.save(karyawanTrainingData);
            log.info("{}","Suskes Update Karyawan Training");
            return templateResponse.Success(karyawanTrainingObj, "Success", "200");
        } catch (Exception e) {
            log.error("{}","Error Update Karyawan Training : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map delete(Long id) {
        try {
            KaryawanTraining checkIdKaryawanTraining = karyawanTrainingRepository.getbyID(id);
            if (checkIdKaryawanTraining == null) {
                return templateResponse.Error("Id Training Tidak ada di database","404");
            }
//            karyawanTrainingRepository.deleteById(obj); // hard delete
            checkIdKaryawanTraining.setDeleted_date(new Date()); // soft delete
            KaryawanTraining karyawanTrainingObj = karyawanTrainingRepository.save(checkIdKaryawanTraining);
            log.info("{}","Suskes Delete Karyawan Training ID : " + id);
            return templateResponse.Success("Suskes Delete Karyawan Training ID : " + checkIdKaryawanTraining.getId(),"Success","200");
        } catch (Exception e) {
            log.error("{}","Error Delete Karyawan Training : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map getAll(int size, int page) {
        try {
            Pageable show_data = PageRequest.of(page, size);
            Page<KaryawanTraining> list = karyawanTrainingRepository.getAllData(show_data);
            log.info("{}","Suskes getAll Karyawan Training ");
            return templateResponse.Success(list,"Success","200");
        } catch (Exception e) {
            log.error("{}","Error getAll Karyawan Training : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map getById(Long id) {
        try {
            KaryawanTraining karyawanTraining = karyawanTrainingRepository.getbyID(id);
            if (karyawanTraining == null) {
                return templateResponse.Error("Karyawan Training Id "+ id +" Tidak Ditemukan","404");
            }
            log.info("{}","Suskes getById Karyawan Training ");
            return templateResponse.Success(karyawanTraining,"Success","200");
        }catch (Exception e) {
            log.error("{}","Error getById Karyawan Training : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }
}
