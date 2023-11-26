package com.synrgy.commit.idstar.karyawan.service.impl;

import com.synrgy.commit.idstar.karyawan.model.Rekening;
import com.synrgy.commit.idstar.karyawan.model.Training;
import com.synrgy.commit.idstar.karyawan.repository.TrainingRepository;
import com.synrgy.commit.idstar.karyawan.service.TrainingService;
import com.synrgy.commit.idstar.karyawan.util.TemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    TrainingRepository trainingRepository;

    @Autowired
    TemplateResponse templateResponse;
    @Override
    public Map insert(Training training) {
        try {
            training.setCreated_date(new Date());
            training.setUpdated_date(new Date());

            Training trainingObj = trainingRepository.save(training);
            log.info("{}","Suskes Menambahkan Training");
            return templateResponse.Success(trainingObj,"Success","200");
        } catch (Exception e) {
            log.error("{}","Error Menambahkan Training : " + e);
            return templateResponse.Error(e.getMessage(),"400");
        }
    }

    @Override
    public Map update(Training training) {
        try {
            Training checkTraining = trainingRepository.getbyID(training.getId());
            if (checkTraining == null) {
                return templateResponse.Error("Training Id "+ training.getId() +" Tidak Ditemukan","404");
            }
            training.setCreated_date(checkTraining.getCreated_date());
            training.setUpdated_date(new Date());
            training.setTema(training.getTema());
            training.setPengajar(training.getPengajar());

            Training trainingObj = trainingRepository.save(training);
            log.info("{}","Suskes Update Training");
            return templateResponse.Success(trainingObj,"Success","200");
        } catch (Exception e) {
            log.error("{}","Error Update Training : " + e);
            return templateResponse.Error(e.getMessage(),"400");
        }
    }

    @Override
    public Map delete(Long id) {
        try {
            Training checkTraining = trainingRepository.getbyID(id);
            if (checkTraining == null) {
                return templateResponse.Error("Id Tidak Ditemukan","404");
            }
//            karyawanRepository.deleteById(karyawan); // hard delete (delete permanent)
            checkTraining.setDeleted_date(new Date()); // soft delete
            trainingRepository.save(checkTraining);

            log.info("{}","Suskes Delete Training ID : " + id);
            return templateResponse.Success("Suskes Delete Training ID : " + checkTraining.getId(),"Success","200");
        } catch (Exception e) {
            log.error("{}","Error Delete Training : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map getAll(int size, int page) {
        try {
            Pageable show_data = PageRequest.of(page, size);
            Page<Training> list = trainingRepository.getAllData(show_data);
            log.info("{}","Suskes getAll Training ");
            return templateResponse.Success(list,"Success","200");
        } catch (Exception e) {
            log.error("{}","Error getAll Training : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map getById(Long id) {
        try {
            Training checkTraining = trainingRepository.getbyID(id);
            if (checkTraining == null) {
                return templateResponse.Error("Training Id "+ id +" Tidak Ditemukan","404");
            }
            log.info("{}","Suskes getById Training ");
            return templateResponse.Success(checkTraining,"Success","200");
        }catch (Exception e) {
            log.error("{}","Error getById Training : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }
}
