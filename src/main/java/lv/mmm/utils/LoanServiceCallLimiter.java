package lv.mmm.utils;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@ApplicationScope
public class LoanServiceCallLimiter {
    @Value("${max.loan.aplly.service.calls.per.second.for.single.country}")
    private Long maxServiceCallsPerSecond;

    private Map<String, CircularFifoQueue<Long>> countryBufferMap;

    public LoanServiceCallLimiter() {
        countryBufferMap = new HashedMap<>();
    }

    public void serviceCall(String country) {
        if (countryBufferMap.containsKey(country)) {
            //There already was calls for this country
            CircularFifoQueue<Long> buffer = countryBufferMap.get(country);
            if (buffer.isAtFullCapacity()) {
                //Need to check if country isn't exceeding it's limit
                Long oldestCallInBuffer = buffer.get(buffer.maxSize() - 1);
                Long currentTime = System.currentTimeMillis();
                if (currentTime - oldestCallInBuffer < TimeUnit.SECONDS.toMillis(1)) {
                    throw new AccessDeniedException("Limit for loan apply service calls for " + country + " country is exceeded. Pleas wait");
                } else {
                    //Passed enough time
                    buffer.add(System.currentTimeMillis());
                }
            } else {
                //In this case it isn't possible to exceed limit. Just add new record
                buffer.add(System.currentTimeMillis());
            }
        } else {
            //It's first call for this country.
            CircularFifoQueue<Long> buffer = new CircularFifoQueue<>(maxServiceCallsPerSecond.intValue());
            buffer.add(System.currentTimeMillis());
            countryBufferMap.put(country, buffer);
        }
    }

}
