package at.htl.ecopoints.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.concurrent.CompletionException;

public class ModelMapper<T> {
    ObjectMapper mapper = new ObjectMapper();
    Class<? extends T> classType;

    public ModelMapper(Class<? extends T> classType) {
        this.classType = classType;
    }

    public byte[] toResource(T model) {
        try {
            return mapper.writeValueAsBytes(model);
        } catch (JsonProcessingException e) {
            throw new CompletionException(e);
        }
    }

    public T fromResource(byte[] bytes) {
        try {
            return mapper.readValue(bytes, classType);
        } catch (IOException e) {
            throw new CompletionException(e);
        }
    }

    public T clone(T model) {
        return fromResource(toResource(model));
    }
}
