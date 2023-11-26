package com.synrgy.commit.idstar.karyawan.service.impl;

import com.synrgy.commit.idstar.karyawan.model.Karyawan;
import com.synrgy.commit.idstar.karyawan.model.KaryawanDetail;
import com.synrgy.commit.idstar.karyawan.repository.KaryawanDetailRepository;
import com.synrgy.commit.idstar.karyawan.repository.KaryawanRepository;
import com.synrgy.commit.idstar.karyawan.service.KaryawanService;
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
public class KaryawanServiceImpl implements KaryawanService {
    @Autowired
    KaryawanRepository karyawanRepository;
    @Autowired
    KaryawanDetailRepository karyawanDetailRepository;
    @Autowired
    TemplateResponse templateResponse;

    @Override
    public Map insert(Karyawan karyawan) {
        try {
            KaryawanDetail karyawanDetailData = new KaryawanDetail();
            karyawanDetailData.setCreated_date(new Date());
            karyawanDetailData.setUpdated_date(new Date());
            karyawanDetailData.setNik(karyawan.getKaryawanDetail().getNik());
            karyawanDetailData.setNpwp(karyawan.getKaryawanDetail().getNpwp());

            Karyawan karyawanData = new Karyawan();
            karyawanData.setName(karyawan.getName());
            karyawanData.setDob(karyawan.getDob());
            karyawanData.setAddress(karyawan.getAddress());
            karyawanData.setStatus(karyawan.getStatus());
            karyawanData.setCreated_date(new Date());
            karyawanData.setUpdated_date(new Date());
            karyawanData.setKaryawanDetail(karyawanDetailData);

            karyawanRepository.save(karyawanData);
            karyawanDetailRepository.save(karyawanDetailData);
            Karyawan updatedKaryawan = karyawanRepository.getbyID(karyawanData.getId());
            log.info("{}","Suskes Menambahkan Karyawan");
            return templateResponse.Success(updatedKaryawan,"Success","200");
        }catch (Exception e) {
            log.error("{}","Error Menambahkan Karyawan : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map update(Karyawan karyawan) {
        try {
            Karyawan checkIdKaryawan = karyawanRepository.getbyID(karyawan.getId());
            if (checkIdKaryawan == null) {
                return templateResponse.Error("Id Tidak Ditemukan","404");
            }

            KaryawanDetail karyawanDetailData = new KaryawanDetail();
            karyawanDetailData.setId(checkIdKaryawan.getKaryawanDetail().getId());
            karyawanDetailData.setCreated_date(checkIdKaryawan.getKaryawanDetail().getCreated_date());
            karyawanDetailData.setUpdated_date(new Date());
            karyawanDetailData.setNik(karyawan.getKaryawanDetail().getNik());
            karyawanDetailData.setNpwp(karyawan.getKaryawanDetail().getNpwp());

            Karyawan karyawanData = new Karyawan();
            karyawanData.setId(karyawan.getId());
            karyawanData.setName(karyawan.getName());
            karyawanData.setDob(karyawan.getDob());
            karyawanData.setAddress(karyawan.getAddress());
            karyawanData.setStatus(karyawan.getStatus());
            karyawanData.setCreated_date(checkIdKaryawan.getCreated_date());
            karyawanData.setUpdated_date(new Date());
            karyawanData.setKaryawanDetail(karyawanDetailData);


            karyawanRepository.save(karyawanData);
            karyawanDetailRepository.save(karyawanDetailData);
            Karyawan updatedKaryawan = karyawanRepository.getbyID(karyawanData.getId());
            log.info("{}","Suskes Update Karyawan");
            return templateResponse.Success(updatedKaryawan,"Success","200");

        }catch (Exception e) {
            log.error("{}","Error Update Karyawan : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map delete(Long id) {
        try {
            Karyawan checkIdKaryawan = karyawanRepository.getbyID(id);
            if (checkIdKaryawan == null) {
                return templateResponse.Error("Id Tidak Ditemukan","404");
            }
//            karyawanRepository.deleteById(karyawan); // hard delete (delete permanent)
            checkIdKaryawan.setDeleted_date(new Date()); // soft delete
            karyawanRepository.save(checkIdKaryawan);

            log.info("{}","Suskes Delete Karyawan ID : " + id);
            return templateResponse.Success("Suskes Delete Karyawan ID : " + checkIdKaryawan.getId(),"Success","200");
        } catch (Exception e) {
            log.error("{}","Error Delete Karyawan : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map getAll(int size, int page) {
        try {
            Pageable show_data = PageRequest.of(page, size);
            Page<Karyawan> list = karyawanRepository.getAllData(show_data);
            log.info("{}","Suskes getAll Karyawan ");
            return templateResponse.Success(list,"Success","200");
        } catch (Exception e) {
            log.error("{}","Error getAll Karyawan : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }

    @Override
    public Map getById(Long id) {
        try {
            Karyawan checkIdKaryawan = karyawanRepository.getbyID(id);
            if (checkIdKaryawan == null) {
                return templateResponse.Error("Id Tidak Ditemukan","404");
            }
            log.info("{}","Suskes getById Karyawan ");
            return templateResponse.Success(checkIdKaryawan,"Success","200");
        }catch (Exception e) {
            log.error("{}","Error getById Karyawan : " + e);
            return templateResponse.Error(e.getMessage(), "400");
        }
    }
}
