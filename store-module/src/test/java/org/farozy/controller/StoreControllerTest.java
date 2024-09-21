//package org.farozy.controller;
//
//import org.farozy.dto.StoreDto;
//import org.farozy.entity.Store;
//import org.farozy.payload.ApiResponse;
//import org.farozy.service.StoreService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class StoreControllerTest {
//
//    @InjectMocks
//    private StoreController storeController;
//
//    @Mock
//    private StoreService storeService;
//
//    @Mock
//    private MultipartFile multipartFile;
//
//    private StoreDto storeDto;
//    private Store store;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        storeDto = new StoreDto();
//        storeDto.setName("Test Store");
//        storeDto.setDescription("Test Description");
//        storeDto.setAddress("Test Address");
//        store = new Store();
//        store.setId(1L);
//        store.setName("Test Store");
//    }
//
//    @Test
//    void retrieveAll_ShouldReturnStores() {
//        when(storeService.findAll()).thenReturn(Collections.singletonList(store));
//
//        ResponseEntity<ApiResponse<List<Store>>> response = storeController.retrieveAll();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Stores retrieved all successfully", Objects.requireNonNull(response.getBody()).getMessage());
//        verify(storeService, times(1)).findAll();
//    }
//
//    @Test
//    void findById_ShouldReturnStore() {
//        when(storeService.findById(1L)).thenReturn(store);
//
//        ResponseEntity<ApiResponse<Store>> response = storeController.findById(1L);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Store by ID retrieved successfully", Objects.requireNonNull(response.getBody()).getMessage());
//        verify(storeService, times(1)).findById(1L);
//    }
//
//    @Test
//    void create_ShouldReturnCreatedStore() {
//        when(storeService.save(any(StoreDto.class), any(MultipartFile.class))).thenReturn(store);
//
//        ResponseEntity<ApiResponse<Store>> response = storeController.create(storeDto, multipartFile);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals("Store created successfully", Objects.requireNonNull(response.getBody()).getMessage());
//        verify(storeService, times(1)).save(any(StoreDto.class), any(MultipartFile.class));
//    }
//
//    @Test
//    void edit_ShouldReturnUpdatedStore() {
//        when(storeService.update(eq(1L), any(StoreDto.class), any(MultipartFile.class))).thenReturn(store);
//
//        ResponseEntity<ApiResponse<Store>> response = storeController.edit(1L, storeDto, multipartFile);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Store updated successfully", Objects.requireNonNull(response.getBody()).getMessage());
//        verify(storeService, times(1)).update(eq(1L), any(StoreDto.class), any(MultipartFile.class));
//    }
//
//    @Test
//    void destroy_ShouldDeleteStore() {
//        doNothing().when(storeService).delete(1L);
//
//        ResponseEntity<ApiResponse<Store>> response = storeController.destroy(1L);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Store deleted successfully", Objects.requireNonNull(response.getBody()).getMessage());
//        verify(storeService, times(1)).delete(1L);
//    }
//}
