//package org.farozy.entity;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class StoreTest {
//
//    private static EntityManagerFactory entityManagerFactory;
//    private static EntityManager entityManager;
//
//    @BeforeAll
//    public static void setUp() {
//        entityManagerFactory = Persistence.createEntityManagerFactory("your-persistence-unit");
//        entityManager = entityManagerFactory.createEntityManager();
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        entityManager.close();
//        entityManagerFactory.close();
//    }
//
//    @Test
//    public void testCreateStore() {
//        Store store = new Store();
//        store.setName("Test Store");
//        store.setDescription("This is a test store description.");
//        store.setAddress("123 Test Address");
//        store.setOpeningTime(LocalTime.of(9, 0));
//        store.setClosingTime(LocalTime.of(17, 0));
//        store.setImage("test_image.png");
//        store.setLatitude(new BigDecimal("12.345678"));
//        store.setLongitude(new BigDecimal("98.765432"));
//
//        entityManager.getTransaction().begin();
//        entityManager.persist(store);
//        entityManager.getTransaction().commit();
//
//        assertNotNull(store.getId(), "Store ID should not be null after persisting");
//    }
//
//    @Test
//    public void testUpdateStore() {
//        // Create and persist a store
//        Store store = new Store();
//        store.setName("Update Test Store");
//        store.setDescription("Description for update test.");
//        store.setAddress("456 Update Address");
//        store.setOpeningTime(LocalTime.of(10, 0));
//        store.setClosingTime(LocalTime.of(18, 0));
//        store.setImage("update_image.png");
//        store.setLatitude(new BigDecimal("12.345678"));
//        store.setLongitude(new BigDecimal("98.765432"));
//
//        entityManager.getTransaction().begin();
//        entityManager.persist(store);
//        entityManager.getTransaction().commit();
//
//        // Update the store
//        entityManager.getTransaction().begin();
//        store.setDescription("Updated description.");
//        entityManager.merge(store);
//        entityManager.getTransaction().commit();
//
//        // Verify the update
//        Store updatedStore = entityManager.find(Store.class, store.getId());
//        assertEquals("Updated description.", updatedStore.getDescription());
//    }
//
//    @Test
//    public void testDeleteStore() {
//        // Create and persist a store
//        Store store = new Store();
//        store.setName("Delete Test Store");
//        store.setDescription("This store will be deleted.");
//        store.setAddress("789 Delete Address");
//        store.setOpeningTime(LocalTime.of(8, 0));
//        store.setClosingTime(LocalTime.of(16, 0));
//        store.setImage("delete_image.png");
//        store.setLatitude(new BigDecimal("12.345678"));
//        store.setLongitude(new BigDecimal("98.765432"));
//
//        entityManager.getTransaction().begin();
//        entityManager.persist(store);
//        entityManager.getTransaction().commit();
//
//        // Delete the store
//        entityManager.getTransaction().begin();
//        entityManager.remove(store);
//        entityManager.getTransaction().commit();
//
//        // Verify the store has been deleted
//        Store deletedStore = entityManager.find(Store.class, store.getId());
//        assertNull(deletedStore, "Store should be null after deletion");
//    }
//}
