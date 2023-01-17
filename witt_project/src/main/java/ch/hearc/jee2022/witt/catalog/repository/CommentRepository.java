package ch.hearc.jee2022.witt.catalog.repository;

import org.springframework.data.repository.CrudRepository;

import ch.hearc.jee2022.witt.catalog.model.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

}
