package sc.task.enricher.Utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CSVUtils {

    public static Iterable<CSVRecord> getCsvRecordsFromInput(InputStream inputStream) throws IOException {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        CSVParser parser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        return parser.getRecords();
    }

}
