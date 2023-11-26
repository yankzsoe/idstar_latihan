package com.synrgy.commit.report;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.synrgy.commit.model.piksi.SosialisasiSekolah;
import com.synrgy.commit.repository.piksi.SosialisasiSekolahRepo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class SosialisasiSekolahExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    @Value("${app.uploadto.cdn}")//FILE_SHOW_RUL
    private String UPLOADED_FOLDER;

    @Value("${BASEURLSHOW}")//FILE_SHOW_RUL
    private String BASEURLSHOW;


//    private final Path fileStorageLocation;




    @Autowired
    public SosialisasiSekolahRepo sosialisasiSekolahRepo;

//    @Autowired
    public SosialisasiSekolahExcelExporter(String uploaded_folder,String  baseUrl) {
        UPLOADED_FOLDER = uploaded_folder;
        BASEURLSHOW = baseUrl;
        workbook = new XSSFWorkbook();

    }


    private void writeHeaderLine() {
        sheet = workbook.createSheet("Sosialisasi Sekolah");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "No.", style);
        createCell(row, 1, "Nama", style);
        createCell(row, 2, "HP", style);
        createCell(row, 3, "Email", style);
        createCell(row, 4, "Nama Sekolah", style);
        createCell(row, 5, "Jenis Sekolah", style);
        createCell(row, 6, "Kecamatan", style);
        createCell(row, 7, "Wilayah", style);
        createCell(row, 8, "Jurusan", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines( List<SosialisasiSekolah> list) {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
int no=1;
        for (int i=0; i<list.size(); i++) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, no++, style);
            createCell(row, columnCount++, (list.get(i).getNama() == null ? "-" :list.get(i).getNama() ), style);
            createCell(row, columnCount++,(list.get(i).getHp() == null ? "-" :list.get(i).getHp() ), style);
            createCell(row, columnCount++,(list.get(i).getEmail() == null ? "-" :list.get(i).getEmail() ), style);
            createCell(row, columnCount++,(list.get(i).getSekolah() == null ? "-" :
                    (list.get(i).getSekolah().getNama()==null ? "-":list.get(i).getSekolah().getNama() ) ), style);
            createCell(row, columnCount++,(list.get(i).getJenisSekolah() == null ? "-" :
                    (list.get(i).getJenisSekolah().getNama()==null ? "-":list.get(i).getJenisSekolah().getNama() ) ), style);
            createCell(row, columnCount++,(list.get(i).getSekolah() == null ? "-" :
                    (list.get(i).getSekolah().getWilayah()==null ? "-":list.get(i).getSekolah().getWilayah() ) ), style);
            createCell(row, columnCount++,(list.get(i).getKecamatan() == null ? "-" :
                    (list.get(i).getKecamatan().getNama()==null ? "-":list.get(i).getKecamatan().getNama() ) ), style);
            createCell(row, columnCount++,(list.get(i).getJurusan() == null ? "-" :list.get(i).getJurusan() ), style);

        }
    }

    public Map export(List<SosialisasiSekolah> list) throws IOException {
        Map map  = new HashMap();
        try {
            writeHeaderLine();
            writeDataLines(list);
            File file = new File(UPLOADED_FOLDER+"sosialisasi_sekolah.xlsx");
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            workbook.close();

            out.close();

            map.put("url",BASEURLSHOW+"/showFile/sosialisasi_sekolah.xlsx");
            return map;
        }catch ( Exception e){
            map.put("eror",e);
            return map;

        }


    }
}