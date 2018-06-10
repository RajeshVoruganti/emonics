package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.Data;
import model.DataFile;
import repository.DataFileRepository;
import repository.DataRepository;

@Service
@Configurable
public class SaveToDBService {
	@Autowired
    private DataRepository dataRepository;

    @Autowired
    private DataFileRepository dataFileRepository;
    
    
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
    	
    	DataFile dataFile = saveDataFile(df);
    	
    	return dataFile;
    	
    }
    
    public void saveData(String data,DataFile dataFile) {
    	
    	try {
			HashMap<String,String> dataMap =
			        new ObjectMapper().readValue(data, HashMap.class);
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
