//package org.farozy.controller;
//
//import org.farozy.dto.StoreDto;
//import org.farozy.entity.Store;
//import org.farozy.service.StoreService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//class StoreControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private StoreService storeService;
//
//    @InjectMocks
//    private StoreController storeController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();
//    }
//
//    @Test
//    void retrieveAllStores_shouldReturnListOfStores() throws Exception {
//        Store store = new Store();
//        store.setId(1L);
//        store.setName("Store 1");
//
//        List<Store> stores = Collections.singletonList(store);
//
//        when(storeService.findAll()).thenReturn(stores);
//
//        mockMvc.perform(get("/api/store"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].name").value("Store 1"))
//                .andExpect(jsonPath("$.message").value("Stores retrieved all successfully"));
//
//        verify(storeService, times(1)).findAll();
//    }
//
//    @Test
//    void findStoreById_shouldReturnStore() throws Exception {
//        Store store = new Store();
//        store.setId(1L);
//        store.setName("Store 1");
//
//        when(storeService.findById(1L)).thenReturn(store);
//
//        mockMvc.perform(get("/api/store/{id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.name").value("Store 1"))
//                .andExpect(jsonPath("$.message").value("Store by ID retrieved successfully"));
//
//        verify(storeService, times(1)).findById(1L);
//    }
//
//    @Test
//    void createStore_shouldReturnCreatedStore() throws Exception {
//        Store store = new Store();
//        store.setId(1L);
//        store.setName("New Store");
//
//        StoreDto storeDto = new StoreDto();
//        storeDto.setName("New Store");
//
//        when(storeService.save(any(StoreDto.class), any(MultipartFile.class))).thenReturn(store);
//
//        mockMvc.perform(multipart("/api/store")
//                        .file("image", "test-image.jpg".getBytes())
//                        .param("name", "New Store")
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.name").value("New Store"))
//                .andExpect(jsonPath("$.message").value("Store created successfully"));
//
//        verify(storeService, times(1)).save(any(StoreDto.class), any(MultipartFile.class));
//    }
//
//    @Test
//    void updateStore_shouldReturnUpdatedStore() throws Exception {
//        Store store = new Store();
//        store.setId(1L);
//        store.setName("Updated Store");
//
//        StoreDto storeDto = new StoreDto();
//        storeDto.setName("Updated Store");
//
//        when(storeService.update(eq(1L), any(StoreDto.class), any(MultipartFile.class))).thenReturn(store);
//
//        mockMvc.perform(multipart("/api/store/{id}", 1L)
//                        .file("image", "updated-image.jpg".getBytes())
//                        .param("name", "Updated Store")
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.name").value("Updated Store"))
//                .andExpect(jsonPath("$.message").value("Store updated successfully"));
//
//        verify(storeService, times(1)).update(eq(1L), any(StoreDto.class), any(MultipartFile.class));
//    }
//
//    @Test
//    void deleteStore_shouldReturnSuccessMessage() throws Exception {
//        doNothing().when(storeService).delete(1L);
//
//        mockMvc.perform(delete("/api/store/{id}", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Store deleted successfully"));
//
//        verify(storeService, times(1)).delete(1L);
//    }
//}
//
