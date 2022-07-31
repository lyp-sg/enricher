package sc.task.enricher.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sc.task.enricher.Model.EnrichedTradeData;
import sc.task.enricher.Service.EnricherService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class EnricherController {

    @Autowired
    private EnricherService enricherService;

    @PostMapping(value = "/api/v1/enrich",
            produces = "text/csv")
    public void enrichTradeDataSimple(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<EnrichedTradeData> tradeDataList = enricherService.getEnrichedTradeData(request.getInputStream());
        enricherService.writeEnrichedTradeDataToResponse(response.getWriter(), tradeDataList);
    }
}
