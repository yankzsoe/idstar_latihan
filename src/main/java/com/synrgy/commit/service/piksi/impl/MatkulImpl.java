package com.synrgy.commit.service.piksi.impl;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.dao.request.RegisterModel;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.model.piksi.MataKuliah;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.repository.piksi.DosenRepository;
import com.synrgy.commit.repository.piksi.LookupRepository;
import com.synrgy.commit.repository.piksi.MatkulRepository;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.DosenService;
import com.synrgy.commit.service.piksi.MatkulService;
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
public class MatkulImpl implements MatkulService {
    private static final Logger logger = LoggerFactory.getLogger(MatkulImpl.class);
    @Autowired
    Response response;
    @Autowired
    MatkulRepository matkulRepository;

    @Autowired
    LookupRepository lookupRepository;

    @Autowired
    public UserService userService;


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
    public Map insert(Principal principal, MataKuliah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getKode() == null) {
                return response.Error("Kode Wajib diisi");
            }

            if (req.getNama() == null) {
                return response.Error("Nama Wajib diisi");
            }

            MataKuliah chekKode = matkulRepository.findFirstByKode(req.getKode());
            if (chekKode != null) {
                return response.Error("Kode "+req.getKode()+ " sudah terdaftar.");
            }

            if (req.getProgramStudi() != null) {
                Lookup chekProgramStudi = lookupRepository.getbyID(req.getProgramStudi().getId());
                if (chekProgramStudi == null) {
                    return response.Error("Program Studi " + req.getProgramStudi().getId() + " tidak ditemukan.");
                } else {
                    req.setProgramStudi(chekProgramStudi);
                }
            }

            if (req.getJenisMatkul() != null) {
                Lookup chek = lookupRepository.getbyID(req.getJenisMatkul().getId());
                if (chek == null) {
                    return response.Error("Jenis Mata Kuliah " + req.getJenisMatkul().getId() + " tidak ditemukan.");
                } else {
                    req.setJenisMatkul(chek);
                }
            }
            req.setCreatedUser(idUser);

//           bobotMatkul; //( sks Tatap Muka + sks Praktikum + sks Praktek Lapangan + sks Simulasi )
            req.setBobotMatkul((req.getBobotTatapMuka() == null ? 0:req.getBobotTatapMuka() )
                    + (req.getBobotPratikum()  == null ? 0:req.getBobotPratikum() )
                    + (req.getBobotPraktekLapangan() == null ? 0:req.getBobotPraktekLapangan() )
                    + ( req.getBobotSimulasi() == null ? 0: req.getBobotSimulasi()));

            return response.Sukses(matkulRepository.save(req));
        } catch (Exception e) {
            return response.Error(e);
        }
    }


    @Override
    public Map update(Principal principal, MataKuliah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getKode() == null) {
                return response.Error("Kode Wajib diisi");
            }

            if (req.getId() == null) {
                return response.Error("Id Wajib diisi");
            }

            if (req.getNama() == null) {
                return response.Error("Nama Wajib diisi");
            }

            MataKuliah chekKode = matkulRepository.findFirstByKodeNotId(req.getKode(), req.getId());
            if (chekKode != null) {
                return response.Error("Kode "+req.getKode()+ " sudah terdaftar.");
            }

            MataKuliah doUpdate = matkulRepository.getbyID(req.getId());
            if (doUpdate == null) {
                return response.Error("Mata Kuliah "+req.getNama()+ " tidak ditemukan.");
            }

            if (req.getProgramStudi() != null) {
                Lookup chekProgramStudi = lookupRepository.getbyID(req.getProgramStudi().getId());
                if (chekProgramStudi == null) {
                    return response.Error("Program Studi " + req.getProgramStudi().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setProgramStudi(chekProgramStudi);
                }
            }

            if (req.getJenisMatkul() != null) {
                Lookup chek = lookupRepository.getbyID(req.getJenisMatkul().getId());
                if (chek == null) {
                    return response.Error("Jenis Mata Kuliah " + req.getJenisMatkul().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setJenisMatkul(chek);
                }
            }

            req.setCreatedUser(idUser);
            //           bobotMatkul; //( sks Tatap Muka + sks Praktikum + sks Praktek Lapangan + sks Simulasi )
            req.setBobotMatkul((req.getBobotTatapMuka() == null ? 0:req.getBobotTatapMuka() )
                    + (req.getBobotPratikum()  == null ? 0:req.getBobotPratikum() )
                    + (req.getBobotPraktekLapangan() == null ? 0:req.getBobotPraktekLapangan() )
                    + ( req.getBobotSimulasi() == null ? 0: req.getBobotSimulasi()));
            return response.Sukses(matkulRepository.save(req));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map delete(Principal principal, MataKuliah req) {
        try {

            MataKuliah doUpdate = matkulRepository.getbyID(req.getId());
            if (null == doUpdate) {
                return response.Error("Id tidak ditemukan.");
            }

            doUpdate.setDeleted_date(new Date());
            matkulRepository.save(doUpdate);
            return response.Sukses("Sukses Deleted");
        } catch (Exception e) {
            return response.Error(e);
        }
    }
}
