//package org.farozy.dto;
//
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalTime;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class StoreDtoTest {
//
//    private Validator validator;
//
//    @BeforeEach
//    public void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    public void testValidStoreDto() {
//        StoreDto storeDto = new StoreDto();
//        storeDto.setName("Store Name");
//        storeDto.setDescription("This is a description.");
//        storeDto.setAddress("123 Store St.");
//        storeDto.setOpeningTime(LocalTime.of(9, 0)); // 09:00 AM
//        storeDto.setClosingTime(LocalTime.of(17, 0)); // 05:00 PM
//        storeDto.setLatitude(new BigDecimal("12.345678"));
//        storeDto.setLongitude(new BigDecimal("98.765432"));
//
//        Set<ConstraintViolation<StoreDto>> violations = validator.validate(storeDto);
//        assertTrue(violations.isEmpty(), "Expected no validation errors, but got: " + violations);
//    }
//
//    @Test
//    public void testStoreDtoWithNullName() {
//        StoreDto storeDto = new StoreDto();
//        storeDto.setDescription("This is a description.");
//        storeDto.setAddress("123 Store St.");
//        storeDto.setOpeningTime(LocalTime.of(9, 0));
//        storeDto.setClosingTime(LocalTime.of(17, 0));
//
//        Set<ConstraintViolation<StoreDto>> violations = validator.validate(storeDto);
//        assertEquals(1, violations.size());
//        assertEquals("Name is required", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testStoreDtoWithEmptyDescription() {
//        StoreDto storeDto = new StoreDto();
//        storeDto.setName("Store Name");
//        storeDto.setDescription("");
//        storeDto.setAddress("123 Store St.");
//        storeDto.setOpeningTime(LocalTime.of(9, 0));
//        storeDto.setClosingTime(LocalTime.of(17, 0));
//
//        Set<ConstraintViolation<StoreDto>> violations = validator.validate(storeDto);
//        assertEquals(1, violations.size());
//        assertEquals("Description is required", violations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void testStoreDtoWithNullOpeningTime() {
//        StoreDto storeDto = new StoreDto();
//        storeDto.setName("Store Name");
//        storeDto.setDescription("This is a description.");
//        storeDto.setAddress("123 Store St.");
//        storeDto.setOpeningTime(null); // Opening time is null
//        storeDto.setClosingTime(LocalTime.of(17, 0));
//
//        Set<ConstraintViolation<StoreDto>> violations = validator.validate(storeDto);
//        assertEquals(1, violations.size());
//        assertEquals("Opening time is requeired", violations.iterator().next().getMessage());
//    }
//
//    // Tambahkan pengujian lainnya sesuai kebutuhan untuk field lain
//
//}
