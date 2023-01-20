package ch.hearc.jee2022.witt.catalog.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String imagePath;
	private String title;
	private String description;
	private LocalDateTime savedAt;
	
	@OneToMany(mappedBy="post", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
	private Set<Comment> comments;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=true)
	private WITTUser user;
	
	public List<Comment> getComments() {
		List<Comment> commentList = new ArrayList<Comment>(comments);
	    commentList.sort((c1,c2)->c2.getSavedAt().compareTo(c1.getSavedAt()));
		return commentList;
	}
	
	public void addComment(Comment comment) {
		comments.add(comment);
	}
	
	public WITTUser getUser() {
		return user;
	}

	public void setUser(WITTUser user) {
		this.user = user;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	public Post(String imagePath, String title, String description) {
		this.imagePath = imagePath;
		this.title = title;
		this.description = description;
		this.savedAt =  LocalDateTime.now();
	}
	
	public Post() {}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public LocalDateTime getSavedAt() {
		return savedAt;
	}

	public void setSavedAt(LocalDateTime savedAt) {
		this.savedAt = savedAt;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Post other = (Post) obj;
		return Objects.equals(id, other.id);
	}
	

}
