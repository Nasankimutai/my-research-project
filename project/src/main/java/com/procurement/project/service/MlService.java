package com.procurement.project.service;

import com.procurement.project.dto.MlFeatureDTO;
import com.procurement.project.dto.MlResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.Map;
import java.util.List;

@Service
public class MlService {

    private final String ML_API_URL = "http://localhost:5000/predict";

    public MlResponseDTO analyzeFeatures(MlFeatureDTO featureDTO) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Our FastAPI expects a list of feature objects
        List<MlFeatureDTO> requestList = Arrays.asList(featureDTO);
        HttpEntity<List<MlFeatureDTO>> requestEntity = new HttpEntity<>(requestList, headers);

        // Response shape:
        // {
        //   "predictions": [
        //     { "label": 0/1, "type": "...", "severity": "...", "score": ... }
        //   ]
        // }
        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                ML_API_URL,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        Map<String, Object> resultMap = responseEntity.getBody();
        if (resultMap == null || !resultMap.containsKey("predictions")) {
            // fallback if there's no "predictions" key
            MlResponseDTO errorResponse = new MlResponseDTO();
            errorResponse.setTransaction_id(featureDTO.getTransaction_id());
            errorResponse.setLabel(0);
            errorResponse.setAnomaly_type("N/A");
            errorResponse.setSeverity("N/A");
            errorResponse.setScore(0.0);
            return errorResponse;
        }

        // parse predictions array
        List<Map<String, Object>> predictions = (List<Map<String, Object>>) resultMap.get("predictions");
        // assume only one transaction was sent
        Map<String, Object> single = predictions.get(0);

        // extract fields
        int label = (int) single.get("label");
        String type = (String) single.get("anomaly_type");
        String severity = (String) single.get("severity");
        double score = Double.parseDouble(single.get("score").toString());

        // Build the MLResponseDTO
        MlResponseDTO mlResponse = new MlResponseDTO();
        mlResponse.setTransaction_id(featureDTO.getTransaction_id());
        mlResponse.setLabel(label);
        mlResponse.setAnomaly_type(type);
        mlResponse.setSeverity(severity);
        mlResponse.setScore(score);

        return mlResponse;
    }
}
