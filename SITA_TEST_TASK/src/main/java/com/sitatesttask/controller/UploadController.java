package com.sitatesttask.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sitatesttask.model.UploadedFile;
import com.sitatesttask.validator.FileValidator;

@Controller
public class UploadController {

	@Autowired
	FileValidator fileValidator;

	@RequestMapping("/fileUploadForm")
	public ModelAndView getUploadForm(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result) {
		return new ModelAndView("uploadForm");
	}

	@RequestMapping("/fileUpload")
	public ModelAndView fileUploaded(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			BindingResult result) {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		MultipartFile file = uploadedFile.getFile();
		fileValidator.validate(uploadedFile, result);

		String fileName = file.getOriginalFilename();
		Scanner scan = null;
	      File f = new File(fileName);
	      try {
	         scan = new Scanner(f);
	      } catch (FileNotFoundException e) {
	         System.out.println("File not found.");
	         System.exit(0);
	      }

	      int total = 0;
	      boolean foundInts = false; //flag to see if there are any integers

	      while (scan.hasNextLine()) { //Note change
	         String currentLine = scan.nextLine();
	         //split into words
	         String words[] = currentLine.split(" ");

	         //For each word in the line
	         for(String str : words) {
	            try {
	               int num = Integer.parseInt(str);
	               total += num;
	               foundInts = true;
	               System.out.println("Found: " + num);
	               File newFile = new File("C:/SITA_TEST_TASK/PROCESSED/" + fileName);	               
	            }catch(NumberFormatException nfe) { }; //word is not an integer, do nothing
	         }
	      } //end while 

	      if(!foundInts){
	         System.out.println("No numbers found.");
	      File newFile = new File("C:/SITA_TEST_TASK/ERROR/" + fileName);
	      }
	      // close the scanner
	      scan.close();
	      File newFile = new File("C:/SITA_TEST_TASK/OUT/" + fileName);
		if (result.hasErrors()) {
			return new ModelAndView("uploadForm");
		}

		try {
			inputStream = file.getInputStream();

			 newFile = new File("C:/Users/nagesh.chauhan/files/" + fileName);
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			outputStream = new FileOutputStream(newFile);
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ModelAndView("showFile", "message", fileName);
	}

}
