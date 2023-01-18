package ch.hearc.jee2022.witt.catalog.repository;

import org.springframework.data.repository.CrudRepository;

import ch.hearc.jee2022.witt.catalog.model.WITTUser;

public interface UserRepository extends CrudRepository<WITTUser, Long>{
	WITTUser findByUsername(String username);
}
