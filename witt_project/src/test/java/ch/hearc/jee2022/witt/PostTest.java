package ch.hearc.jee2022.witt;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.hearc.jee2022.witt.catalog.model.Comment;
import ch.hearc.jee2022.witt.catalog.model.Post;

public class PostTest {
	@Test
	public void testCommentSorting() {
		Post post = new Post("otter.jpg", "WITT: Otter wearing a cap",
				"I found this funny otter wearing a cap and eversince it became my go to placeholder picture :shrug:");

		Comment comment1 = new Comment("Ah ouais super sympa la loutre");
		Comment comment2 = new Comment("GoOgLe EsT tOn AmI");
		Comment comment3 = new Comment("A court d'idées pour le troisième commentaire");
		
		comment1.setSavedAt(LocalDateTime.now());
		comment2.setSavedAt(LocalDateTime.of(2019, 2, 5, 9, 18));
		comment3.setSavedAt(LocalDateTime.of(2022, 1, 12, 12, 22));
		
		comment1.setId(1);
		comment2.setId(2);
		comment3.setId(3);
		
		post.addComment(comment3);
		post.addComment(comment2);
		post.addComment(comment1);


		Integer[] expectedCommentId = new Integer[3];
		int i = 0;
		for (Comment comment : post.getComments()) {
			expectedCommentId[i] = (int) comment.getId();
			i++;
		}
		Integer[] resultCommentId = { 1, 3, 2 };

		Assertions.assertArrayEquals(expectedCommentId, resultCommentId);

	}

}
