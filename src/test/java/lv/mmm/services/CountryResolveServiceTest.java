package lv.mmm.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryResolveServiceTest {

    @Autowired
    private CountryResolveService countryResolveService;

    @Test
    public void getCountryCodeByIP() throws Exception {
        Assert.assertEquals("US", countryResolveService.getCountryCodeByIP("8.8.8.8"));
        Assert.assertEquals("LV", countryResolveService.getCountryCodeByIP("0000000"));
    }

}