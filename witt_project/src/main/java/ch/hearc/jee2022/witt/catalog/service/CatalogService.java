package ch.hearc.jee2022.witt.catalog.service;

import java.util.List;

import ch.hearc.jee2022.witt.catalog.model.Comment;
import ch.hearc.jee2022.witt.catalog.model.Post;

public interface CatalogService {
	
	//General
	public void startApplication();
	
	//WITT post
	public void addPost(Post post);
	
	public List<Post> getAllPosts();
	
	public void deletePost(long id);
	
	public void updatePost(Post post);
	
	public Post getPostById(long id);
	
	//Comment
	public void addComment(Comment comment);
	
	public void deleteComment(long id);
	//User
	
	//User Post
}
