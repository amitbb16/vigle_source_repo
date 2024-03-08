package com.retro.dev.util;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class DevUtils {

	private static final Logger logger = LoggerFactory.getLogger(DevUtils.class);

	@Value("${fileUpload.dir}")
	private String fileUploadDir;

	
	
	
	public String getFileUploadDir() {
		return fileUploadDir;
	}

	public void setFileUploadDir(String fileUploadDir) {
		this.fileUploadDir = fileUploadDir;
	}
	
	
	@SuppressWarnings("unused")
	private Properties loadPropertyData(String propertyName) {
		try {
			FileReader reader = new FileReader(propertyName);
			Properties propData = new Properties();
			propData.load(reader);
			return propData;
		}catch (FileNotFoundException fnf) {
			logger.info(fnf.getMessage());
			fnf.printStackTrace();
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return new Properties();
	}
	
	public Date getCurrentDateAndTime() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = sdf.parse(java.time.Clock.systemUTC().instant().toString());
			return output.parse(output.format(d));
		} catch (ParseException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String getCompleteImagePath(Long userId, String absolutePath) {
		String fileUploadDir = getFileUploadDir()+userId+File.separator;
		String currentDirectory = System.getProperty("user.dir")+File.separator+ fileUploadDir+File.separator;
		return currentDirectory+File.separator+absolutePath;
	}
	
	public LocalDateTime getLocalDateTime() {
		LocalDateTime dateTime = LocalDateTime.now();
		dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return dateTime;
	}
	
	//public static String[] getNullPropertyNames(Object source) {
	public String[] getNullPropertyNames(Object source) {
	    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
	    return Stream.of(wrappedSource.getPropertyDescriptors())
	            .map(FeatureDescriptor::getName)
	            .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
	            .toArray(String[]::new);
	}
	
	public LocalDateTime addDaysToProvidedDateTime(LocalDateTime providedDateTime, String noOfDays) {
		LocalDateTime dateTime = providedDateTime.plusDays(Integer.valueOf(noOfDays));
		dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return dateTime;
	}
	
	public LocalDateTime subtractDaysToProvidedDateTime(LocalDateTime providedDateTime, String noOfDays) {
		LocalDateTime dateTime = providedDateTime.minusDays(Integer.valueOf(noOfDays));
		dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return dateTime;
	}
	
}
