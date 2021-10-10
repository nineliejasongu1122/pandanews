package sg.edu.smu.cs203.pandanews.service.News;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import sg.edu.smu.cs203.pandanews.model.news.News;
import sg.edu.smu.cs203.pandanews.model.news.NewsDAO;
import sg.edu.smu.cs203.pandanews.model.news.NewsListDAO;
import sg.edu.smu.cs203.pandanews.repository.NewsRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service
public class NewsNewsAPIServiceImpl implements NewsAPIService {

    @Value("${azure.bing.ApiKey}")
    private String ApiKey;
    @Value("${azure.bing.endpoint}")
    private String endpoint;
    @Value("${azure.bing.countryCode}")
    private String countryCode;
    @Value("${azure.bing.phase}")
    private String phase;

    @Autowired
    private NewsRepository newsRepository;


    @Override
//    public ResponseEntity<?> apiCall() {
    public List<News> apiCall() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", ApiKey);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity(headers);
        NewsListDAO newsListDAO;
        try {
            ObjectMapper mapper = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false);
            newsListDAO = mapper.
                    readValue(restTemplate.exchange(String.format(endpoint + "?cc=%s&q=%s", countryCode, phase), HttpMethod.GET,
                            entity, String.class, 1).getBody(), NewsListDAO.class);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
//    public News(String title, String description, String content, String coverImage, Date date) {
        List<News> newsList = new ArrayList<>();

        for (NewsDAO news : newsListDAO.getValue()) {
           News n = new News(news.getName(), news.getDescription(), news.getUrl(),
                    news.getImage().getThumbnail().getContentUrl(),
                    formatter(news.getDatePublished()));
            newsList.add(n);
            newsRepository.save(n);
        }
        return newsList;
    }

    private Date formatter(String date) {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd");
        Date d = null;
        try {
            d = dt.parse(date);
        } catch (ParseException e) {
            return null;
        }
        return d;
    }
}
