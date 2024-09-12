package org.farozy.utility;

import org.farozy.exception.ResourceNotFoundException;

import java.util.Optional;
import java.util.function.Function;

public class EntityUtils {

    public static <T> T findEntity(
            String identifier,
            Function<Long, Optional<T>> findById,
            Function<String, Optional<T>> findByName,
            String entityName
    ) {
        try {
            Long id = Long.parseLong(identifier);
            return findById.apply(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("The %s with the specified ID does not exist", entityName)
                    ));
        } catch (NumberFormatException e) {
            return findByName.apply(identifier)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("The %s with the specified NAME does not exists", entityName)
                    ));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }


}
