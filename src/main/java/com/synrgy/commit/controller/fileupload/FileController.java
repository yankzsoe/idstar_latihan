package com.synrgy.commit.controller.fileupload;

import com.synrgy.commit.dao.request.EmailSenderModel;
import com.synrgy.commit.model.piksi.JenisSekolah;
import com.synrgy.commit.model.piksi.Lookup;
import com.synrgy.commit.model.piksi.Sekolah;
import com.synrgy.commit.model.piksi.SosialisasiSekolah;
import com.synrgy.commit.repository.piksi.JenisSekolahRepo;
import com.synrgy.commit.repository.piksi.LookupRepository;
import com.synrgy.commit.repository.piksi.SekolahRepo;
import com.synrgy.commit.repository.piksi.SosialisasiSekolahRepo;
import com.synrgy.commit.service.email.EmailSender;
import com.synrgy.commit.service.piksi.SosialisasiSekolahService;
import com.synrgy.commit.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
//@EnableCaching
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    public EmailSender emailSender;

    @Value("${app.uploadto.cdn}")//FILE_SHOW_RUL
    private String UPLOADED_FOLDER;

    @Value("${app.uploadto.cdntmp}")//FILE_SHOW_RUL
    private String UPLOADED_FOLDER_TEMPLATE;

    @Autowired
    Response response;

    @Autowired
    public LookupRepository lookupRepository;
    @Autowired
    public JenisSekolahRepo jenisSekolahRepo;
    @Autowired
    public SosialisasiSekolahRepo sosialisasiSekolahRepo;
    @Autowired
    public SekolahRepo sekolahRepo;


    @Autowired
    public SosialisasiSekolahService sosialisasiSekolahService;

    @Value("${BASEURLSHOW_REPORT_TEMPLATE}")//FILE_SHOW_RUL
    private String BASEURLSHOW_REPORT_TEMPLATE;


    @Autowired
    private FileStorageService fileStorageService;


    @RequestMapping(value = "/v1/upload", method = RequestMethod.POST, consumes = {"multipart/form-data", "application/json"})
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMyyyyhhmmss");
        String strDate = formatter.format(date);
        String nameFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (nameFormat.isEmpty()) {
            nameFormat = ".png";
        }
        String fileName = UPLOADED_FOLDER + strDate + nameFormat;


        String fileNameforDOwnload = strDate + nameFormat;
        Path TO = Paths.get(fileName);


//        //validasi hanya boleh PNG
//        if(!file.getContentType().equals("image/png")){
//            return null;// eror
//        }

        try {
            Files.copy(file.getInputStream(), TO); // pengolahan upload disini :
        } catch (Exception e) {
            e.printStackTrace();
            return new UploadFileResponse(fileName, null, file.getContentType(), file.getSize(), e.getMessage());
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v1/showFile/")
                .path(fileNameforDOwnload)
                .toUriString();

        return new UploadFileResponse(fileNameforDOwnload, fileDownloadUri, file.getContentType(), file.getSize(), "false");
    }

