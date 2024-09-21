//package org.farozy.repository;
//
//import org.farozy.entity.Store;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class StoreRepositoryTest {
//
//    private final StoreRepository storeRepository;
//
//    public StoreRepositoryTest(StoreRepository storeRepository) {
//        this.storeRepository = storeRepository;
//    }
//
//    @Test
//    @Rollback(false)
//    public void testCreateStore() {
//        Store store = new Store();
//        store.setName("Test Store");
//
//        Store savedStore = storeRepository.save(store);
//
//        assertThat(savedStore).isNotNull();
//        assertThat(savedStore.getId()).isGreaterThan(0);
//    }
//
//    @Test
//    public void testFindStoreById() {
//        Store store = new Store();
//        store.setName("Test Store");
//        Store savedStore = storeRepository.save(store);
//
//        Optional<Store> foundStore = storeRepository.findById(savedStore.getId());
//
//        assertThat(foundStore).isPresent();
//        assertThat(foundStore.get().getName()).isEqualTo("Test Store");
//    }
//
//    @Test
//    public void testDeleteStore() {
//        Store store = new Store();
//        store.setName("Test Store");
//        Store savedStore = storeRepository.save(store);
//        Long storeId = savedStore.getId();
//
//        storeRepository.deleteById(storeId);
//
//        Optional<Store> deletedStore = storeRepository.findById(storeId);
//        assertThat(deletedStore).isNotPresent();
//    }
//}
