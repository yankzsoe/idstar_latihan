package com.synrgy.commit.service.piksi.impl;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.Kurikulum;
import com.synrgy.commit.model.piksi.KurikulumMataKuliah;
import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.model.piksi.MataKuliah;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.repository.piksi.KurikulumMatkulRepository;
import com.synrgy.commit.repository.piksi.KurikulumRepository;
import com.synrgy.commit.repository.piksi.LookupRepository;
import com.synrgy.commit.repository.piksi.MatkulRepository;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.KurikulumService;
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
public class KurikulumImpl implements KurikulumService {
    private static final Logger logger = LoggerFactory.getLogger(KurikulumImpl.class);
    @Autowired
    Response response;
    @Autowired
    KurikulumRepository kurikulumRepository;

    @Autowired
    KurikulumMatkulRepository kurikulumMatkulRepository;

    @Autowired
    LookupRepository lookupRepository;

    @Autowired
    MatkulRepository matkulRepository;

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
    public Map insert(Principal principal, Kurikulum req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getBobotMatkulWajib() == null) {
                return response.Error("Bobot Mata Kuliah Wajib diisi");
            }
            if (req.getNama() == null) {
                return response.Error("Nama Wajib diisi");
            }

            if (req.getBobotMatkulPilihan() == null) {
                return response.Error("Bobot Mata Kuliah Pilihan Wajib diisi");
            }

            if (req.getTglMulaiEfektif() == null) {
                return response.Error("Tanggal Berlaku Wajib diisi");
            }

            if (req.getProgramStudi() == null) {
                return response.Error("Program Studi Wajib diisi");
            }

            if (req.getProgramStudi().getId() == null) {
                return response.Error("Program Studi Wajib diisi");
            }

            Lookup chekProgramStudi = lookupRepository.getbyID(req.getProgramStudi().getId());
            if (chekProgramStudi == null) {
                return response.Error("Program Studi " + req.getProgramStudi().getId() + " tidak ditemukan.");
            } else {
                req.setProgramStudi(chekProgramStudi);
            }


            req.setCreatedUser(idUser);

//           setJumlahSks : SKS wajib + SKS Pilihan
            req.setJumlahSks((req.getBobotMatkulWajib() == null ? 0:req.getBobotMatkulWajib() )
                    + (req.getBobotMatkulPilihan()  == null ? 0:req.getBobotMatkulPilihan() ));
            return response.Sukses(kurikulumRepository.save(req));
        } catch (Exception e) {
            return response.Error(e);
        }
    }


    @Override
    public Map update(Principal principal, Kurikulum req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getBobotMatkulWajib() == null) {
                return response.Error("Bobot Mata Kuliah Wajib diisi");
            }
            if (req.getNama() == null) {
                return response.Error("Nama Wajib diisi");
            }

            if (req.getBobotMatkulPilihan() == null) {
                return response.Error("Bobot Mata Kuliah Pilihan Wajib diisi");
            }

            if (req.getTglMulaiEfektif() == null) {
                return response.Error("Tanggal Berlaku Wajib diisi");
            }

            if (req.getProgramStudi() == null) {
                return response.Error("Program Studi Wajib diisi");
            }

            if (req.getProgramStudi().getId() == null) {
                return response.Error("Program Studi Wajib diisi");
            }

            Kurikulum doUpdate = kurikulumRepository.getbyID(req.getId());
            if (doUpdate == null) {
                return response.Error("Kurikulum " + req.getId() + " tidak ditemukan.");
            }

            Lookup chekProgramStudi = lookupRepository.getbyID(req.getProgramStudi().getId());
            if (chekProgramStudi == null) {
                return response.Error("Program Studi " + req.getProgramStudi().getId() + " tidak ditemukan.");
            } else {
                doUpdate.setProgramStudi(chekProgramStudi);
            }


            doUpdate.setCreatedUser(idUser);
            doUpdate.setBobotMatkulPilihan(req.getBobotMatkulPilihan());
            doUpdate.setBobotMatkulWajib(req.getBobotMatkulWajib());
            doUpdate.setNama(req.getNama());
            doUpdate.setTglMulaiEfektif(req.getTglMulaiEfektif());
