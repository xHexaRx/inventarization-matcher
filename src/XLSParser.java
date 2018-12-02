import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class XLSParser {
	
	public String getExtension(File file) {
		String fname=file.getName();
		int i=fname.lastIndexOf('.');
		if(i>0) {
			return fname.substring(i+1);
		}
		else {
			return "";
		}
	}
	
	public ArrayList<String> parseSim(File file) throws FileNotFoundException, IOException {
		ArrayList<String> list=new ArrayList<String>();
		HSSFWorkbook book=new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheet=book.getSheetAt(0);
		String marker="";
		int n=0;
		while(!marker.contains("Характеристика")) {
			HSSFRow row=sheet.getRow(n);
			if(row.getCell(0)!=null) {
				marker=row.getCell(0).getStringCellValue();
			}
			n++;
		}
		n++;
		while(!marker.contains("Итого")) {
			HSSFRow row=sheet.getRow(n);
			marker=row.getCell(0).getStringCellValue();
			list.add(marker);
			n++;
		}
		list.remove("Итого");
		book.close();
		return list;
	}
}
