package ch.hearc.jee2022.witt.catalog.model;

import java.util.Objects;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "witt_user")
public class WITTUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String username;

	private String password;

	private boolean isAdmin;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Post> posts;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)

	private Set<Comment> comments;
	
	public void addComment(Comment comment) {
		comments.add(comment);
	}
	
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	public void addPost(Post post) {
		posts.add(post);
	}
	
	public void setPost(Set<Post> posts) {
		this.posts = posts;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, isAdmin, password, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WITTUser other = (WITTUser) obj;
		return id == other.id && isAdmin == other.isAdmin && Objects.equals(password, other.password)
				&& Objects.equals(username, other.username);
	}

	public WITTUser(String username, String password, boolean isAdmin) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		this.username = username;
		this.password = bCryptPasswordEncoder.encode(password);
		this.isAdmin = isAdmin;
	}

	public WITTUser() {
	}
	
}
