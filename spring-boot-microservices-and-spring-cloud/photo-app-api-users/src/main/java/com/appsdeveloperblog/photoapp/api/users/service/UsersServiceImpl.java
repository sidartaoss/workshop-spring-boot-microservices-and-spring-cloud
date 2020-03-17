package com.appsdeveloperblog.photoapp.api.users.service;

import com.appsdeveloperblog.photoapp.api.users.data.AlbumsServiceClient;
import com.appsdeveloperblog.photoapp.api.users.data.UserEntity;
import com.appsdeveloperblog.photoapp.api.users.data.UsersRepository;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.AlbumResponseModel;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private static final Logger LOG = LoggerFactory.getLogger(UsersServiceImpl.class);

    private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
//    private RestTemplate restTemplate;
    private Environment environment;
    private AlbumsServiceClient albumsServiceClient;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder,
//                            RestTemplate restTemplate,
                            AlbumsServiceClient albumsServiceClient,
                            Environment environment) {
        this.usersRepository = usersRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//        this.restTemplate = restTemplate;
        this.albumsServiceClient = albumsServiceClient;
        this.environment = environment;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        //hard coding just to pass the implementation phase
//        userEntity.setEncryptedPassword("test");

        final UserEntity savedUserEntity = usersRepository.save(userEntity);

        return modelMapper.map(savedUserEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity byEmail = usersRepository.findByEmail(username);
        if (byEmail == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(
                byEmail.getEmail(),
                byEmail.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>()
        );
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        final UserEntity byEmail = usersRepository.findByEmail(email);
        if (byEmail == null) {
            throw new UsernameNotFoundException(email);
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(byEmail, UserDto.class);

    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = this.usersRepository.findByUserId(userId);
        if (userId == null) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
/*

        String albumsUrl = String.format(environment.getProperty("albums.url"), userId);

        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(
                albumsUrl, HttpMethod.GET
                ,  null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
                });

        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();

*/


/*        List<AlbumResponseModel> albumsList = null;
        try {
            albumsList = this.albumsServiceClient.getAlbums(userId);
        } catch (FeignException e) {
            LOG.error(e.getMessage());
        }*/

        LOG.info("Before calling albums Microservice");

        List<AlbumResponseModel> albumsList = this.albumsServiceClient.getAlbums(userId);

        LOG.info("After calling albums Microservice");

        userDto.setAlbums(albumsList);

        return userDto;
    }
}
