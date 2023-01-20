package ch.hearc.jee2022.witt.catalog.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import ch.hearc.jee2022.witt.catalog.model.Post;

public interface PostRepository extends CrudRepository<Post, Long>, PagingAndSortingRepository<Post, Long>{

}
