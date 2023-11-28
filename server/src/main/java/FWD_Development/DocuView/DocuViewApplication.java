package FWD_Development.DocuView;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DocuViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocuViewApplication.class, args);
	}

}
