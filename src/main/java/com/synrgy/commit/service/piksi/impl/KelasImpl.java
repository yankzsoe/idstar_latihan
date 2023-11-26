package com.synrgy.commit.service.piksi.impl;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.dao.request.RegisterModel;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Dosen;
import com.synrgy.commit.model.piksi.Kelas;
import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.model.piksi.MataKuliah;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.repository.piksi.DosenRepository;
import com.synrgy.commit.repository.piksi.KelasRepository;
import com.synrgy.commit.repository.piksi.LookupRepository;
import com.synrgy.commit.repository.piksi.MatkulRepository;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.DosenService;
import com.synrgy.commit.service.piksi.KelasService;
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
public class KelasImpl implements KelasService {
    private static final Logger logger = LoggerFactory.getLogger(MatkulImpl.class);
    @Autowired
    Response response;
    @Autowired
    MatkulRepository matkulRepository;

    @Autowired
    KelasRepository kelasRepository;

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
    public Map insert(Principal principal, Kelas req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getProgramStudi() == null) {
                return response.Error("Program Studi diisi");
            }

            if (req.getProgramStudi().getId() == null) {
                return response.Error("Program Studi diisi");
            }

            if (req.getSemester() == null) {
                return response.Error("Semester diisi");
            }

            if (req.getSemester().getId() == null) {
                return response.Error("Semester diisi");
            }

            if (req.getMataKuliah() == null) {
                return response.Error("Mata Kuliah diisi");
            }

            if (req.getMataKuliah().getId() == null) {
                return response.Error("Mata Kuliah diisi");
            }

            if (req.getNama() == null) {
                return response.Error("Nama Kelas diisi");
            }

            Lookup chekProgramStudi = lookupRepository.getbyID(req.getProgramStudi().getId());
            if (chekProgramStudi == null) {
                return response.Error("Program Studi " + req.getProgramStudi().getId() + " tidak ditemukan.");
            } else {
                req.setProgramStudi(chekProgramStudi);
            }

            Lookup chekSemester = lookupRepository.getbyID(req.getSemester().getId());
            if (chekSemester == null) {
                return response.Error("Semester " + req.getSemester().getId() + " tidak ditemukan.");
            } else {
                req.setSemester(chekSemester);
            }

            MataKuliah chekMatkul = matkulRepository.getbyID(req.getMataKuliah().getId());
            if (chekMatkul == null) {
                return response.Error("Mata Kuliah " + req.getMataKuliah().getId() + " tidak ditemukan.");
            } else {
                req.setMataKuliah(chekMatkul);
            }

            if (req.getLingkup() != null) {
                Lookup chek = lookupRepository.getbyID(req.getLingkup().getId());
                if (chek == null) {
                    return response.Error("Lingkup Kelas " + req.getLingkup().getId() + " tidak ditemukan.");
                } else {
                    req.setLingkup(chek);
                }
            }

            if (req.getMode() != null) {
                Lookup chek = lookupRepository.getbyID(req.getMode().getId());
                if (chek == null) {
                    return response.Error("Mode Kuliah " + req.getMode().getId() + " tidak ditemukan.");
                } else {
                    req.setMode(chek);
                }
            }
            req.setCreatedUser(idUser);


            return response.Sukses(kelasRepository.save(req));
        } catch (Exception e) {
            return response.Error(e);
        }
    }


    @Override
    public Map update(Principal principal, Kelas req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getProgramStudi() == null) {
                return response.Error("Program Studi diisi");
            }

            if (req.getProgramStudi().getId() == null) {
                return response.Error("Program Studi diisi");
            }

            if (req.getSemester() == null) {
                return response.Error("Semester diisi");
            }

            if (req.getSemester().getId() == null) {
                return response.Error("Semester diisi");
            }

            if (req.getMataKuliah() == null) {
                return response.Error("Mata Kuliah diisi");
            }

            if (req.getMataKuliah().getId() == null) {
                return response.Error("Mata Kuliah diisi");
            }

            if (req.getNama() == null) {
                return response.Error("Nama Kelas diisi");
            }

            Kelas doUpdate = kelasRepository.getbyID(req.getId());
            if (doUpdate == null) {
                return response.Error("Kelas Id tidak ditemukan.");
            }

            Lookup chekProgramStudi = lookupRepository.getbyID(req.getProgramStudi().getId());
            if (chekProgramStudi == null) {
                return response.Error("Program Studi " + req.getProgramStudi().getId() + " tidak ditemukan.");
            } else {
                doUpdate.setProgramStudi(chekProgramStudi);
            }

            Lookup chekSemester = lookupRepository.getbyID(req.getSemester().getId());
            if (chekSemester == null) {
                return response.Error("Semester " + req.getSemester().getId() + " tidak ditemukan.");
            } else {
                doUpdate.setSemester(chekSemester);
            }

            MataKuliah chekMatkul = matkulRepository.getbyID(req.getMataKuliah().getId());
            if (chekMatkul == null) {
                return response.Error("Mata Kuliah " + req.getMataKuliah().getId() + " tidak ditemukan.");
            } else {
                doUpdate.setMataKuliah(chekMatkul);
            }

            if (req.getLingkup() != null) {
                Lookup chek = lookupRepository.getbyID(req.getLingkup().getId());
                if (chek == null) {
                    return response.Error("Lingkup Kelas " + req.getLingkup().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setLingkup(chek);
                }
            }

            if (req.getMode() != null) {
                Lookup chek = lookupRepository.getbyID(req.getMode().getId());
                if (chek == null) {
                    return response.Error("Mode Kuliah " + req.getMode().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setMode(chek);
                }
            }
            doUpdate.setCreatedUser(idUser);
            doUpdate.setNama(req.getNama());
            doUpdate.setTglAkhirEfektif(req.getTglAkhirEfektif());
            doUpdate.setTglMulaiEfektif(req.getTglMulaiEfektif());
            return response.Sukses(kelasRepository.save(doUpdate));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map delete(Principal principal, Kelas req) {
        try {

            Kelas doUpdate = kelasRepository.getbyID(req.getId());
            if (null == doUpdate) {
                return response.Error("Id tidak ditemukan.");
            }

            doUpdate.setDeleted_date(new Date());
            kelasRepository.save(doUpdate);
            return response.Sukses("Sukses Deleted");
        } catch (Exception e) {
            return response.Error(e);
        }
    }
}
