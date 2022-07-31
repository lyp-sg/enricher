package sc.task.enricher.Service;

import org.apache.commons.csv.CSVRecord;
import sc.task.enricher.Model.EnrichedTradeData;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

public interface EnricherService {
    List<EnrichedTradeData> getEnrichedTradeData(InputStream inputStream) throws IOException;

    List<EnrichedTradeData> getEnrichedTradeData(Iterable<CSVRecord> csvRecords);

    void writeEnrichedTradeDataToResponse(PrintWriter writer, List<EnrichedTradeData> tradeDataList) throws IOException;

}
