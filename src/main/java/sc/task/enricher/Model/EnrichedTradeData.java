package sc.task.enricher.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EnrichedTradeData {
    LocalDate date;
    String productName;
    String currency;
    BigDecimal price;

}
