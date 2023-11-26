package com.synrgy.commit.idstar.karyawan.service.impl;

import com.synrgy.commit.idstar.karyawan.model.Karyawan;
import com.synrgy.commit.idstar.karyawan.model.Rekening;
import com.synrgy.commit.idstar.karyawan.repository.KaryawanRepository;
import com.synrgy.commit.idstar.karyawan.repository.RekeningRepository;
import com.synrgy.commit.idstar.karyawan.service.RekeningService;
import com.synrgy.commit.idstar.karyawan.util.TemplateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class RekeningServiceImpl implements RekeningService {
    @Autowired
    KaryawanRepository karyawanRepository;
    @Autowired
    RekeningRepository rekeningRepository;
    @Autowired
    TemplateResponse templateResponse;

    @Override
    public Map insert(Rekening rekening) {
        try {
            Karyawan karyawan = karyawanRepository.getbyID(rekening.getKaryawan().getId());
            if (karyawan == null) {
                return templateResponse.Error("Karyawan Id "+ rekening.getKaryawan().getId() +" Tidak Ditemukan","404");
            }
            Rekening newRekeningData = new Rekening();
            newRekeningData.setNama(rekening.getNama());
            newRekeningData.setJenis(rekening.getJenis());
            newRekeningData.setNorek(rekening.getNorek());
            newRekeningData.setCreated_date(new Date());
            newRekeningData.setUpdated_date(new Date());
            newRekeningData.setKaryawan(karyawan);

            rekeningRepository.save(newRekeningData);
            log.info("{}","Suskes Update Rekening");
            return templateResponse.Success(newRekeningData, "Success","200");
        }catch (Exception e) {
            log.error("{}","Error Update Rekening : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map update(Rekening rekening) {
        try {
            Rekening checkRekening = rekeningRepository.getbyID(rekening.getId());
            if (checkRekening == null) {
                return templateResponse.Error("Rekening Id "+ rekening.getId() +" Tidak Ditemukan","404");
            }
            Karyawan karyawan = karyawanRepository.getbyID(rekening.getKaryawan().getId());

            if (karyawan == null) {
                return templateResponse.Error("Karyawan Id "+ rekening.getKaryawan().getId() +" Tidak Ditemukan","404");
            }
            Rekening newRekeningData = new Rekening();
            newRekeningData.setId(rekening.getId());
            newRekeningData.setNama(rekening.getNama());
            newRekeningData.setJenis(rekening.getJenis());
            newRekeningData.setNorek(rekening.getNorek());
            newRekeningData.setCreated_date(checkRekening.getCreated_date());
            newRekeningData.setUpdated_date(new Date());
            newRekeningData.setDeleted_date(null);
            newRekeningData.setKaryawan(karyawan);
            rekeningRepository.save(newRekeningData);
            Rekening updatedRekening = rekeningRepository.getbyID(newRekeningData.getId());
            log.info("{}","Suskes Update Rekening");
            return templateResponse.Success(updatedRekening, "Success","200");
        }catch (Exception e) {
            log.error("{}","Error Update Rekening : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map delete(Long id) {
        try {
            Rekening checkRekening = rekeningRepository.getbyID(id);
            if (checkRekening == null) {
                return templateResponse.Error("Id Tidak Ditemukan","404");
            }
//            karyawanRepository.deleteById(karyawan); // hard delete (delete permanent)
            checkRekening.setDeleted_date(new Date()); // soft delete
            rekeningRepository.save(checkRekening);

            log.info("{}","Suskes Delete Rekening ID : " + id);
            return templateResponse.Success("Suskes Delete Rekening ID : " + checkRekening.getId(),"Success","200");
        } catch (Exception e) {
            log.error("{}","Error Delete Rekening : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map getAll(int size, int page) {
        try {
            Pageable show_data = PageRequest.of(page, size);
            Page<Rekening> list = rekeningRepository.getAllData(show_data);
            log.info("{}","Suskes getAll Rekening ");
            return templateResponse.Success(list,"Success","200");
        } catch (Exception e) {
            log.error("{}","Error getById Rekening : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map getById(Long id) {
        try {
            Rekening checkRekening = rekeningRepository.getbyID(id);
            if (checkRekening == null) {
                return templateResponse.Error("Id Tidak Ditemukan","404");
            }
            log.info("{}","Suskes getById Rekening ");
            return templateResponse.Success(checkRekening,"Success","200");
        }catch (Exception e) {
            log.error("{}","Error getById Rekening : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }
}
