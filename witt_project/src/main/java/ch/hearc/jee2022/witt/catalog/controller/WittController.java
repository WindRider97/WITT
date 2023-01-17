package ch.hearc.jee2022.witt.catalog.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.hearc.jee2022.witt.catalog.model.Comment;
import ch.hearc.jee2022.witt.catalog.model.Post;
import ch.hearc.jee2022.witt.catalog.service.CatalogService;

@Controller
public class WittController {
	
	@Autowired
	CatalogService catalogService;
	public boolean startup = true;
	
	@GetMapping(value = {"/", "/index"})
	public String showIndexPage(Model model) {
		if(startup) {
			startup = false;
			catalogService.startApplication();
		}
		model.addAttribute("witt_posts", catalogService.getAllPosts());
		model.addAttribute("witt_post", new Post());

		return "index";
	}
	
	@GetMapping(value = "/save-post")
	public String showNewPostForm(Model model) {
		model.addAttribute("witt_post", new Post());
	    model.addAttribute("isNew",true);
	    model.addAttribute("isEdit",false);
		return "save_post";
	}
	
	@GetMapping(value = "/show-post/{id}")
	public String showPost(Model model, @PathVariable("id") int id) {
		model.addAttribute("witt_post", catalogService.getPostById(id));
		model.addAttribute("comments", catalogService.getPostById(id).getComments());
		model.addAttribute("comment", new Comment());
		return "show_post";
	}
	
	@PostMapping(value = "/store-post")
    public String savePost(@ModelAttribute Post witt_post, BindingResult errors, Model model,@RequestParam String type) {
		if(type.equals("new")) {
			witt_post.setSavedAt(LocalDateTime.now());
			catalogService.addPost(witt_post);
		} else {
			catalogService.deletePost(witt_post.getId());
			witt_post.setSavedAt(LocalDateTime.now());
			catalogService.addPost(witt_post);
		}
		return "redirect:/index";
	}
	
	@PostMapping(value = "/store-comment")
    public String savePost(@ModelAttribute Comment comment, BindingResult errors, Model model, long post_id) {
		comment.setSavedAt(LocalDateTime.now());
		comment.setPost(catalogService.getPostById(post_id));
		catalogService.addComment(comment);
		
		return ("redirect:/show-post/" + post_id);
	}
	
	@PostMapping(value = "/delete-post")
	private String deletePost(@RequestParam Integer id) {
		Post post = catalogService.getPostById(id);
		for(Comment comment : post.getComments())
		{
			catalogService.deleteComment(comment.getId());
		}	
		catalogService.deletePost(id);
		
		return "redirect:/index";
	}
	
	@PostMapping(value = "/edit-post")
	public String showEditPostForm(Model model, @RequestParam int id) {
		Post postToEdit = catalogService.getPostById(id);
		
		model.addAttribute("witt_post", postToEdit);
		model.addAttribute("isEdit", true);
		model.addAttribute("isNew", false);
		
		return "save_post";
	}

	
}
