package com.synrgy.commit.service.piksi.impl;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.controller.RegisterController;
import com.synrgy.commit.dao.request.RegisterModel;
import com.synrgy.commit.model.oauth.User;
import com.synrgy.commit.model.piksi.*;
import com.synrgy.commit.repository.oauth.UserRepository;
import com.synrgy.commit.repository.piksi.*;
import com.synrgy.commit.service.UserService;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.DosenService;
import com.synrgy.commit.service.piksi.SosialisasiSekolahService;
import com.synrgy.commit.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Date;
import java.util.Map;

//@Transactional : matikan khusus disini
@Service
public class DosenImpl implements DosenService {
    private static final Logger logger = LoggerFactory.getLogger(DosenImpl.class);
    @Autowired
    Response response;
    @Autowired
    DosenRepository dosenRepository;

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
    public Map insert(Principal principal, Dosen req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getUser().getUsername() == null) {
                return response.Error("Username Wajib diisi");
            }

            if (req.getPassword() == null) {
                return response.Error("Password Wajib diisi");
            }

            User chekUser = userRepository.findOneByUsername(req.getUser().getUsername());
            if (chekUser != null) {
                return response.Error("Username "+req.getUser().getUsername() + " sudah terdaftar.");
            }

            Dosen chekNidn = dosenRepository.findFirstByNidn(req.getNidn());
            if (chekNidn != null) {
                return response.Error("NIDN "+req.getNidn()+ " sudah terdaftar.");
            }

            Dosen chekNIK = dosenRepository.findFirstByNik(req.getNidn());
            if (chekNIK != null) {
                return response.Error("Nik "+req.getNik()+ " sudah terdaftar.");
            }

            if (req.getAgama() != null) {
                Lookup chekAgama = lookupRepository.getbyID(req.getAgama().getId());
                if (chekAgama == null) {
                    return response.Error("Agama " + req.getAgama().getId() + " tidak ditemukan.");
                } else {
                    req.setAgama(chekAgama);
                }
            }


            if (req.getNegara() != null) {
                Lookup chekNegara = lookupRepository.getbyID(req.getNegara().getId());
                if (chekNegara == null) {
                    return response.Error("Negara " + req.getNegara().getId() + " tidak ditemukan.");
                } else {
                    req.setNegara(chekNegara);
                }
            }

            if (req.getProvinsi() != null) {
                Lookup chek = lookupRepository.getbyID(req.getProvinsi().getId());
                if (chek == null) {
                    return response.Error("Provinsi " + req.getProvinsi().getId() + " tidak ditemukan.");
                } else {
                    req.setProvinsi(chek);
                }
            }

            if (req.getKabupaten() != null) {
                Lookup chek = lookupRepository.getbyID(req.getKabupaten().getId());
                if (chek == null) {
                    return response.Error("Kabupaten " + req.getKabupaten().getId() + " tidak ditemukan.");
                } else {
                    req.setKabupaten(chek);
                }
            }

            if (req.getKecamatan() != null) {
                Lookup chek = lookupRepository.getbyID(req.getKecamatan().getId());
                if (chek == null) {
                    return response.Error("Kecamatan " + req.getKecamatan().getId() + " tidak ditemukan.");
                } else {
                    req.setKecamatan(chek);
                }
            }

            if (req.getDesa() != null) {
                Lookup chek = lookupRepository.getbyID(req.getDesa().getId());
                if (chek == null) {
                    return response.Error("Desa " + req.getDesa().getId() + " tidak ditemukan.");
                } else {
                    req.setDesa(chek);
                }
            }

            req.setCreatedUser(idUser);


