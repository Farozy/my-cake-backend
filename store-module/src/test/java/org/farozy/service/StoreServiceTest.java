//package org.farozy.service;
//
//import org.farozy.dto.StoreDto;
//import org.farozy.entity.Store;
//import org.farozy.repository.StoreRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//public class StoreServiceTest {
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @InjectMocks
//    private StoreServiceImpl storeService;
//
//    private StoreDto storeDto;
//    private Store store;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        storeDto = new StoreDto();
//        storeDto.setName("Test Store");
//        storeDto.setDescription("Description for test store.");
//        storeDto.setAddress("123 Test Address");
//        storeDto.setOpeningTime(LocalTime.of(9, 0));
//        storeDto.setClosingTime(LocalTime.of(17, 0));
//        storeDto.setImage(null); // You can use a mock MultipartFile if needed
//        storeDto.setLatitude(new BigDecimal("12.345678"));
//        storeDto.setLongitude(new BigDecimal("98.765432"));
//
//        store = new Store();
//        store.setId(1L);
//        store.setName("Test Store");
//        store.setDescription("Description for test store.");
//        store.setAddress("123 Test Address");
//        store.setOpeningTime(LocalTime.of(9, 0));
//        store.setClosingTime(LocalTime.of(17, 0));
//        store.setImage("test_image.png");
//        store.setLatitude(new BigDecimal("12.345678"));
//        store.setLongitude(new BigDecimal("98.765432"));
//    }
//
//    @Test
//    public void testFindAll() {
//        List<Store> storeList = new ArrayList<>();
//        storeList.add(store);
//
//        when(storeRepository.findAll()).thenReturn(storeList);
//
//        List<Store> result = storeService.findAll();
//
//        assertEquals(1, result.size());
//        assertEquals("Test Store", result.get(0).getName());
//        verify(storeRepository, times(1)).findAll();
//    }
//
//    @Test
//    public void testFindById() {
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
//
//        Store result = storeService.findById(1L);
//
//        assertNotNull(result);
//        assertEquals("Test Store", result.getName());
//        verify(storeRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testSave() {
//        when(storeRepository.save(any(Store.class))).thenReturn(store);
//
//        Store result = storeService.save(storeDto, null);
//
//        assertNotNull(result);
//        assertEquals("Test Store", result.getName());
//        verify(storeRepository, times(1)).save(any(Store.class));
//    }
//
//    @Test
//    public void testUpdate() {
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
//        when(storeRepository.save(any(Store.class))).thenReturn(store);
//
//        Store result = storeService.update(1L, storeDto, null);
//
//        assertNotNull(result);
//        assertEquals("Test Store", result.getName());
//        verify(storeRepository, times(1)).findById(1L);
//        verify(storeRepository, times(1)).save(any(Store.class));
//    }
//
//    @Test
//    public void testDelete() {
//        doNothing().when(storeRepository).delete(any(Store.class));
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
//
//        storeService.delete(1L);
//
//        verify(storeRepository, times(1)).findById(1L);
//        verify(storeRepository, times(1)).delete(any(Store.class));
//    }
//}
