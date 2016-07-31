package com.arjay.exercise;

import java.io.*;
import java.util.*;

public class Main{

	private Scanner read = new Scanner (System.in);
	public static Table table= new Table();
	
	public static void main(String[] args)  throws IOException{
		table.checkIfFileExist();
		new Main().menu();
	}
	
	private void menu(){
		System.out.println("\nChoose from the following");
		System.out.println("[1].Search  [2].Edit  [3].Add Row  [4].Sort  [5].Print  [6].Reset  [7].Exit\n");
		System.out.print("Enter Choice: ");
		while(!read.hasNextInt()){
			System.out.println("Enter a valid choice! (1-7): ");
			read.next();
		}
		int choice=read.nextInt();
		switch(choice){
			case 1: 
				table.searchTable();
				break;
			case 2:	
				table.editTable();
				break;
			case 3: 
				table.addRow();
				break;
			case 4: 
				table.sortTable();
				break;
			case 5:	
				table.printTable();
				break;
			case 6: 
				table.createTable();
				break;
			case 7: 
				read.close();
				System.exit(0);
				break;
			default:
				System.out.println("Invalid Choice!!!. Re-enter Choice");
				break;			
		}
		table.printToFile();
		menu();
	}
}
