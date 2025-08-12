package shiyee_FYP.fullstack_backend.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import shiyee_FYP.fullstack_backend.model.GlobalLocation;
import shiyee_FYP.fullstack_backend.model.GlobalLocationRelation;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRelationRepository;
import shiyee_FYP.fullstack_backend.repository.GlobalLocationRepository;
import org.springframework.http.*;

import java.util.*;
@Slf4j
@Service
public class GlobalService {
    @Autowired
    private GlobalLocationRepository locationRepository;

    @Autowired
    private GlobalLocationRelationRepository globalLocationRelationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${google.maps.api.key}")
    private String googleApiKey;

    @Value("${searoute.api.key}")
    private String searoutesApiKey;

    public List<GlobalLocation> getAllLocations() {
        return locationRepository.findAll();
    }


    public List<GlobalLocationRelation> getAllRelations() {
        return globalLocationRelationRepository.findAll();
    }

    public List<GlobalLocation> getLocationsByCompanyId(Long companyId) {
        return locationRepository.findByCompanyId(companyId);
    }

    public List<GlobalLocationRelation> getRelations(Long sourceId, Long targetId) {
        return globalLocationRelationRepository.findBySourceIdAndTargetId(sourceId, targetId);
    }

    public GlobalLocation getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }


    public String getDirectionByRelationId(Long relationId) {
        GlobalLocationRelation relation = globalLocationRelationRepository.findById(relationId)
                .orElseThrow(() -> new EntityNotFoundException("Relation not found with id: " + relationId));

        if (!"ROAD".equalsIgnoreCase(relation.getTransportMode())) {
            throw new IllegalArgumentException("Direction calculation is only available for ROAD transport mode");
        }

        GlobalLocation origin = locationRepository.findById(relation.getSourceId())
                .orElseThrow(() -> new EntityNotFoundException("Source location not found with id: " + relation.getSourceId()));

        GlobalLocation destination = locationRepository.findById(relation.getTargetId())
                .orElseThrow(() -> new EntityNotFoundException("Target location not found with id: " + relation.getTargetId()));

        String googleMapsUrl = "https://maps.googleapis.com/maps/api/directions/json?origin="
                + origin.getLatitude() + "," + origin.getLongitude() + "&destination="
                + destination.getLatitude() + "," + destination.getLongitude()
                + "&key=" + googleApiKey;

        ResponseEntity<String> response = restTemplate.exchange(
                googleMapsUrl, HttpMethod.GET, null, String.class);

        String responseBody = response.getBody();
        if (responseBody.contains("\"status\" : \"ZERO_RESULTS\"")) {
            return "{\"error\":\"No route found between the locations. Please check the coordinates.\"}";
        }

        return responseBody;
    }
}