//           setJumlahSks : SKS wajib + SKS Pilihan
            doUpdate.setJumlahSks((doUpdate.getBobotMatkulWajib() == null ? 0 : doUpdate.getBobotMatkulWajib() )
                    + (doUpdate.getBobotMatkulPilihan()  == null ? 0:doUpdate.getBobotMatkulPilihan() ));
            return response.Sukses(kurikulumRepository.save(doUpdate));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map delete(Principal principal, Kurikulum req) {
        try {

            Kurikulum doUpdate = kurikulumRepository.getbyID(req.getId());
            if (null == doUpdate) {
                return response.Error("Id tidak ditemukan.");
            }

            doUpdate.setDeleted_date(new Date());
            kurikulumRepository.save(doUpdate);
            return response.Sukses("Sukses Deleted");
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map insertMataKuliah(Principal principal, KurikulumMataKuliah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getMatkul() == null) {
                return response.Error("Mata Kuliah Wajib diisi");
            }
            if (req.getMatkul().getId() == null) {
                return response.Error("Mata Kuliah Id Wajib diisi");
            }

            MataKuliah chekMataKuliah = matkulRepository.getbyID(req.getMatkul().getId());
            if (chekMataKuliah == null) {
                return response.Error("Mata Kuliah " + req.getMatkul().getId() + " tidak ditemukan.");
            } else {
                req.setMatkul(chekMataKuliah);
            }

            if (req.getKurikulum() == null) {
                return response.Error("Kurikulum Wajib diisi");
            }
            if (req.getKurikulum().getId() == null) {
                return response.Error("Kurikulum Id Wajib diisi");
            }

            Kurikulum chekKurikulum = kurikulumRepository.getbyID(req.getKurikulum().getId());
            if (chekKurikulum == null) {
                return response.Error("Kurikulum " + req.getKurikulum().getId() + " tidak ditemukan.");
            } else {
                req.setKurikulum(chekKurikulum);
            }
            req.setCreatedUser(idUser);
            return response.Sukses(kurikulumMatkulRepository.save(req));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map updateMataKuliah(Principal principal, KurikulumMataKuliah req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getMatkul() == null) {
                return response.Error("Mata Kuliah Wajib diisi");
            }
            if (req.getMatkul().getId() == null) {
                return response.Error("Mata Kuliah Id Wajib diisi");
            }

            KurikulumMataKuliah doUpdate = kurikulumMatkulRepository.getbyID(req.getId());
            if (doUpdate == null) {
                return response.Error("Kurikulum Mata Kuliah " + req.getId() + " tidak ditemukan.");
            }

            MataKuliah chekMataKuliah = matkulRepository.getbyID(req.getMatkul().getId());
            if (chekMataKuliah == null) {
                return response.Error("Mata Kuliah " + req.getMatkul().getId() + " tidak ditemukan.");
            } else {
                doUpdate.setMatkul(chekMataKuliah);
            }

            if (req.getKurikulum() == null) {
                return response.Error("Kurikulum Wajib diisi");
            }
            if (req.getKurikulum().getId() == null) {
                return response.Error("Kurikulum Id Wajib diisi");
            }

            Kurikulum chekKurikulum = kurikulumRepository.getbyID(req.getKurikulum().getId());
            if (chekKurikulum == null) {
                return response.Error("Kurikulum " + req.getKurikulum().getId() + " tidak ditemukan.");
            } else {
                doUpdate.setKurikulum(chekKurikulum);
            }
            doUpdate.setCreatedUser(idUser);
            doUpdate.setSemester(req.getSemester());
            doUpdate.setType(req.getType());
            return response.Sukses(kurikulumMatkulRepository.save(doUpdate));
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map deleteMataKuliah(Principal principal, KurikulumMataKuliah req) {
        try {

            KurikulumMataKuliah doUpdate = kurikulumMatkulRepository.getbyID(req.getId());
            if (null == doUpdate) {
                return response.Error("Id tidak ditemukan.");
            }

            doUpdate.setDeleted_date(new Date());
            kurikulumMatkulRepository.save(doUpdate);
            return response.Sukses("Sukses Deleted");
        } catch (Exception e) {
            return response.Error(e);
        }
    }
}
