package ch.hearc.jee2022.witt.catalog.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ch.hearc.jee2022.witt.catalog.model.Comment;
import ch.hearc.jee2022.witt.catalog.model.Post;
import ch.hearc.jee2022.witt.catalog.model.WITTUser;
import ch.hearc.jee2022.witt.catalog.repository.CommentRepository;
import ch.hearc.jee2022.witt.catalog.repository.PostRepository;
import ch.hearc.jee2022.witt.catalog.repository.UserRepository;
import ch.hearc.jee2022.witt.catalog.service.CatalogService;

@Service
public class CatalogServiceImpl implements CatalogService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public void startApplication() {
		Post post1 = new Post("otter.jpg", "WITT: Otter wearing a cap",
				"I found this funny otter wearing a cap and eversince it became my go to placeholder picture :shrug:");
		Post post2 = new Post("otter.jpg", "WITT: Another otter wearing a cap",
				"I'm seeing this otter everywhere, why ???");
		Post post3 = new Post("otter.jpg", "WITT: Otter wearing a cap, again...",
				"Why am I seeing this otter everywhere? Is there a meaning behind it or is it just a funny otter wearing a cap ?");

		Comment comment1 = new Comment("Ah ouais super sympa la loutre");
		Comment comment2 = new Comment("GoOgLe EsT tOn AmI");
		Comment comment3 = new Comment("A court d'idées pour le troisième commentaire");

		comment1.setPost(post1);
		comment2.setPost(post1);
		comment3.setPost(post1);
		
		WITTUser user1 = new WITTUser("admin", "password", true);
		WITTUser user2 = new WITTUser("Benjamin", "password1", false);
		WITTUser user3 = new WITTUser("Guillaume", "password2", false);
		WITTUser user4 = new WITTUser("GrosTroll", "password3", false);
		
		comment1.setUser(user2);
		comment2.setUser(user4);
		comment3.setUser(user3);
		
		post1.setUser(user2);
		post2.setUser(user2);
		post3.setUser(user2);


		userRepository.saveAll(Arrays.asList(user1, user2, user3, user4));
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
		posts.sort((p1, p2) -> p2.getSavedAt().compareTo(p1.getSavedAt()));
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

	@Override
	public void addUser(WITTUser user) {
		userRepository.save(user);
	}

	@Override
	public WITTUser getUserByName(String name) {
		return userRepository.findByUsername(name);
	}

	@Override
	public List<Post> getAllPosts(int pageNumber) {
		PageRequest paging = PageRequest.of(pageNumber, 1);
		Page<Post> page = postRepository.findAll(paging);
		return page.toList();
	}


	
	

}
