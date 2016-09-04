package lv.mmm.services;

import lv.mmm.domain.GeoServiceResponse;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CountryResolveService {
    private static final Logger LOG = Logger.getLogger(CountryResolveService.class);

    @Value("${geo.service.loaction}")
    private String geoServiceLocation;

    @Value("${geo.service.security.key}")
    private String geoServiceSecurityKey;

    @Value("${geo.service.default.country.code}")
    private String defaultCountryCode;

    private final RestTemplate restTemplate;
    private Map<String, String> cache;

    public CountryResolveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        cache = new HashMap<String,String>();
    }

    public String getCountryCodeByIP(String ip) {
        if (StringUtils.isEmpty(ip)) {
            throw new IllegalArgumentException("IP shouldn't be null");
        }
        if (cache.containsKey(ip)) {
            return cache.get(ip);
        }
        GeoServiceResponse geoServiceResponse;
        try {
            geoServiceResponse = restTemplate.getForObject(buildCountryQueryLink(ip), GeoServiceResponse.class);
        } catch (RestClientException e) {
            LOG.warn(e);
            geoServiceResponse = null;
        }
        if (geoServiceResponse == null || StringUtils.isEmpty(geoServiceResponse.getCountry_code())) {
            return defaultCountryCode;
        } else {
            cache.put(ip, geoServiceResponse.getCountry_code());
            return geoServiceResponse.getCountry_code();
        }
        //return geoServiceResponse == null || StringUtils.isEmpty(geoServiceResponse.getCountry_code()) ? defaultCountryCode : geoServiceResponse.getCountry_code();

    }

    private String buildCountryQueryLink(String ip) {
        return geoServiceLocation + "?ip=" + ip + "&auth=" + geoServiceSecurityKey;
    }
}
