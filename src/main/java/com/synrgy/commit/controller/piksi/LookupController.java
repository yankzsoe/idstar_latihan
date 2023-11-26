package com.synrgy.commit.controller.piksi;

import com.synrgy.commit.config.Config;
import com.synrgy.commit.model.piksi.Kurikulum;
import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.repository.piksi.LookupRepository;
import com.synrgy.commit.service.oauth.Oauth2UserDetailsService;
import com.synrgy.commit.service.piksi.LookUpService;
import com.synrgy.commit.util.Response;
import com.synrgy.commit.util.SimpleStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/v1/lookup")
public class LookupController {

    @Autowired
    LookupRepository lookupRepository;

    @Autowired
    LookUpService lookUpService;

    Config config = new Config();

    @Autowired
    private Oauth2UserDetailsService userDetailsService;

    @Autowired
    Response response;

    SimpleStringUtils simpleStringUtils = new SimpleStringUtils();


    @GetMapping("/list")
    public ResponseEntity<Map> list(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String nama,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype,
            Principal principal) {
//        User user = methodGlobal.getUserIdToken(principal, userDetailsService);

        if (orderby == null) {
            orderby = "nama";
        }
        if (ordertype == null) {
            ordertype = "asc";
        }

//        if (type == null) {
//            return new ResponseEntity<Map>(response.Error("Type wajib diisi"), new HttpHeaders(), HttpStatus.BAD_REQUEST);
//        }

        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<Lookup> list = null;

        if (type != null && nama != null && !type.isEmpty() && !nama.isEmpty()) {
            list = lookupRepository.getAllData("%" + nama + "%", type, show_data);
            return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
        } else if (type != null && !type.isEmpty()) {
            list = lookupRepository.getAllDataBytype(type, show_data);
            return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
        } else {
            list = lookupRepository.getAllData(   show_data);
            return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
        }
    }

    @GetMapping("/list-type")
    public ResponseEntity<Map> list(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
//            @RequestParam(required = false) String typeLike,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype,
            Principal principal) {
//        User user = methodGlobal.getUserIdToken(principal, userDetailsService);


        Pageable show_data = simpleStringUtils.getShortByType(orderby, ordertype, page, size);
        Page<String> list = null;
//        if(typeLike !=null && !StringUtils.isEmpty(typeLike)){
//            list = lookupRepository.getAllDataDistinctType("%" + typeLike + "%",show_data);
//            return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
//        }else{
            list = lookupRepository.getAllDataDistinctType(show_data);
            return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
//        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<Map> id(
            @PathVariable(value = "id") Long id,
            Principal principal) {

        Lookup chek = lookupRepository.getbyID(id);
        if (chek == null) {
            return new ResponseEntity<Map>(response.Error("Id tidak ditemuakan."), new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Map>(response.Sukses(chek), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/list/{idParent}")
    public ResponseEntity<Map> childByParent(
            @RequestParam() Integer page,
            @RequestParam(required = true) Integer size,
            @RequestParam(required = false) String orderby,
            @RequestParam(required = false) String ordertype,
            @PathVariable(value = "idParent") Long idParent,
            @RequestParam(required = false) String nama,
            Principal principal) {
        if (orderby == null) {
            orderby = "nama";
        }
        if (ordertype == null) {
            ordertype = "asc";
        }

        if (idParent == null) {
            return new ResponseEntity<Map>(response.Error("idParent wajib diisi."), new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }


        Pageable show_data = simpleStringUtils.getShort(orderby, ordertype, page, size);
        Page<Lookup> list = null;
        if (idParent != null && nama != null && !nama.isEmpty()) {
            list = lookupRepository.getAllChildByParentLikeNama("%" + nama + "%", idParent, show_data);
        } else {
            list = lookupRepository.getAllChildByParent(idParent, show_data);
        }

        return new ResponseEntity<Map>(response.Sukses(list), new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping(value = {"/save", "/save/"})
    public ResponseEntity<Map> save(@RequestBody() Lookup req,
                                    Principal principal) {
        Map map = lookUpService.insert(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @PutMapping(value = {"/update", "/update/"})
    public ResponseEntity<Map> update(@RequestBody() Lookup req,
                                      Principal principal) {
        Map map = lookUpService.update(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete", "/delete/"})
    public ResponseEntity<Map> delete(@RequestBody() Lookup req,
                                      Principal principal) {
        Map map = lookUpService.delete(principal, req);
        return new ResponseEntity<Map>(map, HttpStatus.OK);
    }
}