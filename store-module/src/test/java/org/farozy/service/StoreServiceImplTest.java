//package org.farozy.service;
//
//import org.farozy.dto.StoreDto;
//import org.farozy.entity.Store;
//import org.farozy.exception.ResourceAlreadyExistsException;
//import org.farozy.exception.ResourceNotFoundException;
//import org.farozy.helper.FileUploadHelper;
//import org.farozy.repository.StoreRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Sort;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.math.BigDecimal;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//public class StoreServiceImplTest {
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @Mock
//    private MultipartFile imageFile;
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
//        when(storeRepository.findAll(any(Sort.class))).thenReturn(List.of(store));
//
//        List<Store> result = storeService.findAll();
//
//        assertEquals(1, result.size());
//        assertEquals("Test Store", result.get(0).getName());
//        verify(storeRepository, times(1)).findAll(any(Sort.class));
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
//    public void testFindByIdNotFound() {
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.findById(1L));
//    }
//
//    @Test
//    public void testSave() {
//        when(FileUploadHelper.processSaveImage(anyString(), any(MultipartFile.class))).thenReturn("uploaded_image.png");
//        when(storeRepository.save(any(Store.class))).thenReturn(store);
//
//        Store result = storeService.save(storeDto, imageFile);
//
//        assertNotNull(result);
//        assertEquals("Test Store", result.getName());
//        verify(storeRepository, times(1)).save(any(Store.class));
//    }
//
//    @Test
//    public void testSaveAlreadyExists() {
//        when(storeRepository.save(any(Store.class))).thenThrow(new ResourceAlreadyExistsException("Store already exists"));
//
//        assertThrows(ResourceAlreadyExistsException.class, () -> storeService.save(storeDto, imageFile));
//    }
//
//    @Test
//    public void testUpdate() {
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
//        when(FileUploadHelper.processSaveImage(anyString(), any(MultipartFile.class))).thenReturn("uploaded_image.png");
//        when(storeRepository.save(any(Store.class))).thenReturn(store);
//
//        Store result = storeService.update(1L, storeDto, imageFile);
//
//        assertNotNull(result);
//        assertEquals("Test Store", result.getName());
//        verify(storeRepository, times(1)).save(any(Store.class));
//    }
//
//    @Test
//    public void testUpdateNotFound() {
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.update(1L, storeDto, imageFile));
//    }
//
//    @Test
//    public void testDelete() {
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
//        doNothing().when(storeRepository).deleteById(anyLong());
//
//        assertDoesNotThrow(() -> storeService.delete(1L));
//        verify(storeRepository, times(1)).deleteById(1L);
//    }
//
//    @Test
//    public void testDeleteNotFound() {
//        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () -> storeService.delete(1L));
//    }
//}
