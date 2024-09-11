package com.mainhub.homebanking.UtilsTests;

import com.mainhub.homebanking.utils.GenerateNumber;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class GenerateNumberTests {
    @Autowired
    GenerateNumber generateNumber;

    @Test
    void generateNumberTest() {
        String number = generateNumber.generateNumber();
        assertThat(number, containsString("-"));
    }

//     @Test
//    void generateRandomNumberTest() {
//       String number = generateNumber.generateRandomNumber();
//        assertThat(number, containsString("-"));
//    }

    @Test
    void prefijoTest() {
        String prefijo = generateNumber.prefijo();
        assertThat(prefijo, is("VIN"));
    }
}
