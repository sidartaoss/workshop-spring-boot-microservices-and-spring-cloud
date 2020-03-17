package com.appsdeveloperblog.photoapp.api.users.data;

import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

//@FeignClient(name = "albums-ws", fallback = AlbumsFallback.class)
@FeignClient(name = "albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {
    @GetMapping("/users/{id}/albums")
    List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

@Component
//class AlbumsFallback implements AlbumsServiceClient {
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

    @Override
    public AlbumsServiceClient create(Throwable cause) {
        return new AlbumsServiceClientFallback(cause);
    }

/*    @Override
    public List<AlbumResponseModel> getAlbums(String id) {
        return new ArrayList<>();
    }*/
}

class AlbumsServiceClientFallback implements AlbumsServiceClient {

    private Logger LOG = LoggerFactory.getLogger(AlbumsServiceClientFallback.class);

    private Throwable throwable;

    public AlbumsServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public List<AlbumResponseModel> getAlbums(String id) {
        if (throwable instanceof FeignException && ((FeignException) throwable).status() == 404) {
            LOG.error("404 error took place when getAlbums was called with userId: " + id + ". Error message: " + throwable.getLocalizedMessage());
        } else {
            LOG.error("Other error took place: " + throwable.getLocalizedMessage());
        }
        return new ArrayList<>();
    }
}