package com.arjay.exercise;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class Table{

	private int numRows;
	private int numCols;
  private List<List<Map<String, String>>> table;
	private List<Map<String, String>> row;
	private Map<String, String> cell;
	private Scanner read = new Scanner(System.in);
	public static final String filePath="fileTable.txt";
	
	public void checkIfFileExist() throws IOException {
		if(Files.exists(Paths.get(filePath))){
			readFromFile();
		}
		else{
			System.out.println("File not found. We will create a fileTable");
			createTable();
			printToFile();
		}
		System.out.println("Table from file : fileTable.txt");
		printTable();
	}

	public void searchTable(){
		System.out.print("Enter String or Character to search: ");
		Scanner read = new Scanner(System.in);
		String searchStr=read.next();
		int sumOccur=0;
		int iRow = 0;
		String KeyValue="";
		String KeyValueString="";
		for (List<Map<String, String>> row : table) {
			int iCol = 0;
			for (Map<String, String> cell : row) {
				for (String key : cell.keySet()) {
					if (key.contains(searchStr)){
						KeyValue="Key";
						KeyValueString=key;
						sumOccur+=occurence(KeyValue,KeyValueString,searchStr,iRow,iCol);
					}
                 	if(cell.get(key).contains(searchStr)){
						KeyValue="Value";
						KeyValueString=cell.get(key);
						sumOccur+=occurence(KeyValue,KeyValueString,searchStr,iRow,iCol);
					}
				}
				iCol++;
			}
			iRow++;
		}
		if(sumOccur==0) System.out.println(String.format("Could not find search string \"%s\"", searchStr));
		if(sumOccur>0) System.out.println(searchStr+" found "+sumOccur+" time/s");
    }

	public void editTable(){
		Scanner read = new Scanner(System.in);
		int rowEdit=checkRow(0, numRows-1);
		int colEdit=checkColumn(0, numCols-1);
		int keyOrValue=keyOrValue();
		String stringEdit="";
		do{
			System.out.println("Enter 3 Characters, No space , No Tab :");
			stringEdit=read.nextLine();
		}while(stringEdit.length()!=3||stringEdit.contains("\t")||stringEdit.contains(" "));
		int iRow = 0;
		for (List<Map<String, String>> row : table) {
            int iCol = 0;
            for (Map<String, String> cell : row){
                for (String key : cell.keySet()) {
                	if( iRow==rowEdit && iCol==colEdit ){
						if(keyOrValue==1){
							String value=cell.get(key);
							cell.remove(key);
							cell.put(stringEdit,value);
						}
						else{
                			String value =stringEdit;
							cell.put(key,stringEdit);
						}
					}
                }
				iCol++;
			}
			iRow++;
		}
	}

	public void addRow(){
		System.out.println("Enter rows to be added: ");
		int plusRow=checkRow(1,9);
		for (int iRow=0; iRow<plusRow; iRow++) {
            row = new ArrayList<Map<String, String>>();
			for (int iCol = 0; iCol < numCols; iCol++) {
           		cell = new LinkedHashMap<String, String>();
                cell.put(getFirstCellValue(), getSecondCellValue());
                row.add(cell);
            }
            table.add(row);
        }
		numRows+=plusRow;
	}

	public void sortTable() {
        for (List<Map<String, String>> row : table) {
       		Collections.sort(row, new CellComparator());
        }
        Collections.sort(table, new RowComparator());
    }

	public void printTable() {
		System.out.println();
        for (List<Map<String,String>> row : table) {
            StringBuilder sb = new StringBuilder();
            int iCol = 0;
            for (Map<String,String> cell : row) {
                if (iCol++ > 0) {
                    sb.append("\t");
                }
                for (String key : cell.keySet()) {
                    sb.append(key);
                    sb.append("  ");
                    sb.append(cell.get(key));
                }
            }
            System.out.println(sb.toString());
        }
		System.out.println();
    }

	public void createTable() {
		table = new ArrayList<List<Map<String, String>>>();
		numRows=checkRow(1,9);
		numCols=checkColumn(1,9);
        for (int iRow=0; iRow<numRows; iRow++) {
            row = new ArrayList<Map<String, String>>();
            for (int iCol = 0; iCol < numCols; iCol++) {
           		cell = new LinkedHashMap<String, String>();
                cell.put(getFirstCellValue(), getSecondCellValue());
                row.add(cell);
            }
            table.add(row);
        }
	}

	private String getFirstCellValue() {
        return getRandomAscii();
    }

    private String getSecondCellValue() {
        return getRandomAscii();
    }

    private String getRandomAscii() {
        Random ran = new Random();
        String result = "";
        for (int i=0;i<3;i++) {
            int value = ran.nextInt(89) + 33;
            result += (char) value;
        }
        return result;
    }

    public int occurence(String KeyValue, String KVString,String searchStr,int iRow, int iCol){
		int sum=0;
		switch(searchStr.length()){
			case 1:	{
					for(int k=0;k<3;k++){
						if(KVString.charAt(k)==searchStr.charAt(0)){
							System.out.printf("Found! %s Coordinate Row=%d Column=%d,\tKeyValue=%s,\tCharacter=%d \n",searchStr,iRow,iCol,KeyValue,(k+1));
		    				sum+=1;
						}
					}
					}
					break;
			case 2: {
					if(searchStr.charAt(0)==searchStr.charAt(1)){
						if((KVString.charAt(0)==KVString.charAt(1))&&(KVString.charAt(1)==KVString.charAt(2))){
							sum=sum+2;
							System.out.printf("Found! %s Coordinate Row=%d Column=%d,\tKeyValue=%s\t2 times \n",searchStr, iRow,iCol,KeyValue);
						}
						else{
							System.out.printf("Found! %s Coordinate Row=%d Column=%d,\tKeyValue=%s \n",searchStr, iRow,iCol,KeyValue);
							sum+=1;
						}
					}
					else{
						System.out.printf("Found! %s Coordinate Row=%d Column=%d,\tKeyValue=%s \n",searchStr, iRow,iCol,KeyValue);
			    		sum+=1;
					}
					}
					break;
			default:{
					System.out.printf("Found! %s Coordinate Row=%d Column=%d,\tKeyValue=%s \n ",searchStr, iRow,iCol,KeyValue);
			    	sum+=1;
					}
					break;
		}
		return sum;
	}

	public static int keyOrValue(){
		int keyOrValue=0;
		Scanner read= new Scanner(System.in);
		while(true)
		{
			System.out.print("Edit the 1.Key or 2.Value: ");
			try{
				keyOrValue=Integer.parseInt(read.next());
				if(keyOrValue>0&&keyOrValue<3)
				{
					break;
				}
				System.out.println("Not Valid! Reenter");
			}
			catch(NumberFormatException ignore){
				System.out.println("Invalid: not a number");
			}
		}
		return keyOrValue;
	}

	public void printToFile(){
		try{
			PrintWriter output = new PrintWriter(filePath);
			for (List<Map<String, String>> row : table) {
            StringBuilder sb = new StringBuilder();
            int iCol = 0;
            	for (Map<String, String> cell : row) {
            		if (iCol++ > 0) {
                    sb.append("\t");
            		}
            		for (String key : cell.keySet()) {
            			sb.append(key);
            			sb.append("  ");
            			sb.append(cell.get(key));
            		}
            	}
            output.println(sb.toString());
        	}
			output.close();
		}catch(IOException ex){
			System.out.println("");
		}
    }

	public void readFromFile() throws IOException{
		String key="";
		String val="";
		File file= new File(filePath);
		String string = FileUtils.readFileToString(file, "UTF-8");
		Path path = Paths.get(filePath);
		int lineCount = (int) Files.lines(path).count();
		numRows=lineCount;
		String [] split = StringUtils.split(string, "\t\n ");
		int halfCell=(split.length/numRows);
		numCols=halfCell/2;
		int i=0;
		table = new ArrayList<List<Map<String, String>>>();
		while(i<split.length){
			row = new ArrayList<Map<String, String>>();
			for(int j=0;j<halfCell;j++){
				if(j%2==0)
					key=split[i];
				else{
					val=split[i];
					cell = new LinkedHashMap<String, String>();
					cell.put(key, val);
	            	row.add(cell);
				}
				i++;
			}
			 table.add(row);
		}
	}


	private static class CellComparator implements Comparator<Map<String, String>> {
        public int compare(Map<String, String> o1, Map<String, String> o2) {
            String key1 = o1.keySet().iterator().next();
            String key2 = o2.keySet().iterator().next();
            return key1.compareTo(key2);
        }
    }

	private static class RowComparator implements Comparator<List<Map<String, String>>> {
	   public int compare(List<Map<String, String>> o1, List<Map<String, String>> o2) {
	       for (int i = 0; i < o1.size(); i++) {
	           String key1 = o1.get(i).keySet().iterator().next();
	           String key2 = o2.get(i).keySet().iterator().next();
	           if (!key1.equals(key2)) {
	               return key1.compareTo(key2);
	           }
		   }
	       return 0;
	   }
	}

	public static int checkRow(int min, int max){
		Scanner read = new Scanner(System.in);
		int row=0;
		while(true){
			try{
				System.out.printf("Enter row (%s-%s) : ",min,max);
				row=Integer.parseInt(read.next());
				if(row>=min&&row<=max){
					break;
				}
				System.out.println("Not in range");
			}catch(NumberFormatException ignore){
				System.out.println("Not a number");
			}
		}
		return row;
	}

	public static int checkColumn(int min, int max){
		Scanner read = new Scanner(System.in);
		int column=0;
		while(true){
			try{
				System.out.printf("Enter column (%s-%s) : ",min,max);
				column=Integer.parseInt(read.next());
				if(column>=min&&column<=max){
					break;
				}
				System.out.println("Not in range");
			}catch(NumberFormatException ignore){
				System.out.println("Not a number");
			}
		}
		return column;
	}
}
