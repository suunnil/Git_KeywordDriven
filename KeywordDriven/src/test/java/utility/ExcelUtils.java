package utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import config.Constants;
import executionEngine.DriverScriptTest;

public class ExcelUtils {
	
	private static XSSFWorkbook ExcelWBook;
	private static XSSFSheet ExcelWSheet;
    private static XSSFCell Cell;
    public static XSSFRow Row;

//This method is to set the File path and to open the Excel file
//Pass Excel Path and SheetName as Arguments to this method
public static void setExcelFile(String Path) throws Exception {
	try{
		//Create an object of FileInputStream class to read excel file
		FileInputStream ExcelFile = new FileInputStream(Path);
        //Create an object of XSSFWorkbook class to load workbook
		ExcelWBook = new XSSFWorkbook(ExcelFile);
        //Remove below sheet name argument as we have two sheet names, one is from DataEgnine, another from test cases. Modify all below functions to add an extra argument of sheet name 
        //ExcelWSheet = ExcelWBook.getSheet(SheetName);
	}catch(Exception e){
		Log.error("Class Utils | Method setExcelFile | Exception desc : "+e.getMessage());
		DriverScriptTest.bResult = false;
	}
        
}

//This method is to read the test data from the Excel cell
//In this we are passing parameters/arguments as Row Num and Col Num
public static String getCellData(int RowNum, int ColNum, String SheetName) throws Exception{
	ExcelWSheet = ExcelWBook.getSheet(SheetName);
	try{
		Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
	      String CellData = Cell.getStringCellValue();
	      return CellData;	
	}catch (Exception e){
		Log.error("Class Utils | Method getCellData | Exception desc : "+e.getMessage());
		DriverScriptTest.bResult = false;
		return"";
	}
	
}

//public static int getColumnCount(String sheetName){
//	ExcelWSheet = ExcelWBook.getSheet(sheetName);
//	XSSFRow row = ExcelWSheet.getRow(0);
//	int colCount=row.getLastCellNum(); 
//	return colCount;
//}

//This method is to get the row count used of the excel sheet
public static int getRowCount(String sheetName){
	int iNumber=0;
	try{
		ExcelWSheet = ExcelWBook.getSheet(sheetName);
		iNumber=ExcelWSheet.getLastRowNum()+1;
	}catch(Exception e){
		Log.error("Class Utils | Method getRowCount | Exception desc : "+e.getMessage());
		DriverScriptTest.bResult = false;
	}
	return iNumber;
}

//This method is to get the Row number of the test case
//This methods takes three arguments(Test Case name , Column Number & Sheet name)
public static int getRowContains(String sTestCaseName, int ColNum, String SheetName) throws Exception{
	int iRowNum=0;
	try{
		//ExcelWSheet=ExcelWBook.getSheet(SheetName);
		int rowCount = ExcelUtils.getRowCount(SheetName);
		for (; iRowNum<rowCount; iRowNum++){
			if (ExcelUtils.getCellData(iRowNum, ColNum, SheetName).equalsIgnoreCase(sTestCaseName)){
				break;
			}
		}
	
	}catch (Exception e){
		Log.error("Class Utils | Method getRowContains | Exception desc : "+e.getMessage());
		DriverScriptTest.bResult = false;
	}
	
	return iRowNum;
}


//This method is to get the count of the test steps of test case
//This method takes three arguments (Sheet name, Test Case Id & Test case row number)
public static int getTestStepsCount(String SheetName, String sTestCaseID, int iTestCaseStart) throws Exception{
	try{
		for(int i=iTestCaseStart;i<=ExcelUtils.getRowCount(SheetName);i++){
			if(!sTestCaseID.equals(ExcelUtils.getCellData(i, Constants.Col_TestCaseID, SheetName))){
				int number = i-1;
				return number;			
		}
	}
		ExcelWSheet = ExcelWBook.getSheet(SheetName);
		int number=ExcelWSheet.getLastRowNum()+1;
		return number;
	} catch (Exception e){
		Log.error("Class Utils | Method getRowContains | Exception desc : "+e.getMessage());
		DriverScriptTest.bResult = false;
		return 0;
	}
	
}

//This method is use to write value in the excel sheet
//This method accepts four arguments (Result, Row Number, Column Number & Sheet Name)
@SuppressWarnings("static-access")
public static void setCellData(String Result,  int RowNum, int ColNum, String SheetName) throws Exception    {
	   try{

		   ExcelWSheet = ExcelWBook.getSheet(SheetName);
		   Row=ExcelWSheet.getRow(RowNum);
		   Cell = Row.getCell(ColNum, Row.RETURN_BLANK_AS_NULL);
		   if (Cell == null) {
			   Cell = Row.createCell(ColNum);
			   Cell.setCellValue(Result);
		   } else {
				Cell.setCellValue(Result);
				}
			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(Constants.Path_TestDataPolicy);
			ExcelWBook.write(fileOut);
			//fileOut.flush();
			fileOut.close();
			ExcelWBook = new XSSFWorkbook(new FileInputStream(Constants.Path_TestDataPolicy));
		 }catch(Exception e){
			DriverScriptTest.bResult = false;
			}
		}


}