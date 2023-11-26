package com.synrgy.commit.service.piksi.impl;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.*;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.repository.piksi.*;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.AbsensiService;
import com.synrgy.commit.service.piksi.SosialisasiSekolahService;
import com.synrgy.commit.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Date;
import java.util.Map;

@Transactional
@Service
public class SosialisasiSekolahImpl implements SosialisasiSekolahService {
    private static final Logger logger = LoggerFactory.getLogger(SosialisasiSekolahImpl.class);
    @Autowired
    Response response;
    @Autowired
    SosialisasiSekolahRepo sosialisasiSekolahRepo;

    @Autowired
    JenisSekolahRepo jenisSekolahRepo;

    @Autowired
    SekolahRepo sekolahRepo;

    @Autowired
    LookupRepository lookupRepository;

    Config config = new Config();
    @Autowired
    UserRepository userRepository;
    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    private User getUserIdToken(Principal principal, Oauth2UserDetailsService userDetailsService) {
        UserDetails user = null;
        String username = principal.getName();
        if (!StringUtils.isEmpty(username)) {
            user = userDetailsService.loadUserByUsername(username);
        }

        if (null == user) {
            throw new UsernameNotFoundException("User not found");
        }
        User idUser = userRepository.findOneByUsername(user.getUsername());
        if (null == idUser) {
            throw new UsernameNotFoundException("User name not found");
        }
        return idUser;
    }


    @Override
    public Map insert(Principal principal, SosialisasiSekolah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getSekolah() == null) {
                return response.Error("Sekolah harus diisi.");
            }

            if (req.getSekolah().getId() == null) {
                return response.Error("Sekolah harus diisi.");
            }

            if (req.getKecamatan() != null) {
                Lookup kecamatan = lookupRepository.getbyID(req.getKecamatan().getId());
                if (null == kecamatan) {
                    return response.Error("Kecamatan " + req.getKecamatan().getId() + " tidak ditemukan.");
                }else {
                    req.setKecamatan(kecamatan);
                }

            }

            //chek penugasan
            if (null == req.getJenisSekolah()) {
                return response.Error("Jenis Sekolah harus diisi.");
            }
            JenisSekolah jenisSekolah = jenisSekolahRepo.getbyID(req.getJenisSekolah().getId());
            if (null == jenisSekolah) {
                logger.error("Jenis Sekolah tidak ditemukan.", req.getJenisSekolah().getId());
                return response.Error("Jenis Sekolah tidak ditemukan.");
            }

            Sekolah sekolah = sekolahRepo.getbyID(req.getSekolah().getId());
            if (null == sekolah) {
                logger.error("Sekolah tidak ditemukan.", req.getJenisSekolah().getId());
                return response.Error("Sekolah tidak ditemukan.");
            }

            req.setCreatedUser(idUser);
            req.setJenisSekolah(jenisSekolah);
            req.setSekolah(sekolah);
            return response.Sukses(sosialisasiSekolahRepo.save(req));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map insertSekolah(Principal principal, Sekolah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getNama() == null) {
                return response.Error("Sekolah harus diisi.");
            }
            Sekolah old = sekolahRepo.findLikeByNama(req.getNama());
            if (null != old) {
                return response.Error("Sekolah " + req.getNama() + " sudah ada.");
            }



            req.setCreatedUser(idUser);
            return response.Sukses(sekolahRepo.save(req));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map updateSekolah(Principal principal, Sekolah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }
            if (req.getNama() == null) {
                return response.Error("Nama harus diisi.");
            }
            Sekolah doUpdate = sekolahRepo.getbyID(req.getId());
            if (null == doUpdate) {
                return response.Error("Id tidak ditemukan.");
            }

            Sekolah old = sekolahRepo.findLikeByNamaAndNotID(req.getNama(), req.getId());
            if (null != old) {
                return response.Error("Sekolah " + req.getNama() + " sudah ada.");
            }

            doUpdate.setCreatedUser(idUser);
            doUpdate.setNama(req.getNama());
            doUpdate.setWilayah(req.getWilayah());



            return response.Sukses(sekolahRepo.save(doUpdate));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map deleteSekolah(Principal principal, Sekolah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            Sekolah doUpdate = sekolahRepo.getbyID(req.getId());
            if (null == doUpdate) {
                return response.Error("Id tidak ditemukan.");
            }

            doUpdate.setDeleted_date(new Date());
            sekolahRepo.save(doUpdate);
            return response.Sukses("Sukses Deleted");
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map update(Principal principal, SosialisasiSekolah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }
            if (req.getSekolah() == null) {
                return response.Error("Sekolah harus diisi.");
            }

            if (req.getSekolah().getId() == null) {
                return response.Error("Sekolah harus diisi.");
            }


            //chek penugasan
            if (null == req.getJenisSekolah()) {
                return response.Error("Jenis Sekolah harus diisi.");
            }
            SosialisasiSekolah doUpdate = sosialisasiSekolahRepo.getbyID(req.getId());
            if (null == doUpdate) {
                logger.error("Id tidak ditemukan.", req.getJenisSekolah().getId());
                return response.Error("Id tidak ditemukan.");
            }

            JenisSekolah jenisSekolah = jenisSekolahRepo.getbyID(req.getJenisSekolah().getId());
            if (null == jenisSekolah) {
                logger.error("Jenis Sekolah tidak ditemukan.", req.getJenisSekolah().getId());
                return response.Error("Jenis Sekolah tidak ditemukan.");
            }

            Sekolah sekolah = sekolahRepo.getbyID(req.getSekolah().getId());
            if (null == sekolah) {
                logger.error("Sekolah tidak ditemukan.", req.getJenisSekolah().getId());
                return response.Error("Sekolah tidak ditemukan.");
            }

            if (req.getKecamatan() != null) {
                Lookup kecamatan = lookupRepository.getbyID(req.getKecamatan().getId());
                if (null == kecamatan) {
                    return response.Error("Kecamatan " + req.getKecamatan().getId() + " tidak ditemukan.");
                }else {
                    doUpdate.setKecamatan(kecamatan);
                }

            }
            doUpdate.setCreatedUser(idUser);
            doUpdate.setEmail(req.getEmail());
            doUpdate.setJenisSekolah(jenisSekolah);
            doUpdate.setHp(req.getHp());
            doUpdate.setNama(req.getNama());
            doUpdate.setJurusan(req.getJurusan());
            doUpdate.setSekolah(req.getSekolah());
            return response.Sukses(sosialisasiSekolahRepo.save(doUpdate));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map delete(Principal principal, SosialisasiSekolah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            SosialisasiSekolah doUpdate = sosialisasiSekolahRepo.getbyID(req.getId());
            if (null == doUpdate) {
                logger.error("Id tidak ditemukan.", req.getJenisSekolah().getId());
                return response.Error("Id tidak ditemukan.");
            }

            doUpdate.setDeleted_date(new Date());
            sosialisasiSekolahRepo.save(doUpdate);
            return response.Sukses("Sukses Deleted");
        } catch (Exception e) {
            return response.Error(e);
        }
    }
}
