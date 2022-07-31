package sc.task.enricher.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;
import sc.task.enricher.Service.EnricherService;

import javax.annotation.Resource;
import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EnricherControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EnricherService enricherService;

    @Resource
    private TestRestTemplate restTemplate;

    @Test
    void whenPost_thenReturn() throws Exception {

        String fileContent = getRequestData();
        String postResponse = restTemplate.postForObject("/api/v1/enrich",createUploadRequest(fileContent),String.class);

        String expectedOutcome = "date,product_name,currency,price\r\n20160101,Treasury Bills Domestic,EUR,10.0\r\n20160101,Corporate Bonds Domestic,EUR,20.1\r\n20160101,REPO Domestic,EUR,30.34\r\n";
        assertEquals(postResponse,expectedOutcome);
    }

    private String getRequestData() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line, fileContent = "";
        File file = ResourceUtils.getFile("classpath:test/trade.csv");

        BufferedReader reader  = new BufferedReader((new InputStreamReader(new FileInputStream(file))));

        while((line = reader.readLine()) !=null){
            sb.append(line).toString();
            sb.append("\r\n");
        }

        fileContent = sb.toString();
        return fileContent;
    }

    private HttpEntity<String> createUploadRequest(String fileContent) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new HttpEntity<String>(fileContent, headers);
    }

}