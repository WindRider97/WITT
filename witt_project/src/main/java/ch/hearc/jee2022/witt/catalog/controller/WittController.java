package ch.hearc.jee2022.witt.catalog.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ch.hearc.jee2022.witt.catalog.model.Comment;
import ch.hearc.jee2022.witt.catalog.model.Post;
import ch.hearc.jee2022.witt.catalog.model.WITTUser;
import ch.hearc.jee2022.witt.catalog.service.CatalogService;
import jakarta.servlet.http.HttpSession;

@Controller
public class WittController {

	@Autowired
	CatalogService catalogService;
	public boolean startup = true;

	@Autowired
	HttpSession userSession;

	public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images";

	@GetMapping(value = { "/", "/index" })
	public String showIndexPage(Model model) {
		if (startup) {
			userSession.setAttribute("user", null);
			userSession.setAttribute("isAdmin", null);
			userSession.setAttribute("username", null);
			startup = false;
			catalogService.startApplication();

		}
		model.addAttribute("witt_posts", catalogService.getAllPosts());
		model.addAttribute("witt_post", new Post());
		model.addAttribute("userSession", userSession);

		return "redirect:/index/0";
	}

	@GetMapping(value = { "/index/{pageNumber}" })
	public String showIndexPage(Model model, @PathVariable int pageNumber) {
		if (startup) {
			userSession.setAttribute("user", null);
			startup = false;
			catalogService.startApplication();
		}
		model.addAttribute("witt_posts", catalogService.getAllPosts(pageNumber));
		int numberOfPages = catalogService.getAllPosts().size();
		model.addAttribute("numberOfPages", numberOfPages);
		model.addAttribute("witt_post", new Post());
		model.addAttribute("userSession", userSession);

		return "index";
	}

	// handler method to handle user registration form request
	@GetMapping("/register")
	public String showRegistrationForm(Model model) {
		model.addAttribute("userSession", userSession);
		// create model object to store form data
		WITTUser user = new WITTUser();
		model.addAttribute("wittuser", user);
		return "register";
	}

	// handler method to handle user login form request
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("userSession", userSession);
		if (userSession.getAttribute("user") == null) {
			WITTUser user = new WITTUser();
			model.addAttribute("wittuser", user);
			return "login";

		}
		return "redirect:/index";
	}

	@PostMapping(value = "/start-session")
	public String startSession(@ModelAttribute WITTUser user, BindingResult errors, Model model) {
		WITTUser witt_user = null;
		try {
			witt_user = catalogService.getUserByName(user.getUsername());
		} catch (Exception e) {
			model.addAttribute("userSession", userSession);
			return "redirect:/login";
		}

		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		if (bCryptPasswordEncoder.matches(user.getPassword(), witt_user.getPassword())) {
			userSession.setAttribute("user", witt_user.getId());
			userSession.setAttribute("isAdmin", witt_user.isAdmin());
			userSession.setAttribute("username", witt_user.getUsername());
		} else {
			model.addAttribute("userSession", userSession);
			return "redirect:/login";
		}
		model.addAttribute("userSession", userSession);
		return "redirect:/index";

	}

	@GetMapping(value = { "/logout" })
	public String Logout(Model model) {

		userSession.setAttribute("user", null);
		userSession.setAttribute("isAdmin", null);
		userSession.setAttribute("username", null);

		return "redirect:/index";
	}

	@PostMapping("/register/store-user")
	public String registration(@ModelAttribute("wittuser") WITTUser user, BindingResult result, Model model) {
		model.addAttribute("userSession", userSession);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		catalogService.addUser(user);
		return startSession(user, result, model);
	}

	@GetMapping(value = "/save-post")
	public String showNewPostForm(Model model) {
		if (userSession.getAttribute("user") == null) {
			return "redirect:/index";
		}
		model.addAttribute("userSession", userSession);
		model.addAttribute("witt_post", new Post());
		model.addAttribute("isNew", true);
		model.addAttribute("isEdit", false);
		return "save_post";
	}

	@GetMapping(value = "/show-post/{id}")
	public String showPost(Model model, @PathVariable("id") int id) {

		model.addAttribute("userSession", userSession);
		model.addAttribute("witt_post", catalogService.getPostById(id));
		model.addAttribute("comments", catalogService.getPostById(id).getComments());
		model.addAttribute("comment", new Comment());
		return "show_post";
	}

	@PostMapping(value = "/store-post")
	public String savePost(@ModelAttribute Post witt_post, BindingResult errors, Model model, @RequestParam String type,
			@RequestParam("image") MultipartFile file) throws IOException {
		if (userSession.getAttribute("user") == null) {
			return "redirect:/index";
		}
		model.addAttribute("userSession", userSession);
		if (type.equals("new")) {
			StringBuilder fileNames = new StringBuilder();
			Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
			fileNames.append(file.getOriginalFilename());
			Files.write(fileNameAndPath, file.getBytes());
			witt_post.setImagePath(file.getOriginalFilename());
			witt_post.setSavedAt(LocalDateTime.now());
			witt_post.setUser(catalogService.getUserByName((String) userSession.getAttribute("username")));

			catalogService.addPost(witt_post);
			return "redirect:/index";
		} else {
			witt_post.setUser(catalogService.getUserByName((String) userSession.getAttribute("username")));
			witt_post.setSavedAt(LocalDateTime.now());
			catalogService.addPost(witt_post);

			return "redirect:/show-post/" + (witt_post.getId());
		}

	}

	@PostMapping(value = "/store-comment")
	public String savePost(@ModelAttribute Comment comment, BindingResult errors, Model model, long post_id) {
		if (userSession.getAttribute("user") == null) {
			return "redirect:/index";
		}
		model.addAttribute("userSession", userSession);
		comment.setSavedAt(LocalDateTime.now());
		comment.setPost(catalogService.getPostById(post_id));
		comment.setUser(catalogService.getUserByName((String) userSession.getAttribute("username")));
		catalogService.addComment(comment);

		return ("redirect:/show-post/" + post_id);
	}

	@PostMapping(value = "/delete-post")
	private String deletePost(@RequestParam Integer id) {
		if (userSession.getAttribute("user") == null) {
			return "redirect:/index";
		}
		Post post = catalogService.getPostById(id);
		for (Comment comment : post.getComments()) {
			catalogService.deleteComment(comment.getId());
		}
		catalogService.deletePost(id);

		return "redirect:/index";
	}

	@PostMapping(value = "/edit-post")
	public String showEditPostForm(Model model, @RequestParam int id) {
		if (userSession.getAttribute("user") == null) {
			return "redirect:/index";
		}
		model.addAttribute("userSession", userSession);
		Post postToEdit = catalogService.getPostById(id);

		model.addAttribute("witt_post", postToEdit);
		model.addAttribute("isEdit", true);
		model.addAttribute("isNew", false);

		return "save_post";
	}

}
