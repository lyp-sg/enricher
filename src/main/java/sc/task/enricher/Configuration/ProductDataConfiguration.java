package sc.task.enricher.Configuration;

import org.apache.commons.csv.CSVRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import sc.task.enricher.Utils.CSVUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProductDataConfiguration {

    @Bean
    public Map<Integer, String> loadProductDataMap(){
        Map<Integer, String> map = new HashMap<Integer, String>();

        try {
            File file = ResourceUtils.getFile("classpath:static/product.csv");

            Iterable<CSVRecord> csvRecords = CSVUtils.getCsvRecordsFromInput(new FileInputStream(file));

            csvRecords.forEach(
                    record -> map.put(Integer.parseInt(
                    record.get("product_id")),
                    record.get("product_name")));

        }catch (IOException ex){

        }
        return map;
    }
}
