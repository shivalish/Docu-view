package FWD_Development.DocuView.controllers.api.v1;

import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.jdbc.core.JdbcTemplate;

@Service
public class Async_Func {
    
    @Async
    public void cache(GoogleDriveService googleDriveService, JdbcTemplate jdbcTemplate, List<Map<String, Object>> resp) {
        int counter = 0;
        for (Map<String, Object> row : resp) {
            if (counter >= 12) {
                break;
            }
            String attachmentId = ((Integer) row.get("attachmentId")).toString();
            try {
                FileShareV1.previewCache(googleDriveService, jdbcTemplate, attachmentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            counter++;
        }
        return;
	}
}