            //insert into user oauth
            RegisterModel register = new RegisterModel();
            register.setInterest("Backend,QA");
            register.setPassword(req.getPassword());
            register.setGender(req.getUser().getGender());
            register.setPhone_number(req.getUser().getPhone_number());
            register.setEmail(req.getUser().getUsername());
            register.setName(req.getUser().getFullname());
            register.setNamaIbuKandung(req.getUser().getNamaIbuKandung());
            register.setTempatLahir(req.getUser().getTempatLahir());
            register.setTanggalLahir(req.getUser().getTanggalLahir());
            Map responseData = userService.registerManual(register);
            if (!responseData.get("status").toString().equals("200")) {
                return (responseData);
            }
            Map data = (Map) responseData.get("data");
            Map data2 = (Map) data.get("data");
            Map user = (Map) data2.get("user");
//
            System.out.println("data=" + user);
            User updateUser = userRepository.getbyID(Long.parseLong(user.get("id").toString()));
//            updateUser.setDosen(saveDosen);
//            userRepository.save(updateUser);
//            updateUser.setDosen(null);
//            saveDosen.setUser(updateUser);
            req.setUser(updateUser);
            Dosen saveDosen = dosenRepository.save(req);
            return response.Sukses(saveDosen);
        } catch (Exception e) {
            return response.Error(e);
        }
    }


    @Override
    public Map update(Principal principal, Dosen req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }

            if (req.getId() == null) {
                return response.Error("Id Wajib diisi.");
            }

            Dosen chekNidn = dosenRepository.findFirstByNidnNotId(req.getNidn(),req.getId());
            if (chekNidn != null) {
                return response.Error("NIDN "+req.getNidn()+ " sudah terdaftar.");
            }

            Dosen chekNIK = dosenRepository.findFirstByNikNotId(req.getNik(),req.getId());
            if (chekNIK != null) {
                return response.Error("Nik "+req.getNik()+ " sudah terdaftar.");
            }

            Dosen doUpdate = dosenRepository.getbyID(req.getId());
            if (null == doUpdate) {
                return response.Error("Id tidak ditemukan.");
            }

            if (req.getAgama() != null) {
                Lookup chekAgama = lookupRepository.getbyID(req.getAgama().getId());
                if (chekAgama == null) {
                    return response.Error("Agama " + req.getAgama().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setAgama(chekAgama);
                }
            }


            if (req.getNegara() != null) {
                Lookup chekNegara = lookupRepository.getbyID(req.getNegara().getId());
                if (chekNegara == null) {
                    return response.Error("Negara " + req.getNegara().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setNegara(chekNegara);
                }
            }

            if (req.getProvinsi() != null) {
                Lookup chek = lookupRepository.getbyID(req.getProvinsi().getId());
                if (chek == null) {
                    return response.Error("Provinsi " + req.getProvinsi().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setProvinsi(chek);
                }
            }

            if (req.getKabupaten() != null) {
                Lookup chek = lookupRepository.getbyID(req.getKabupaten().getId());
                if (chek == null) {
                    return response.Error("Kabupaten " + req.getKabupaten().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setKabupaten(chek);
                }
            }

            if (req.getKecamatan() != null) {
                Lookup chek = lookupRepository.getbyID(req.getKecamatan().getId());
                if (chek == null) {
                    return response.Error("Kecamatan " + req.getKecamatan().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setKecamatan(chek);
                }
            }

            if (req.getDesa() != null) {
                Lookup chek = lookupRepository.getbyID(req.getDesa().getId());
                if (chek == null) {
                    return response.Error("Desa " + req.getDesa().getId() + " tidak ditemukan.");
                } else {
                    doUpdate.setDesa(chek);
                }
            }


            doUpdate.setCreatedUser(idUser);
            doUpdate.setNidn(req.getNidn());
            doUpdate.setPhoto(req.getPhoto());
            doUpdate.setNik(req.getNik());
            doUpdate.setAlamat(req.getAlamat());
            doUpdate.setRt(req.getRt());
            doUpdate.setRw(req.getRw());
            doUpdate.setDusun(req.getDusun());
            doUpdate.setKodePos(req.getKodePos());
            doUpdate.setTelpRumah(req.getTelpRumah());
            doUpdate.setNpwp(req.getNpwp());
            doUpdate.setNamaNpwp(req.getNamaNpwp());
            doUpdate.setSintaId(req.getSintaId());
            doUpdate.setStatus(req.getStatus());
            Dosen save = dosenRepository.save(doUpdate);


            User updateUser = userRepository.getbyID(save.getUser().getId());
            updateUser.setGender(req.getUser().getGender());
            updateUser.setPhone_number(req.getUser().getPhone_number());
            updateUser.setFullname(req.getUser().getFullname());
            updateUser.setFullname(req.getUser().getFullname());
            updateUser.setNamaIbuKandung(req.getUser().getNamaIbuKandung());
            updateUser.setTempatLahir(req.getUser().getTempatLahir());
            updateUser.setTanggalLahir(req.getUser().getTanggalLahir());
            userRepository.save(updateUser);
            return response.Sukses(save);
        } catch (Exception e) {
            return response.Error(e);
        }
    }

    @Override
    public Map delete(Principal principal, Dosen req) {
        try {
            User idUser = getUserIdToken(principal, userDetailsService);
            if (idUser == null) {
                return response.Error("User not found");
            }
            Dosen doUpdate = dosenRepository.getbyID(req.getId());
            if (null == doUpdate) {
                return response.Error("Id tidak ditemukan.");
            }

            doUpdate.setDeleted_date(new Date());
            dosenRepository.save(doUpdate);
            return response.Sukses("Sukses Deleted");
        } catch (Exception e) {
            return response.Error(e);
        }
    }
}
