package com.lcwd.electronicstore.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.lcwd.electronicstore.exceptions.BadApiRequestException;
import com.lcwd.electronicstore.services.FileService;


@Component
public class FileServiceImpl implements FileService{

	private Logger logger=LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadImage(MultipartFile file, String path) throws IOException {
		String originalFileName=file.getOriginalFilename();
		logger.info("FileName :- {}",originalFileName);
		String filenameString =UUID.randomUUID().toString();
		String extention=originalFileName.substring(originalFileName.lastIndexOf("."));
		String fileNameWithExtension = filenameString+extention;
		String fullPathWithFileName =path+fileNameWithExtension;
		if(extention.contentEquals(".png") ||extention.contentEquals(".jpeg")) {
			File folder=new File(path);
			if(!folder.exists()) {
				folder.mkdirs();
			}
			Files.copy(file.getInputStream(),Paths.get(fullPathWithFileName));
			return fileNameWithExtension;
		}else {
			throw new BadApiRequestException("FileWith this " +extention+ " not allowed !!");
		}
	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		String fullPathString=path+File.separator+name;
		InputStream inputStream=new FileInputStream(fullPathString);
		return inputStream;
	}

}
