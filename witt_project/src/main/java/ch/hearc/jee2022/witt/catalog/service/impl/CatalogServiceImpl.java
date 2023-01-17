package ch.hearc.jee2022.witt.catalog.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hearc.jee2022.witt.catalog.model.Comment;
import ch.hearc.jee2022.witt.catalog.model.Post;
import ch.hearc.jee2022.witt.catalog.repository.CommentRepository;
import ch.hearc.jee2022.witt.catalog.repository.PostRepository;
import ch.hearc.jee2022.witt.catalog.service.CatalogService;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	PostRepository postRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Override
	public void startApplication() {
		Post post1 = new Post("otter.jpg", "WITT: Otter wearing a cap", "I found this funny otter wearing a cap and eversince it became my go to placeholder picture :shrug:" );
		Post post2 = new Post("otter.jpg", "WITT: Another otter wearing a cap", "I'm seeing this otter everywhere, why ???" );
		Post post3 = new Post("otter.jpg", "WITT: Otter wearing a cap, again...", "Why am I seeing this otter everywhere? Is there a meaning behind it or is it just a funny otter wearing a cap ?" );

		Comment comment1 = new Comment("Ah ouais super sympa la loutre");
		Comment comment2 = new Comment("GoOgLe EsT tOn AmI");
		Comment comment3 = new Comment("A court d'idées pour le troisième commentaire");
		Comment comment4 = new Comment("Je suis partout");
		
		comment1.setPost(post1);
		comment2.setPost(post1);
		comment3.setPost(post1);
		comment4.setPost(post1);
		comment4.setPost(post2);

		
		postRepository.saveAll(Arrays.asList(post1, post2, post3));
		commentRepository.saveAll(Arrays.asList(comment1, comment2, comment3));
	}

	@Override
	public void addPost(Post post) {
		postRepository.save(post);
		
	}

	@Override
	public List<Post> getAllPosts() {
		List<Post> posts = new ArrayList<Post>();
		postRepository.findAll().forEach(posts::add);
		posts.sort((p1,p2)->p2.getSavedAt().compareTo(p1.getSavedAt()));
		return posts;
	}

	@Override
	public void deletePost(long id) {
		postRepository.deleteById(id);
		
	}

	@Override
	public void updatePost(Post post) {
		postRepository.save(post);
		
	}

	@Override
	public Post getPostById(long id) {
		return postRepository.findById(id).get();
		
	}

	@Override
	public void addComment(Comment comment) {
		commentRepository.save(comment);
		
	}

	@Override
	public void deleteComment(long id) {
		commentRepository.deleteById(id);
		
	}

	

}
