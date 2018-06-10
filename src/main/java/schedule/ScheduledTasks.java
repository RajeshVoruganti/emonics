package schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import repository.DataFileRepository;
import model.DataFile;
import java.util.List;

@Component
public class ScheduledTasks {
	
	@Autowired
	private JavaMailSender sender;
	
	@Autowired
    private DataFileRepository dataFileRepository;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
    	
    	Date time = new Date(System.currentTimeMillis() - 3600 * 1000);
    	MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo("sailendra.mandla@gmail.com");
			helper.setText("Files added in last hour");
			List<DataFile> dataFiles = dataFileRepository.findByTime(time);
			for(DataFile file: dataFiles) {
				helper.setText(file.toString());
			}
			helper.setSubject("Files Update");
		} catch (MessagingException e) {
			e.printStackTrace();
			log.info("mail not sent");
		}
		sender.send(message);
		log.info("mail sent");
       
    }
}
