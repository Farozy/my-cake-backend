package org.farozy.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.farozy.dto.StoreDto;
import org.farozy.entity.Store;
import org.farozy.exception.ResourceAlreadyExistsException;
import org.farozy.exception.ResourceNotFoundException;
import org.farozy.helper.FileUploadHelper;
import org.farozy.repository.StoreRepository;
import org.farozy.utility.FileUtils;
import org.farozy.validation.annotation.permission.UserPermission;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private static final String storeModule = "store-module";

    @Override
    @Transactional
    @UserPermission.UserRead
    public List<Store> findAll() {
        try {
            return storeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while fetching stores: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Store findById(Long id) {
        return getStoreById(id);
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Store save(StoreDto request, MultipartFile imageFile) {
        try {
            return saveOrUpdateStore(null, request, imageFile);
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while saving store: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    @UserPermission.UserRead
    public Store update(Long id, StoreDto request, MultipartFile imageFile) {
        try {
            return saveOrUpdateStore(id, request, imageFile);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (ResourceAlreadyExistsException e) {
            throw new ResourceAlreadyExistsException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while updating store: " + ex.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Store store = getStoreById(id);

            FileUtils.deleteFile(storeModule, store.getImage());

            storeRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception ex) {
            throw new RuntimeException("Error occurred while deleting by id store: " + ex.getMessage());
        }
    }

    private Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The store with the specified ID does not exist"
                ));
    }

    private Store saveOrUpdateStore(Long id, StoreDto request, MultipartFile imageFile) throws IOException {
        Store store = (id == null) ? new Store() : getStoreById(id);

        if (id != null) FileUtils.deleteFile(storeModule, store.getImage());

        store.setImage(FileUploadHelper.processSaveImage(storeModule, imageFile));

        return saveOrUpdateStoreFromRequest(store, request);
    }

    public Store saveOrUpdateStoreFromRequest(Store store, StoreDto request) {
        store.setName(request.getName());
        store.setDescription(request.getDescription());
        store.setAddress(request.getAddress());

        store.setLatitude(request.getLatitude() != null ? request.getLatitude() : store.getLatitude());
        store.setLongitude(request.getLongitude() != null ? request.getLongitude() : store.getLongitude());

        return storeRepository.save(store);
    }


}
