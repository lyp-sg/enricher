package sc.task.enricher.Service;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;
import sc.task.enricher.Model.EnrichedTradeData;
import sc.task.enricher.EnricherApplication;
import sc.task.enricher.Utils.CSVUtils;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
        classes = EnricherApplication.class)
@AutoConfigureMockMvc
class EnricherServiceImplTest {

    @Autowired
    private EnricherService enricherService;

    @Test
    void getEnrichedTradeDataInvalidDate() throws IOException {
        // Given
        File file = ResourceUtils.getFile("classpath:test/invalidDateTradeData.csv");

        // When
        Iterable<CSVRecord> csvRecords = CSVUtils.getCsvRecordsFromInput(new FileInputStream(file));

        //Then
        assertEquals(0, enricherService.getEnrichedTradeData(csvRecords).size());

    }


    @Test
    void getEnrichedTradeDataValidDate() throws IOException {

        // Given
        File file = ResourceUtils.getFile("classpath:test/validDateTradeData.csv");

        // When
        Iterable<CSVRecord> csvRecords = CSVUtils.getCsvRecordsFromInput(new FileInputStream(file));

        // Then
        assertEquals(1, enricherService.getEnrichedTradeData(csvRecords).size());

    }

    @Test
    void writeEnrichedTradeDataToResponse() throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        // Given
        List<EnrichedTradeData> tradeDataList = new ArrayList<>();
        tradeDataList.add(
                EnrichedTradeData.builder()
                        .date(LocalDate.parse("20160101", DateTimeFormatter.BASIC_ISO_DATE))
                        .productName("Treasury Bills Domestic")
                        .currency("EUR")
                        .price(new BigDecimal("10.0")).build());
        tradeDataList.add(
                EnrichedTradeData.builder()
                        .date(LocalDate.parse("20160101", DateTimeFormatter.BASIC_ISO_DATE))
                        .productName("Corporate Bonds Domestic")
                        .currency("EUR")
                        .price(new BigDecimal("20.1")).build());
        tradeDataList.add(
                EnrichedTradeData.builder()
                        .date(LocalDate.parse("20160101", DateTimeFormatter.BASIC_ISO_DATE))
                        .productName("REPO Domestic")
                        .currency("EUR")
                        .price(new BigDecimal("30.34")).build());

        // When
        enricherService.writeEnrichedTradeDataToResponse(printWriter, tradeDataList);

        // Then
        String actualCsv = stringWriter.toString();
        assertThat(actualCsv.split("\r\n"))
                .as("Produced CSV")
                .containsExactly(
                        "date,product_name,currency,price",
                        "20160101,Treasury Bills Domestic,EUR,10.0",
                        "20160101,Corporate Bonds Domestic,EUR,20.1",
                        "20160101,REPO Domestic,EUR,30.34");

    }
}