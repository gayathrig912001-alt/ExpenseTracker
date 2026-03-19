package com.example.Learning.Repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.Learning.Entity.UserEntity;

@Repository
public interface IUserRepository extends CrudRepository<UserEntity, Long>{

	//UserEntity findbyName(String userName);

	boolean existsByEmail(String email);

	UserEntity findByEmail(String email);

	Optional<UserEntity> findByUsername(String username);

}
