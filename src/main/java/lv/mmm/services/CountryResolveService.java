package lv.mmm.services;

import lv.mmm.domain.GeoServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Service
public class CountryResolveService {

    @Value("${geo.service.loaction}")
    private String geoServiceLocation;

    @Value("${geo.service.security.key}")
    private String geoServiceSecurityKey;

    @Value("${geo.service.default.country.code}")
    private String defaultCountryCode;

    private final RestTemplate restTemplate;

    public CountryResolveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCountryCodeByIP(String ip) {
        if (StringUtils.isEmpty(ip)) {
            throw new IllegalArgumentException("IP shouldn't be null");
        }
        GeoServiceResponse geoServiceResponse = restTemplate.getForObject(buildCountryQueryLink(ip), GeoServiceResponse.class);
        return geoServiceResponse == null || StringUtils.isEmpty(geoServiceResponse.getCountry_code()) ? defaultCountryCode : geoServiceResponse.getCountry_code();

    }

    private String buildCountryQueryLink(String ip) {
        return geoServiceLocation + "?ip=" + ip + "&auth=" + geoServiceSecurityKey;
    }
}
