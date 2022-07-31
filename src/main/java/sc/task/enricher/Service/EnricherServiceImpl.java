package sc.task.enricher.Service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.validator.GenericValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sc.task.enricher.Model.EnrichedTradeData;
import sc.task.enricher.Utils.CSVUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EnricherServiceImpl implements EnricherService {

    @Autowired
    private Map<Integer, String> productDataMap;

    @Override
    public List<EnrichedTradeData> getEnrichedTradeData(InputStream inputStream) throws IOException {

        Iterable<CSVRecord> csvRecords = CSVUtils.getCsvRecordsFromInput(inputStream);
        return getEnrichedTradeData(csvRecords);
    }

    public List<EnrichedTradeData> getEnrichedTradeData(Iterable<CSVRecord> csvRecords) {
        List<EnrichedTradeData> tradeDataList = new ArrayList<>();

        for(CSVRecord record:csvRecords) {
            if(!GenericValidator.isDate(record.get("date"),"yyyyMMdd",true)){
                log.info("Date is invalid "+ record.get("date"));
                continue;
            }

            tradeDataList.add(
                    EnrichedTradeData.builder()
                            .date(LocalDate.parse(record.get("date"), DateTimeFormatter.BASIC_ISO_DATE))
                            .productName(productDataMap.get(Integer.parseInt(record.get("product_id"))))
                            .currency(record.get("currency"))
                            .price(new BigDecimal(record.get("price")))
                            .build());
        }
        return tradeDataList;
    }

    @Override
    public void writeEnrichedTradeDataToResponse(PrintWriter writer, List<EnrichedTradeData> tradeDataList) throws IOException {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        CSVPrinter csvPrinter = new CSVPrinter(writer, format);
        String[] header = new String[]{"date", "product_name", "currency", "price"};

        csvPrinter.printRecord(header);
        for (EnrichedTradeData tradeData : tradeDataList) {
            csvPrinter.printRecord(
                    Arrays.asList(
                            String.valueOf(tradeData.getDate().format(DateTimeFormatter.BASIC_ISO_DATE)),
                            tradeData.getProductName(),
                            tradeData.getCurrency(),
                            String.valueOf(tradeData.getPrice())
                    ));
        }

        csvPrinter.flush();
    }
}
