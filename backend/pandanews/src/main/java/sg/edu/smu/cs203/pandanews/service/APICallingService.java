package sg.edu.smu.cs203.pandanews.service;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
//This method is for calling of external API
public interface APICallingService {
    public ResponseEntity<?> apiCall();
//    public ResponseEntity<?> apiCall();
}