//    public String getPAth(String type) {
//        try {
//            if (type.toLowerCase().equals("product")) {
//                File theDir = new File(UPLOADED_FOLDER_PRODUCT);
//                if (!theDir.exists()) {
//                    theDir.mkdirs();
//                    return  UPLOADED_FOLDER_PRODUCT;
//                }
//                return UPLOADED_FOLDER;
//            }
//
//        } catch (Exception e) {
//            logger.info("Eror create folder" + e);
//        }
//        return UPLOADED_FOLDER;
//    }

    @GetMapping("v1/showFile/{fileName:.+}")
    public ResponseEntity<Resource> showFile(@PathVariable String fileName, HttpServletRequest request) { // Load file as Resource : step 1 load path lokasi name file
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        // Try to determine file's content type
        String contentType = null;
        try {
            // dapatan URL download
//            System.out.println("resource.getFile().getAbsolutePath" + resource.getFile().getAbsolutePath());
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";// type .json
        }
//        System.out.println("filename=2=" + HttpHeaders.CONTENT_DISPOSITION);
//        System.out.println("filename=3=" + resource.getFilename());
//        System.out.println("filename=3=" + resource);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @PostMapping("v1/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        return Arrays.asList(files)
                .stream()
                .map(file -> {
                    try {
                        // step 1 : call method uplaod
                        return uploadFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }


    private File multipartToFile(MultipartFile upload, String routeName) {
        String base = "";


        logger.info(String.format("Trying upload file: %s", upload.getOriginalFilename()));

        File file = new File(base + upload.getOriginalFilename());

        try {
            logger.info(String.format("Saving uploaded file to: '%s'", file.getAbsolutePath()));
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(upload.getBytes());
            fos.close();
        } catch (IOException e) {
            logger.error(String.format("Error: POST|UPLOAD %s", routeName), e);
        }

        return file;
    }

    private File multipartToFile(MultipartFile upload) {
        return multipartToFile(upload, UPLOADED_FOLDER);
    }


    //upload csv sekolah
    @RequestMapping(value = "/v1/upload/sos-sekolah", method = RequestMethod.POST, consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<Map> uploadFileSosialisasiSekolah(@RequestParam("file") MultipartFile file) throws IOException {

// langkah 1: simpan file
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMyyyyhhmmss");
        String strDate = formatter.format(date);
        String nameFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (nameFormat.isEmpty()) {
            nameFormat = ".png";
        }

        if (!file.getContentType().equals("text/csv")) {
            return new ResponseEntity<Map>(response.Error("Format File harus CSV. Format file yang diupload saat ini adalah " + file.getContentType()), HttpStatus.OK);
        }

        String fileName = UPLOADED_FOLDER_TEMPLATE + strDate + nameFormat;


        String fileNameforDOwnload = strDate + nameFormat;
        try {
            Path TO = Paths.get(fileName);
            try {
                Files.copy(file.getInputStream(), TO); // pengolahan upload disini :
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Map>(response.Sukses(new UploadFileResponse(fileName, null, file.getContentType(), file.getSize(), e.getMessage())), HttpStatus.OK);

            }

            // langkah 2 : read csv
            String line = "";
            String splitBy = ",";
            String lineHeader = "";
            String splitByHeader = ",";
            BufferedReader brChekHeader = new BufferedReader(new FileReader(fileName));
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            try {
                //parsing a CSV file into BufferedReader class constructor

                //chek format
                while ((lineHeader = brChekHeader.readLine()) != null)
                //returns a Boolean value
                {
                    String[] employee = lineHeader.split(splitByHeader);
                    //use comma as separator
//                    No.	Nama	HP	Email	Nama Sekolah	Jenis Sekolah	Kecamatan	Wilayah	Jurusan
                    //chek header sesuai apa ga
                    if (!employee[0].toString().toLowerCase().replace("\\s+", "").equals("no.") ||
                            !employee[1].toString().toLowerCase().replace("\\s+", "").equals("nama") ||
                            !employee[2].toString().toLowerCase().replace("\\s+", "").equals("hp") ||
                            !employee[3].toString().toLowerCase().replace("\\s+", "").equals("email") ||
                            !employee[4].toString().toLowerCase().replace("\\s+", "").equals("nama sekolah") ||
                            !employee[5].toString().toLowerCase().replace("\\s+", "").equals("jenis sekolah") ||
                            !employee[6].toString().toLowerCase().replace("\\s+", "").equals("kecamatan") ||
                            !employee[7].toString().toLowerCase().replace("\\s+", "").equals("wilayah") ||
                            !employee[8].toString().toLowerCase().replace("\\s+", "").equals("jurusan")) {
                        brChekHeader.close();
                        deletedFile(fileNameforDOwnload);
                        return new ResponseEntity<Map>(response.Error("Format Header tidak sesuai. Silahkan Download Template"), HttpStatus.OK);
                    }


                    break;
                }
                brChekHeader.close();

                List<SosialisasiSekolah> listSosSekolah = new ArrayList<>();
                while ((line = br.readLine()) != null)
                //returns a Boolean value
                {
                    String[] employee = line.split(splitBy);
                    //use comma as separator
//                    No.	Nama	HP	Email	Nama Sekolah	Jenis Sekolah	Kecamatan	Wilayah	Jurusan
//                    System.out.println("Emp[No.=" + employee[0] +
//                            ", Nama=" + employee[1] +
//                            ", HP=" + employee[2] +
//                            ", Email= " + employee[3] +
//                            ", Nama Sekolah= " + employee[4] +
//                            ", Jenis Sekolah= " + employee[5] +
//                            ", Kecamatan= " + employee[6] +
//                            ", Wilayah= " + employee[7] +
//                            ", Jurusan= " + employee[8] + "]");
/*
noted:
1. Data dibawah ini digunakan untuk pengisian pilihan data pada kolumn sebelah, silahkan pilih salah  satu data saja.
2. Posisi kolumn dan nama header jangan diubah-ubah"
3. Kolum :Nama Sekolah : Diambil dari list sekolah. Copi paste kan nama sekolah sesuai database. Jika Belum ada, akan membuat nama sekolah baru didatabase.
4. Kolum :Jenis Sekolah isi dengan salah satu velue : SMA,SMK,MA atau Lain-lain
5. Kolum :Kecamatan isi dengan salah satu value : Cilegon Raya, Kota/Kab. Serang, Pandeglang/Lebak, Tangerang Raya atau Lain-lain
6. Kolum :HP usahakan dengan value string dengan memberikan kutip "" di cell csv.
 */
                    if (!employee[0].toString().toLowerCase().equals("no.")) {
                        String nama = employee[1].toString();
                        String hp = employee[2].toString();
                        String email = employee[3].toString().toLowerCase().replace("\\s+", "");
                        String namaSekolah = employee[4].toString();
                        String jenisSekolah = employee[5].toString().toLowerCase();
                        String kecamatan = employee[6].toString();
                        String wilayah = employee[7].toString();
                        String jurusan = employee[8].toString();

                        SosialisasiSekolah doSave = new SosialisasiSekolah();
//                    langkah 3 : save
                        if (!StringUtils.isEmpty(namaSekolah)) {
                            Sekolah sekolah = sekolahRepo.findLikeByNama(namaSekolah);
                            if (null == sekolah) {
                                Sekolah saveSekolah = new Sekolah();
                                saveSekolah.setNama(namaSekolah);
                                saveSekolah.setWilayah(wilayah);
                                Sekolah save = sekolahRepo.save(saveSekolah);
                                doSave.setSekolah(save);
                            } else {
                                doSave.setSekolah(sekolah);
                            }

                        }

                        if (!StringUtils.isEmpty(kecamatan)) {
                            Lookup chekKecamatan = lookupRepository.findByNamaAndType(kecamatan, "wilayah_sekolah");
                            if (null == chekKecamatan) {
                                Lookup chekKecamatanLainLain = lookupRepository.findByNamaAndType("lain-lain", "wilayah_sekolah");
                                doSave.setKecamatan(chekKecamatanLainLain);
                            } else {
                                doSave.setKecamatan(chekKecamatan);
                            }

                        }
                        if (!StringUtils.isEmpty(kecamatan)) {
                            JenisSekolah chekJenisSekolah = jenisSekolahRepo.findOneByNama(jenisSekolah);
                            if (null == chekJenisSekolah) {
                                JenisSekolah chekJenisSekolahLain2 = jenisSekolahRepo.findOneByNama("lain-lain");
                                doSave.setJenisSekolah(chekJenisSekolahLain2);
                            } else {
                                doSave.setJenisSekolah(chekJenisSekolah);
                            }
                        }

                        doSave.setNama(nama);
                        doSave.setHp(hp);
                        doSave.setEmail(email);
                        doSave.setJurusan(jurusan);
                        listSosSekolah.add(doSave);
                    }
                    sosialisasiSekolahRepo.saveAll(listSosSekolah);
                }

            } catch (IOException e) {
                br.close();
                deletedFile(fileNameforDOwnload);
                e.printStackTrace();
            } finally {

                br.close();

            }


            return new ResponseEntity<Map>(response.Sukses("Sukses"), HttpStatus.OK);
        } catch (Exception e) {
            deletedFile(fileNameforDOwnload);
            return new ResponseEntity<Map>(response.Error(e), HttpStatus.OK);

        } finally {
            //langkah 4 : deletet file
            deletedFile(fileNameforDOwnload);
        }
    }

    public void deletedFile(String fileNameforDOwnload) {
        File myObj = new File(UPLOADED_FOLDER_TEMPLATE + fileNameforDOwnload);
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    @RequestMapping(value = "/v1/upload/timesheet", method = RequestMethod.POST, consumes = {"multipart/form-data", "application/json"})
    public ResponseEntity<Map> uploadTimesheet(@RequestParam("file") MultipartFile file) throws IOException {

// langkah 1: simpan file
        List<String> listDataEmail = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("ddMyyyyhhmmss");
        String strDate = formatter.format(date);
        String nameFormat = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        if (nameFormat.isEmpty()) {
            nameFormat = ".png";
        }

        if (!file.getContentType().equals("text/csv")) {
            return new ResponseEntity<Map>(response.Error("Format File harus CSV. Format file yang diupload saat ini adalah " + file.getContentType()), HttpStatus.OK);
        }

        String fileName = UPLOADED_FOLDER_TEMPLATE + strDate + nameFormat;


        String fileNameforDOwnload = strDate + nameFormat;
        try {
            Path TO = Paths.get(fileName);
            try {
                Files.copy(file.getInputStream(), TO); // pengolahan upload disini :
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<Map>(response.Sukses(new UploadFileResponse(fileName, null, file.getContentType(), file.getSize(), e.getMessage())), HttpStatus.OK);

            }

            // langkah 2 : read csv
            String line = "";
            String splitBy = ",";
            String lineHeader = "";
            String splitByHeader = ",";
            BufferedReader brChekHeader = new BufferedReader(new FileReader(fileName));
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            try {
                //parsing a CSV file into BufferedReader class constructor
                //chek format
                while ((lineHeader = brChekHeader.readLine()) != null)
                //returns a Boolean value
                {
                    String[] employee = lineHeader.split(splitByHeader);
                    //use comma as separator
//                    No.	Nama	HP	Email	Nama Sekolah	Jenis Sekolah	Kecamatan	Wilayah	Jurusan
                    //chek header sesuai apa ga
                    if (!employee[0].toString().toLowerCase().replace("\\s+", "").equals("email")) {
                        brChekHeader.close();
                        deletedFile(fileNameforDOwnload);
                        return new ResponseEntity<Map>(response.Error("Format Header tidak sesuai. Silahkan Download Template"), HttpStatus.OK);
                    }


                    break;
                }
                brChekHeader.close();
                while ((line = br.readLine()) != null)
                //returns a Boolean value
                {
                    String[] employee = line.split(splitBy);

                    if (!employee[0].toString().toLowerCase().equals("email")) {
                        String email = employee[0].toString();

                    }

                }

            } catch (IOException e) {
                br.close();
                deletedFile(fileNameforDOwnload);
                e.printStackTrace();
            } finally {
                br.close();

            }


            return new ResponseEntity<Map>(response.Sukses(sendEmailPassword(listDataEmail)), HttpStatus.OK);
        } catch (Exception e) {
            deletedFile(fileNameforDOwnload);
            return new ResponseEntity<Map>(response.Error(e), HttpStatus.OK);

        } finally {
            deletedFile(fileNameforDOwnload);
        }
    }


    public Map sendEmailPassword(List<String> listEmail) {
        String message = "Thanks, please check your email";
        String subjek="Reminder-Send Timesheet";
        /*
        http://www.unit-conversion.info/texttools/text-to-html/
        https://www.base64encode.org/
         */
        String reqTemplateTimesheet = "PHA+dWppIGNvYmEgc2FqYTwvcD4=";


        byte[] decodedBytes = Base64.getDecoder().decode(reqTemplateTimesheet);
        String decodedString = new String(decodedBytes);

        String[] from = new String[listEmail.size()];
        from = listEmail.toArray(from);
        Map cehk = emailSender.sendAsyncManyEmail("rikialdipari@gmail.com", from, subjek, decodedString);


        return cehk;
    }


}
