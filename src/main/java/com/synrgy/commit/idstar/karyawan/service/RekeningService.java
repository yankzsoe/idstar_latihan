package com.synrgy.commit.idstar.karyawan.service;

import com.synrgy.commit.idstar.karyawan.model.Karyawan;
import com.synrgy.commit.idstar.karyawan.model.Rekening;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RekeningService {
    public Map insert(Rekening rekening);
    public Map update(Rekening rekening);
    public Map delete(Long id);

    public Map getAll(int size, int page);
    public Map getById(Long id);
}
