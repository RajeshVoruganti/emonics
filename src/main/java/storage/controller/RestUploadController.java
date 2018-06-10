package storage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Data;
import model.DataFile;
import model.UploadModel;
import repository.DataFileRepository;
import repository.DataRepository;
import service.SaveToDBService;
import storage.FileSystemStorageService;
import storage.StorageProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
public class RestUploadController {

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);
    
    
    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataFileRepository dataFileRepository;

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "D://temp//";

    //Single file upload
    @PostMapping("/api/upload")
    // If not @RestController, uncomment this
    //@ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile uploadfile) {

        logger.debug("Single file upload!");

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }


            try {
				saveUploadedFiles(Arrays.asList(uploadfile));
			} catch (IOException e) {
				e.printStackTrace();
				 return new ResponseEntity("Error not uploaded - " +
			                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED);

				
			}


        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);

    }
    
    @GetMapping("/datafile/{Id}")
    public Optional<DataFile> getFileData(@PathVariable Long Id) {
    	return dataFileRepository.findById(Id);
    	
    }
    


    //save file
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException  {

        for (MultipartFile file : files) {
        	
            if (file.isEmpty()) {
                continue; //next pls
            }
            System.out.println("file loding.../n/n/n/n");
            	Path path = Paths.get(UPLOADED_FOLDER);
            	if(Files.notExists(path)){
                    Files.createFile(Files.createDirectories(path)).toFile();
                }
            	File convFile = new File(UPLOADED_FOLDER+"/"+ file.getOriginalFilename());
            	convFile.createNewFile();
                file.transferTo(convFile);
                saveDataFile(convFile);
        }

    }
    
    
    public DataFile saveDataFile(File file) {
    	DataFile df = new DataFile();
    	df.setFileName(file.getName());
    	byte[] encoded = null;
		try {
			encoded = Files.readAllBytes(Paths.get(file.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	String content = new String(encoded);
    	df.setContent(content);
    	DataFile dataFile = saveDataFile(df);
    	saveData(content,dataFile);
    	
    	return dataFile;
    	
    }
    
    public void saveData(String data,DataFile dataFile) {
    	
    	try {
			HashMap<String,String> dataMap =
			        new ObjectMapper().readValue(data, HashMap.class);
			System.out.println(dataMap);
			Set<Map.Entry<String, String>> entrySet1 = dataMap.entrySet();
			
			for (Entry entry : entrySet1) {
				Data dat =  new Data();
				dat.setFile(dataFile);
				dat.setPropertyName((String)entry.getKey());
				dat.setPropertyValue((String)entry.getValue());
				saveData(dat);
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public DataFile saveDataFile(DataFile dataFile) {
    	return dataFileRepository.save(dataFile);
    }
    
    public Data saveData(Data data) {
    	return dataRepository.save(data);
    	
    }
}
